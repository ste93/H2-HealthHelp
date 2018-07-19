/**
* Function to manage patient-related file I/O.
*/
var fileManager = require('./file_manager');

var patientFileName = "datafile/patients/patients.json";

/**
* Return the index of the queried element on the given list.
* @param jsonList is the given list of patients (JSON format).
* @param patientId is the id of the patient which index is returned.
*/
function getIndex(jsonList, patientId) {
  return jsonList.map(function(element) {
      return(element.id);
  }).indexOf(patientId);
}

/**
* Return to the callback the whole list of patients data registered in the storage of the current control unit.
*/
module.exports.getPatientsList = function (callback) {
  fileManager.initialiseJsonFile(patientFileName);
  fileManager.readJsonFromFile(patientFileName, callback);
}

/**
* Return to the callback the a single patient data registered in the storage of the current control unit.
* @param patientId is the id of the patient to be found.
*/
module.exports.getPatient = function (patientId, callback) {
  fileManager.readJsonFromFile(patientFileName, function(error, jsonObject) {
    var index = getIndex(jsonObject, patientId);
    callback(error, jsonObject[index]);
  });
};

/**
* Add a new patient's data to the control unit's storage.
* @param patientId is the id of the patient to be added.
* @param patientName is the name of the patient to be added.
*/
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

/**
* Remove a single patient data from the control unit's storage.
* @param patientId is the id of the patient to be deleted.
*/
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
