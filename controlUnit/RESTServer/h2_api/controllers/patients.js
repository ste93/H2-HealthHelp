/**
* Controller class to handle patient-related operation.
*/

var jsonUtilities = require('../jsonUtilities');
var patientFileManager = require('../../control_unit_app/data_manager/patient_manager');
var patList = [{"id" : "75", "name" : "pat1"}, {"id" : "34", "name" : "pat2"}, {"id" : "42", "name" : "pat3"}];

/**
 * Function that return the entire list of previuosly connected patient (AKA users).
 * Read the list from file and then return to the requester in Json format.
 * @param req Is the HTTP Request. Not used by this function.
 * @param res Is the HTTP Response, returned to the requesting host with the result.
 */
module.exports.getPatientsList = function(req,res) {
  console.log("Get patients list")

  patientFileManager.getPatientsList(function(error, patientList) {
    if (error) {
      jsonUtilities.sendJsonResponse(res, 400, error);
    } else {
      jsonUtilities.sendJsonResponse(res, 200, patientList);
    }
  });
}


/**
 * Function that return the details of a specific patient.
 * Read the patients' details from file and then return to the requester in Json format.
 * @param req Is the HTTP Request. Holds the ID of the patients to be searched.
 * @param res Is the HTTP Response, returned to the requesting host with the result.
 */
module.exports.getPatientDetails = function(req,res) {
  console.log("Get patient " + req.params.patientID + " details");

  patientFileManager.getPatient(req.params.patientID, function(error, jsonObj) {
    if (error) {
      jsonUtilities.sendJsonResponse(res, 400, error);
    } else {
      jsonUtilities.sendJsonResponse(res, 200, jsonObj);
      console.log("Retrieved patient : ", jsonObj )
    }
  });
};

/**
 * Function that add a patient to the list and save his details.
 * Read the various parameter of the patient from request body and then save this to the file.
 * @param req Is the HTTP Request. Holds in his body a json with all the informations of the patient to be saved.
 * @param res Is the HTTP Response, returned to the requesting host with the result code. The body can be null because no extra informations are returned.
 */
module.exports.createPatient = function (req, res) {
  console.log("New patient Creation : ");
  console.log(" --- id : " + req.body.id);
  console.log(" --- name : " + req.body.name);

  patientFileManager.addPatient(req.body.id, req.body.name);
  jsonUtilities.sendJsonResponse(res, 201, null);
};


/**
 * Function that remove a patient from the list (if present).
 * Read the ID of the patient from request's parameters and then remove it from the file.
 * @param req Is the HTTP Request. Holds the ID of the patient to be removed.
 * @param res Is the HTTP Response, returned to the requesting host with the result code. The body can be null because no extra informations are returned.
 */
module.exports.deletePatient = function(req,res) {
  console.log("Deleting " + req.params.sensorID + " patient");

  patientFileManager.deletePatient(req.params.sensorID);
  jsonUtilities.sendJsonResponse(res, 204, null);
}
