var Gpio = require('onoff').Gpio;

var buzzer = require('./buzzer');
var emergencyLed = require('./emergencyLed');
var phoneLed = require('./phoneLed');
var lcd = require('./controlUnitLCD');
lcd.initializeLcd();

var pushButton = new Gpio(25, 'in', 'rising', {debounceTimeout: 10}); //OK
var emergency = false;
var initialized = false;

function initialize () {

  if (!initialized){
    console.log("initializing");
    emergencyLed.initializeLed(24);  
    phoneLed.initializeLed(21);    
    buzzer.initializeBuzzer(23);
    lcd.initializeLcd();

    pushButton.watch(function (err, state) { //Watch for hardware interrupts on pushButton GPIO, specify callback function
      if (err) { //if an error
        console.error('There was an error : ', err); //output error message to console
        return;
      }
      if (emergency === 1) {
          emergency = 0;
          reset();
          lcd.write("NO EMERGENCY");
      }
    });
    initialized = true;
  }
  console.log("Emergency Manager Modules initialized.");
}


  function reset(){
    emergencyLed.turnOff();
    phoneLed.turnOff();
    buzzer.stop();
    lcd.reset();
  }

  function emergencyProtocol() {
    emergency = 1;
    lcd.write("EMERGENCY");
    emergencyLed.blink(250, 10000);
    /////////////////buzzer.beep();
    
  }

  function checkEmergency() {
    console.log(" -- End of control time. What to do ? ");
    buzzer.stop();
    if(emergency === 1){
        callRescuers();
    }
    else {
        console.log("NO MORE EMERGENCY");
        emergencyLed.turnOff();
        phoneLed.turnOff();
        lcd.reset();
    }
  }

  function callRescuers() {
        console.log("CALLING EMERGENCY RESCUERS");
        lcd.write("CALLING RESCUERS");
        emergencyLed.turnOff();
        phoneLed.turnOn();
        emergency = 0;
        setTimeout(reset, 5000);
  }

  module.exports.startEmergency = function () {
    console.log(" -- StartEmergency()");
    if (initialized) {
      emergencyProtocol();
      setTimeout(checkEmergency, 10000);
    } else {
      initialize();
      emergencyProtocol();
      setTimeout(checkEmergency, 10000);
    }

  }
