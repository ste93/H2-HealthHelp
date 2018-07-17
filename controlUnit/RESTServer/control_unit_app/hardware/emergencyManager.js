/**
* Hardware manager for user interaction during emergency handling.
*/

//TODO : SET TIME AS A REAL EMERGENCY HANDLING ( SHORT TIME PERIOD DURING TESTING )

var Gpio = require('onoff').Gpio;

var buzzer = require('./buzzer');
var emergencyLed = require('./emergencyLed');
var phoneLed = require('./phoneLed');
// use a button to interact with users.
var pushButton = new Gpio(25, 'in', 'rising', {debounceTimeout: 10});
var lcd = require('./controlUnitLCD');
lcd.initializeLcd();

var emergency = false;
var initialized = false;
var lastEmergencyTerminated = true;

/**
* Function that initialize the whole hardware.
*/
function initialize () {

  if (!initialized){
    console.log("initializing");
    emergencyLed.initializeLed(24);
    phoneLed.initializeLed(21);
    buzzer.initializeBuzzer(23);
    lcd.initializeLcd();

    pushButton.watch(function (err, state) { //Watch for hardware interrupts on pushButton GPIO, specify callback function
      if (err) {
        console.error('There was an error : ', err);
        return;
      }
      if (emergency) {
          emergency = false;
          reset();
          lcd.write("NO EMERGENCY");
      }
    });
    initialized = true;
  }
  console.log("Emergency Manager Modules initialized.");
}

/**
* Function that reset the whole hardware.
*/
function reset(){
  emergencyLed.turnOff();
  phoneLed.turnOff();
  buzzer.stop();
  lcd.reset();
  lastEmergencyTerminated = true;
}

/**
* Function that handle hardware output when an emergency is detected.
*/
function emergencyProtocol() {
  emergency = true;
  lastEmergencyTerminated = false;
  lcd.write("EMERGENCY");
  emergencyLed.blink(250, 10000);
  buzzer.beep();
}

/**
* Function that SIMULATE emergency call to rescuers.
* FUTURE TODO : implement real calling.
*/
function checkEmergency() {
  console.log(" -- End of control time. What to do ? ");
  buzzer.stop();
  if(emergency){
      callRescuers();
  }
  else {
      console.log("NO MORE EMERGENCY");
      emergencyLed.turnOff();
      phoneLed.turnOff();
      lcd.reset();
  }
}

/**
* Function that SIMULATE emergency call to rescuers.
* FUTURE TODO : implement real calling.
*/
function callRescuers() {
    console.log("CALLING EMERGENCY RESCUERS");
    lcd.write("CALLING RESCUERS");
    emergencyLed.turnOff();
    phoneLed.turnOn();
    emergency = false;
    // TODO : Add a button to reset the control unit instead of a timeout
    setTimeout(reset, 5000);
}

//TODO:
// start protocol and wait for X second to check if is a real emergency or a false alarm ( the user could stop it).
module.exports.startEmergency = function () {
  if (lastEmergencyTerminated){
    console.log(" -- Start Emergency ");
    if (initialized) {
    emergencyProtocol();
    setTimeout(checkEmergency, 10000);
    } else {
    initialize();
    emergencyProtocol();
    setTimeout(checkEmergency, 10000);
    }
  } else {
    console.log(" -- Already handling this emergency ! ");
  }
}
