
var mongoose = require('mongoose');
var sens = mongoose.model('Sensor');

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

  console.log("Get sensor details");

  sensorFileManager.getSensor(req.params.sensorsID, function(error, jsonObj) {
    if (error) {
      jsonUtilities.sendJsonResponse(res, 400, error);
    } else {
      jsonUtilities.sendJsonResponse(res, 200, jsonObj);
    }
  });


    /*sens
      .find({'id' : req.params.sensorsID})
      .exec(function(err, sensor) {
        jsonUtilities.sendJsonResponse(res, 200, sensor);
      });
      */
};


/*module.exports.updateSensor = function (req, res) {
  jsonUtilities.sendJsonResponse(res, 200, null);
  /*if (! req.params.sensorsID) {
    sendJsonResponse(res, 404, {
      "message": "Not found, sensorID is required"
    });
    return;
  }

  var updatedSensorData = {
    id : req.params.sensorsID,
    name : req.formData.name,
    patient : req.formData.patient,
    system : req.formData.system,
    value : req.formData.value,
    unit : req.formData.unit
  }

  console.log("updatedData : " + JSON.stringify(updatedSensorData, null, 2));
  */

  /*sens
    .find({'id' : req.params.sensorsID})
    //.select(-id)
    .exec(
      function(err, sensor) {
        if (!sensor) {
          sendJsonResponse(res, 404, {
            "message": "sensor not found"
          });
          return;
        } else if (err) {
          sendJsonResponse(res, 400, err);
          return;
        }
        //sensor.id = req.params.sensorsID;
        /*sensor.name = req.body.name;
        sensor.patient = req.body.patient;
        sensor.system = req.body.system;
        sensor.value = req.body.value;
        sensor.unit = req.body.unit;*/
        /*var s = new sens(sensor)
        s.save(function(err, s) {
          if (err) {
            sendJsonResponse(res, 404, err);
          } else {
            sendJsonResponse(res, 200, s);
          }
        });
      }
    );*/

module.exports.deleteSensor = function (req, res) {
  var sensorID = req.params.sensorID;

  if (sensorID) {
    sens
      .deleteOne({id : sensorID})
      .exec(
        function(err, location) {
          if (err) {
            console.log("error: " + err);
            jsonUtilities.sendJsonResponse(res, 404, err);
            return;
        }
        jsonUtilities.sendJsonResponse(res, 204, null);
        }
      );
    } else {
      jsonUtilities.sendJsonResponse(res, 404, {
        "message": "No sensorID found"
      });
    }
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
