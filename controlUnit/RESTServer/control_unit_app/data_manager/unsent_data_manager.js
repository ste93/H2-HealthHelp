//file_manager library functions
var fileManager = require('./file_manager');

var dataFilename = "datafile/unsent/data.json";

module.exports.getDataList = function (callback) {
  console.log("Retrieving locally saved data");
  fileManager.initialiseJsonFile(dataFilename);
  fileManager.readJsonFromFile(dataFilename, callback);
}

module.exports.addData = function(json) {
  fileManager.initialiseJsonFile(dataFilename);
  console.log("Save data locally");
  console.log("data : ", JSON.stringify(json));
  fileManager.readJsonFromFile(dataFilename, function(error, jsonObject) {
      if (error) {
          console.log(error);
      }
      else {
          jsonObject.push(json);
          fileManager.writeJsonToFile(jsonObject, dataFilename, function(error) {
              if (error) {
                  console.log(error);
              }
          });
      }
  });
}

module.exports.clearList = function() {
  fileManager.clearFile(dataFilename);
}
