/**
* Basic module to interact with an hardware 16x2 LCD.
* By default this hold the project name in the first row and write messages on the second one.
*/

var LCD = require('lcd');

var lcd;
var initialized = false;

/**
* Initialize lcd control and GPIO.
*/
module.exports.initializeLcd = function() {

  if (! initialized){
    lcd = new LCD({rs: 7, e: 8, data: [17, 18, 27, 22], cols: 16, rows: 2});

    lcd.on('ready', function () {
      initialized = true;
      console.log("[LCD] Initialization completed");
      lcd.clear(function(err){
        if (err) {
          console.log("error : " , err);
        }
      });

      lcd.print('H2 - Health Help', function (err, str) {
        if (err) {
          console.log("error : " , err);
        };
      });

    });
  }
}

/**
 * Write a message on the video screen.
 * This implementation write only on line 2 because keep the default project name on line 1.
 *
 * @param message - the message to be printed.
 */
module.exports.write = function(message) {

  console.log("printing message !");
  // write on line 2.
  lcd.setCursor(0, 1);
  lcd.print(message, function (err, str) {
    if (err) {
      console.log("error : " , err);
    };
  });
}

/**
 * Clean the message from screen and return to default state ( Project name on first row, no message on the second )
 */
module.exports.reset = function()  {
  console.log("resetting screen !");
  lcd.clear(function (err) {
    if (err) {
      console.log("error : " , err);
    }
  });

  lcd.print('H2 - Health Help', function (err, str) {
      if (err) {
        console.log("error : " , err);
      };
  });
}

// clean exit when the process is terminated.
process.on('SIGINT', function () {
  lcd.close();
  process.exit();
});
