

var jsonUtilities = require('../jsonUtilities');
var patientFileManager = require('../../control_unit_app/data_manager/patient_manager');
var patList = [{"id" : "75", "name" : "pat1"}, {"id" : "34", "name" : "pat2"}, {"id" : "42", "name" : "pat3"}];


module.exports.getPatientsList = function(req,res) {
  console.log("GET PATIENTS LIST")

  patientFileManager.getPatientsList(function(error, patientList) {
    if (error) {
      jsonUtilities.sendJsonResponse(res, 400, error);
    } else {
      jsonUtilities.sendJsonResponse(res, 200, patientList);
    }
  });

}

module.exports.getPatientDetails = function(req,res) {
  console.log("Get patient " + req.params.patientID + " details");

  patientFileManager.getPatient(req.params.patientID, function(error, jsonObj) {
    if (error) {
      jsonUtilities.sendJsonResponse(res, 400, error);
    } else {
      jsonUtilities.sendJsonResponse(res, 200, jsonObj);
      console.log("retrieved patient : ", jsonObj )
    }
  });
};


module.exports.createPatient = function (req, res) {
  console.log("New patient Creation : ");
  console.log(" --- id : " + req.body.id);
  console.log(" --- name : " + req.body.name);

  patientFileManager.addPatient(req.body.id, req.body.name);
  jsonUtilities.sendJsonResponse(res, 201, null);
};


module.exports.deletePatient = function(req,res) {
  console.log("Deleting " + req.params.sensorID + " patient");

  patientFileManager.deletePatient(req.params.sensorID);

  jsonUtilities.sendJsonResponse(res, 204, null);
}
