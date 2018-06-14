/**
* Basic module to interact with an hardware buzzer for sound output.
*/

var Gpio = require('onoff').Gpio; //include onoff module to interact with the GPIO

var buzzer;
var state;
var beepInterval;

/**
* Initialize buzzer control and GPIO.
* @param gpioNumber is the pin number in which the buzzer is connected.
*/
module.exports.initializeBuzzer = function(gpioNumber) {
   buzzer = new Gpio(gpioNumber, 'out');
   state = false;
   console.log(" [Buzzer] Initialized on gpio ", gpioNumber );
 }

 /**
 * Function to make the buzzer sound continuosly.
 * If already playing sound this does nothing.
 */
 module.exports.beep = function() {
     buzzer.writeSync(1);
     console.log(" [Buzzer] beep on ");
 }

 /**
 * Function to make the buzzer sound continuosly for a certain amount of time.
 * @param duration is the amount of millisecond in which buzzer emit sound.
 */
 module.exports.beepFor = function(duration) {
     buzzer.writeSync(1);
     state = true;
     setTimeout(buzzer.writeSync(0), duration);
     console.log(" [Buzzer] beep for ", duration);

 }

 /**
 * Function to stop the buzzer from emitting sound.
 * If already stopped this does nothing.
 */
 module.exports.stop = function stop () {
     buzzer.writeSync(0);
     console.log(" [Buzzer] beep off");
}

/**
* Function to make the buzzer sound alternatively for a certain amount of time.
* @param period is the duration of a single state ( SOUND - STOP ) before switching.
* @param duration is the amount of millisecond in which buzzer emit sound.
*/
module.exports.alarm = function(period, duration) {
  beepInterval = setInterval(switchSound, period);
  setTimeout(endAlarm, duration);
  console.log(" [Buzzer] alarm beep ");
}

/**
* Function to alternate buzzer state:
* If is playing sound it stop, if is stopped start playong sound.
*/
function switchSound() {
  if (state) {
    buzzer.writeSync(1);
    state = true;
  } else {
    buzzer.writeSync(0);
    state = false;
  }
}

/**
* Function to stop alarm after a certain period.
*/
function endAlarm() {
  clearInterval(beepInterval);
  buzzer.writeSync(0);
}

// clean exit when the process is terminated.
process.on('SIGINT', function () {
  endBeep();
  buzzer.unexport();
  process.exit();
});
