
var Gpio = require('onoff').Gpio; //include onoff to interact with the GPIO

var BUZZER;
var state;
var beepInterval;

module.exports.initializeBuzzer = function(gpioNumber) {
  //use GPIO with the specified number, and specify that it is output
   BUZZER = new Gpio(gpioNumber, 'out');
   state = false;
   console.log(" [Buzzer] Initialized on gpio", gpioNumber );
 }

 module.exports.beep = function() {
     BUZZER.writeSync(1);
     console.log(" [Buzzer] beep on ");
 }

 module.exports.beepFor = function(period) {
     BUZZER.writeSync(1);
     state = true;
     setTimeout(BUZZER.writeSync(0), timeout);
     console.log(" [Buzzer] beep for ", period);

 }

 module.exports.stop = function stop () {
     BUZZER.writeSync(0);  
     console.log(" [Buzzer] beep off");
}

module.exports.alarmBeep = function(period, timeout) {
// potrebbe non andare, devo toglierlo
  beepInterval = setInterval(switchSound, period);
  setTimeout(endBeep, timeout); //stop blinking after 5 seconds
  console.log(" [Buzzer] alarm beep ");
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
