/**
* Function to manage unsent data file I/O to grant durability of data even if connection is not available.
*/
var fileManager = require('./file_manager');

var dataFilename = "datafile/unsent/data.json";

/**
* Return to the callback the whole list of unsent data registered in the storage of the current control unit.
*/
module.exports.getDataList = function (callback) {
  console.log("Retrieving locally saved data");
  fileManager.initialiseJsonFile(dataFilename);
  fileManager.readJsonFromFile(dataFilename, callback);
}

/**
* Add an unsent message to the storage of the control unit.
* @param message is the JSON-format message to be saved.
*/
module.exports.addData = function(message) {
  fileManager.initialiseJsonFile(dataFilename);
  console.log("Save data locally");
  console.log("data : ", JSON.stringify(message));
  fileManager.readJsonFromFile(dataFilename, function(error, jsonObject) {
      if (error) {
          console.log(error);
      }
      else {
          jsonObject.push(message);
          fileManager.writeJsonToFile(jsonObject, dataFilename, function(error) {
              if (error) {
                  console.log(error);
              }
          });
      }
  });
}

/**
* Empty the unsent message list.
*/
module.exports.clearList = function() {
  fileManager.clearFile(dataFilename);
}
