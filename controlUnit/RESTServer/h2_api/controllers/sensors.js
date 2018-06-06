/**
* Controller class to handle sensor-related operation.
*/

var jsonUtilities = require('../jsonUtilities');
var sensorFileManager = require('../../control_unit_app/data_manager/saved_sensor_manager');
var analyser = require('../../control_unit_app/analyser/analyser');

/**
 * Function that return the entire list of previuosly connected sensors (AKA configured).
 * Read the list from file and then return to the requester in Json format.
 * @param req Is the HTTP Request. Not used by this function.
 * @param res Is the HTTP Response, returned to the requesting host with the result.
 */
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

/**
 * Function that return the details of a specific sensor.
 * Read the sensors' details from file and then return to the requester in Json format.
 * @param req Is the HTTP Request. Holds the ID of the sensors to be searched.
 * @param res Is the HTTP Response, returned to the requesting host with the result.
 */
module.exports.getSensorDetails = function (req, res) {

  console.log("Get sensor " + req.params.sensorID + " details");

  sensorFileManager.getSensor(req.params.sensorID, function(error, jsonObj) {
    if (error) {
      jsonUtilities.sendJsonResponse(res, 400, error);
    } else {
      jsonUtilities.sendJsonResponse(res, 200, jsonObj);
    }
  });
};

/**
 * Function that add a sensor to the list and save his details.
 * Read the various parameter of the sensor from request body and then save this to the file.
 * @param req Is the HTTP Request. Holds in his body a json with all the informations of the sensor to be saved.
 * @param res Is the HTTP Response, returned to the requesting host with the result code. The body can be null because no extra informations are returned.
 */
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

/**
 * Function that remove a sensor from the list (if present).
 * Read the ID of the sensor from request's parameters and then remove it from the file.
 * @param req Is the HTTP Request. Holds the ID of the sensor to be removed.
 * @param res Is the HTTP Response, returned to the requesting host with the result code. The body can be null because no extra informations are returned.
 */
module.exports.deleteSensor = function (req, res) {

  console.log("Deleting " + req.params.sensorID + " sensor");

  sensorFileManager.deleteSensor(req.params.sensorID);
  jsonUtilities.sendJsonResponse(res, 204, null);
};

/**
 * Function to add data from a sensors.
 * Read the ID of the sensor from request parameters and then ....
 * @todo use data for something. ( save them in a list and send to the analyser).
 * @param req Is the HTTP Request. Holds the ID of the sensors that measure the data.
 * @param res Is the HTTP Response, returned to the requesting host with the result code. The body can be null because no extra informations are returned.
 */
module.exports.addData = function (req,res) {
  var sensorID = req.params.sensorID;
  console.log("Received data from " , req.params.sensorID);
  if(req.body){
    console.log("BODY: " , req.body);
    analyser.analyseData(req.body);
  }
  //analyser.analyseData(req.body);
  jsonUtilities.sendJsonResponse(res, 200, null);
};
