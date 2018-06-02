//file_manager library functions
var fileManager = require('./file_manager');

var sensorFileName = "datafile/sensors/sensors.json";

function getIndex(jsonList, sensorID) {
  return jsonList.map(function(element) {
      return(element.sensorId);
  }).indexOf(sensorID);
}

module.exports.getSensorsList = function (callback) {
  fileManager.initialiseJsonFile(sensorFileName);
  fileManager.readJsonFromFile(sensorFileName, callback);
}

module.exports.getSensor = function (sensorId, callback) {
  fileManager.readJsonFromFile(sensorFileName, function(error, jsonObject) {
    var index = getIndex(jsonObject, sensorId);
    callback(error, jsonObject[index]);
  });
};

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


module.exports.deleteSensor = function(sensorId) {
    fileManager.readJsonFromFile(sensorFileName, function(error, jsonObject) {
        if (error) {
            console.log(error);
        } else {
            /*var index = jsonObject.map(function(element) {
                return(element.sensorId);
            }).indexOf(sensorId);*/
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
