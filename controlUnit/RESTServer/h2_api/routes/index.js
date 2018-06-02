var express = require('express');
var router = express.Router();
var sensorsController = require('../controllers/sensors');
var patientsController = require('../controllers/patients');

// sensors

router.get('/sensors', sensorsController.getSensorsList);
router.post('/sensors', sensorsController.createSensors);
router.get('/sensors/:sensorsID', sensorsController.getSensorDetails);

//router.put('/sensors/:sensorsID', sensorsController.updateSensor);
router.delete('/sensors/:sensorID', sensorsController.deleteSensor);

router.post('/sensors/:sensorID/data', sensorsController.addData);

router.get('/patients', patientsController.getPatientsList);
router.post('/patients', patientsController.createPatient);
router.get('/patients/:patientID/', patientsController.getPatientDetails);

router.delete('/patients/:patientID/', patientsController.deletePatient);


module.exports = router;
