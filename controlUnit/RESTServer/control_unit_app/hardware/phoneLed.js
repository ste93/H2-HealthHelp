
var Gpio = require('onoff').Gpio; //include onoff to interact with the GPIO

var LED;
var blinkInterval;
var state;

module.exports.initializeLed = function(gpioNumber) {
  //use GPIO with the specified number, and specify that it is output
   LED = new Gpio(gpioNumber, 'out');
   state = false;
   console.log(" [Led] initialized on gpio", gpioNumber);
 }

 module.exports.turnOn = function turnOn() {
     LED.writeSync(1);
     console.log(" [Led] on ");
 }

 module.exports.turnOff = function turnOff() {
     LED.writeSync(0);
     console.log(" [Led] off");
 }

function switchLED() {
   if (! state) { //check the pin state, if the state is 0 (or off)
     LED.writeSync(1); //set pin state to 1 (turn LED on)
     state = true;
   } else {
     LED.writeSync(0); //set pin state to 0 (turn LED off)
     state = false;
   }
 }

 module.exports.blink = function(period, timeout) {
  blinkInterval = setInterval(switchLED, period);
  setTimeout(endBlink, timeout); 
  console.log(" [Led] blink for" , timeout);
}

function endBlink() { //function to stop blinking
  clearInterval(blinkInterval); // Stop blink intervals
  LED.writeSync(0); // Turn LED off
}

process.on('SIGINT', function () {
  clearInterval(blinkInterval);
  LED.writeSync(0);
  LED.unexport();
  process.exit();
});

// state = LED.readSync()
//LED.unexport(); // Unexport GPIO to free resources
