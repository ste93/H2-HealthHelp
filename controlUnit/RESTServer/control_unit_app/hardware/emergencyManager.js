var Gpio = require('onoff').Gpio;

var buzzer = require('./buzzer');
var emergencyLed = require('./led');
var phoneLed = require('./led');
var lcd = require('./controlUnitLCD');

var pushButton = new Gpio(17, 'in', 'both');
var emergency = false;

modules.exports.initializeEmergencyProtocol = function() {

  emergencyLed.initializeLed(24);
  phoneLed.initializeLed(21);
  buzzer.initializeBuzzer(23);
  //lcd.initializeLcd();

  pushButton.watch(function (err, state) { //Watch for hardware interrupts on pushButton GPIO, specify callback function
    if (err) { //if an error
      console.error('There was an error : ', err); //output error message to console
      return;
    }
    if (emergency === 1) {
        emergency = 0;
        reset();
        //lcd.write("NO EMERGENCY");
    }
  //LED.writeSync(value); //turn LED on or off depending on the button state (0 or 1)
  });
}

  function reset(){
    emergencyLed.turnOff();
    phoneLed.turnOff();
    buzzer.stop();
    //lcd.reset();
  }

  function emergencyProtocol() {
    emergency = 1;
    emergencyLed.blink(250, 10000);
    buzzer.alarmBeep(250, 10000);
    //lcd.write("EMERGENCY");
  }

  function stopEmergency() {
    if(emergency === 1){
        callRescuers();
    }
    else {
        System.out.println("NO MORE EMERGENCY");
        //lcd.reset();
    }
  }

  module.exports.startEmergency = function () {
    emergencyProtocol();
    setTimeout(stopEmergency, 10000);
  }

    private void callRescuers(){
        console.log("CALLING EMERGENCY RESCUERS");
        //lcd.write("CALLING RESCUERS");
        buzzer.stop();
        emergencyLed.turnOn();
        phoneLed.turnOn();
        emergency = 0;
    }
}
