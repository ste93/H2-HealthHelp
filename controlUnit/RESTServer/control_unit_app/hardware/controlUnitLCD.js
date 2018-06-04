var LCD = require('lcd'),

var lcd;

module.exports.initializeLcd = function() {

  lcd = new LCD({rs: 7, e: 8, data: [17, 18, 27, 22], cols: 16, rows: 2});

  lcd.on('ready', function () {

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

/**
 * Write a message on the video screen.
 * This implementation write only on line 2 because keep the default project name on line 1.
 *
 * @param message - the message to be printed.
 */
modules.exports.write = function(message) {

  console.log("printing message !");
  // write on line 2.
  lcd.setCursor(0, 1);
  lcd.print(message, function (err, str) {
    if (err) {
      console.log("error : " , err);
    });
  };
}

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

// If ctrl+c is hit, free resources and exit.
process.on('SIGINT', function () {
  lcd.close();
  process.exit();
});
