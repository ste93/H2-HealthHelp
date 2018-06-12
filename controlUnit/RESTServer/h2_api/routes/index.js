/**
* Router class to handle different request to the variuos API.
* Every istruction handle a different HTTP Method for a specific resources, defining the REST API for H2.
*/

var express = require('express');
var router = express.Router();
var sensorsController = require('../controllers/sensors');
var patientsController = require('../controllers/patients');

//////// Sensors-related Request ///////////////////////////////////////////////
// Request for the whole sensors list.
router.get('/sensors', sensorsController.getSensorsList);

// Request for adding a sensor to the list of connected ones.
router.post('/sensors', sensorsController.createSensors);

// Request for details for a specific sensor.
router.get('/sensors/:sensorID', sensorsController.getSensorDetails);

// Request to delete a specific sensor
router.delete('/sensors/:sensorID', sensorsController.deleteSensor);

// Request to add measured values from a specific sensor.
router.post('/sensors/:sensorID/data', sensorsController.addData);
////////////////////////////////////////////////////////////////////////////////

//////// Patients-related Request //////////////////////////////////////////////
// Request for the whole patients list.
router.get('/patients', patientsController.getPatientsList);

// Request for adding a patient to the list of users.
router.post('/patients', patientsController.createPatient);

// Request for details for a specific patient.
router.get('/patients/:patientID/', patientsController.getPatientDetails);

// Request to delete a specific patient.
router.delete('/patients/:patientID/', patientsController.deletePatient);
////////////////////////////////////////////////////////////////////////////////

module.exports = router;
