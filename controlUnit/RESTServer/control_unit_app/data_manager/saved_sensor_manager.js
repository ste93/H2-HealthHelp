/**
* Function to manage sensor-related file I/O.
*/
var fileManager = require('./file_manager');

var sensorFileName = "datafile/sensors/sensors.json";

/**
* Return the index of the queried element on the given list.
* @param jsonList is the given list of sensors (JSON format).
* @param sensorID is the id of the sensor which index is returned.
*/
function getIndex(jsonList, sensorID) {
  return jsonList.map(function(element) {
      return(element.sensorId);
  }).indexOf(sensorID);
}

/**
* Return to the callback the whole list of sensors data registered in the storage of the current control unit.
*/
module.exports.getSensorsList = function (callback) {
  fileManager.initialiseJsonFile(sensorFileName);
  fileManager.readJsonFromFile(sensorFileName, callback);
}

/**
* Return to the callback the a single sensor data registered in the storage of the current control unit.
* @param sensorId is the id of the sensor to be found.
*/
module.exports.getSensor = function (sensorId, callback) {
  fileManager.readJsonFromFile(sensorFileName, function(error, jsonObject) {
    var index = getIndex(jsonObject, sensorId);
    callback(error, jsonObject[index]);
  });
};

/**
* Add a new sensor's data to the control unit's storage.
* @param sensorId is the id of the sensor to be added.
* @param sensorName is the name of the sensor to be added.
* @param patientId is the id of the patient that use the sensor to be added.
* @param dataType is the type of data measured by the sensor to be added.
* @param unit is the unity of measurement of the sensor to be added.
*/
module.exports.addSensor = function(sensorId, sensorName, patientId, dataType, unit) {
  fileManager.initialiseJsonFile(sensorFileName);

  var jsonToSerialise = {
    'sensorId' : sensorId,
    'value' : {
        'name' : sensorName,
        'patientID' : patientId,
        'dataType' : dataType,
        'unit' : unit
    }
  }
  fileManager.readJsonFromFile(sensorFileName, function(error, jsonObject) {
    if (error) {
      console.log(error);
    }
    else {
      jsonObject.push(jsonToSerialise);
      fileManager.writeJsonToFile(jsonObject, sensorFileName, function(error) {
        if (error) {
          console.log(error);
        }
      });
    }
  });
}

/**
* Remove a single sensor's data from the control unit's storage.
* @param sensorId is the id of the sensor to be deleted.
*/
module.exports.deleteSensor = function(sensorId) {
  fileManager.readJsonFromFile(sensorFileName, function(error, jsonObject) {
    if (error) {
      console.log(error);
    } else {
      var index = getIndex(jsonObject, sensorId);
      jsonObject.splice(index, 1);
      fileManager.writeJsonToFile(jsonObject, sensorFileName, function(error) {
        if (error) {
          console.log(error);
        }
      });
    }
  });
}
