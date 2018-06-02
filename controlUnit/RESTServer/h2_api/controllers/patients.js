

var jsonUtilities = require('../jsonUtilities');
var patList = [{"id" : "75", "name" : "pat1"}, {"id" : "34", "name" : "pat2"}, {"id" : "42", "name" : "pat3"}];


module.exports.getPatientsList = function(req,res) {
  console.log("GET PATIENTS LIST")

  //console.log("Patient List found : " + JSON.stringify(patList));
  jsonUtilities.sendJsonResponse(res, 200, patList);

}

module.exports.getPatientDetails = function(req,res) {
  console.log("GET PATIENT DETAILS")
/*sens
  .find({'id' : req.params.sensorsID})
  .exec(function(err, sensor) {
    sendJsonResponse(res, 200, sensor);
  });*/
  jsonUtilities.sendJsonResponse(res, 200, patList);
}


module.exports.createPatient = function (req, res) {
  console.log("POST PATIENT")
  /*sens.create({
    name: req.body.name,
    id: req.body.id,
    patient: req.body.patient,
    system: req.body.system,
    value: req.body.value,
    unit: req.body.unit,
  } , function(err, location) {
    if (err) {
      sendJsonResponse(res, 400, err);
    } else {
      sendJsonResponse(res, 201, location);
    }
  });*/
  var json = {"OK" : "OK"};
  jsonUtilities.sendJsonResponse(res, 201, json );
};


module.exports.deletePatient = function(req,res) {
  console.log("DELETE PATIENT");
    jsonUtilities.sendJsonResponse(res, 204, null);
}
