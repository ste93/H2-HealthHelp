/**
* Basic module to interact with an hardware Led.
*/
var Gpio = require('onoff').Gpio; //include onoff to interact with the GPIO

var led;
var blinkInterval;
var state;

/**
* Initialize led control and GPIO.
* @param gpioNumber is the pin number in which the led is connected.
*/
module.exports.initializeLed = function(gpioNumber) {
   led = new Gpio(gpioNumber, 'out');
   state = false;
   console.log(" [Led] initialized on gpio ", gpioNumber);
 }

 /**
 * Function to turn on the led.
 * If already on this does nothing.
 */
 module.exports.turnOn = function turnOn() {
     led.writeSync(1);
     console.log(" [Led] on ");
 }

 /**
 * Function to turn off the led.
 * If already off this does nothing.
 */
 module.exports.turnOff = function turnOff() {
     led.writeSync(0);
     console.log(" [Led] off");
 }

 /**
 * Function to make the led blink.
 * @param period is the period of the blinking.
 * @param duration is the amount of millisecond in which led will blink.
 */
 module.exports.blink = function(period, duration) {
  blinkInterval = setInterval(switchLed, period);
  setTimeout(endBlink, duration);
  console.log(" [Led] blink for" , duration);
}

/**
* Function to alternate led state:
* If is on it turn it off and viceversa.
*/
function switchLed() {
   if (! state) {
     led.writeSync(1);
     state = true;
   } else {
     led.writeSync(0);
     state = false;
   }
 }

 /**
 * Function to stop blinking after a certain period.
 */
function endBlink() {
  clearInterval(blinkInterval);
  led.writeSync(0);
}

// clean exit when the process is terminated.
process.on('SIGINT', function () {
  clearInterval(blinkInterval);
  led.writeSync(0);
  led.unexport();
  process.exit();
});
