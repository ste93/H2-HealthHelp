var jsonUtilities = require('../jsonUtilities');
var sensorFileManager = require('../../control_unit_app/data_manager/saved_sensor_manager');

module.exports.getSensorsList = function (req, res) {

  console.log("Get sensors list");

  sensorFileManager.getSensorsList(function(error, jsonObj) {
    if (error) {
      jsonUtilities.sendJsonResponse(res, 400, error);
    } else {
      jsonUtilities.sendJsonResponse(res, 200, jsonObj);
    }
  });
};

module.exports.createSensors = function (req, res) {

  console.log("New Sensor Creation : ");
  console.log(" --- id : " + req.body.id);
  console.log(" --- name : " + req.body.name);
  console.log(" --- patient : " + req.body.patient);
  console.log(" --- datatype : " + req.body.dataType);
  console.log(" --- unit : " + req.body.unit);

  sensorFileManager.addSensor(req.body.id, req.body.name, req.body.patient, req.body.dataType, req.body.unit);
  jsonUtilities.sendJsonResponse(res, 201, null);
};

module.exports.getSensorDetails = function (req, res) {

  console.log("Get sensor " + req.params.sensorID + "details");

  sensorFileManager.getSensor(req.params.sensorID, function(error, jsonObj) {
    if (error) {
      jsonUtilities.sendJsonResponse(res, 400, error);
    } else {
      jsonUtilities.sendJsonResponse(res, 200, jsonObj);
    }
  });
};


module.exports.deleteSensor = function (req, res) {

  console.log("Deleting " + req.params.sensorID + " sensor");

  sensorFileManager.deleteSensor(req.params.sensorID);

  jsonUtilities.sendJsonResponse(res, 204, null);

};


module.exports.addData = function (req,res) {
  var sensorID = req.params.sensorID;
  if (sensorID) {
    console.log("received data");
    console.log("BODY: " + req.body);
    console.log("BODY: " + JSON.stringify(req.body, null, 2))
    //sendJsonResponse(res, 200, null);
  }

};
