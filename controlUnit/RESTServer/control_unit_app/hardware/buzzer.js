
var Gpio = require('onoff').Gpio; //include onoff to interact with the GPIO

var BUZZER;
var state;
var beepInterval;

module.exports.initializeBuzzer = function(gpioNumber) {
  //use GPIO with the specified number, and specify that it is output
   BUZZER = new Gpio(gpioNumber, 'out');
   state = false;
 }

 module.exports.beep = function() {
   if ( ! state ){
     BUZZER.writeSync(1);
     state = true;
   };
 }

 module.exports.beepFor = function(period) {
   if ( ! state ){
     BUZZER.writeSync(1);
     state = true;
   };
   setTimeout(stop, timeout);
 }

 module.exports.stop = function() {
   if ( state ){
     BUZZER.writeSync(0);
     state = false;
   };
 }

module.exports.alarmBeep = function(period, timeout) {
  beepInterval = setInterval(switchSound, period);
  setTimeout(endBeep, timeout); //stop blinking after 5 seconds
}

function switchSound() {
  if (state) {
    BUZZER.writeSync(1);
    state = true;
  } else {
    BUZZER.writeSync(0);
    state = false;
  }
}

function endBeep() {
  clearInterval(beepInterval);
  BUZZER.writeSync(0);
}

process.on('SIGINT', function () {
  endBeep();
  BUZZER.unexport();
  process.exit();
});
