//file_manager library functions
var fileManager = require('./file_manager');

var patientFileName = "datafile/patients/patients.json";

function getIndex(jsonList, patientId) {
  return jsonList.map(function(element) {
      return(element.id);
  }).indexOf(patientId);
}

module.exports.getPatientsList = function (callback) {
  fileManager.initialiseJsonFile(patientFileName);
  fileManager.readJsonFromFile(patientFileName, callback);
}

module.exports.getPatient = function (patientId, callback) {
  fileManager.readJsonFromFile(patientFileName, function(error, jsonObject) {
    var index = getIndex(jsonObject, patientId);
    callback(error, jsonObject[index]);
  });
};

module.exports.addPatient = function(patientId, patientName) {
    fileManager.initialiseJsonFile(patientFileName);

    var jsonToSerialise = {
        'id' : patientId,
        'name' : patientName
    }

    fileManager.readJsonFromFile(patientFileName, function(error, jsonObject) {
        if (error) {
            console.log(error);
        }
        else {
            jsonObject.push(jsonToSerialise);
            fileManager.writeJsonToFile(jsonObject, patientFileName, function(error) {
                if (error) {
                    console.log(error);
                }
            });
        }
    });
}


module.exports.deletePatient = function(patientId) {
    fileManager.readJsonFromFile(patientFileName, function(error, jsonObject) {
        if (error) {
            console.log(error);
        } else {
            var index = getIndex(jsonObject, patientId);
            jsonObject.splice(index, 1);
            fileManager.writeJsonToFile(jsonObject, patientFileName, function(error) {
                if (error) {
                    console.log(error);
                }
            });
        }
    });
}
