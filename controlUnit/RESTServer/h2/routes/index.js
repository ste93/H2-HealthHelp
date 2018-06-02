var express = require('express');
var router = express.Router();
var homepageController = require('../controllers/home');
var sensorsController = require('../controllers/sensors');
var patientsController = require('../controllers/patients');
var othersController = require('../controllers/others');


// specify what to do for every handled url:
router.get('/', homepageController.home);
router.get("/sensors", sensorsController.sensorsHome);

router.get("/sensors/:sensorID/", sensorsController.sensorDetail);
router.get("/sensors/:sensorID/details", sensorsController.sensorDetail);

router.get("/sensors/:sensorID/connect", sensorsController.sensorConnect);

router.get("/sensors/:sensorID/connectNew", sensorsController.sensorConnectNew);
router.post("/sensors/:sensorID/connectNew", sensorsController.addNewSensors);

router.get("/sensors/:sensorID/delete", sensorsController.delete);

router.get("/patients", patientsController.patientsHome);
router.get("/patients/new", patientsController.newPatientForm);
router.post("/patients/new", patientsController.addNew);
router.get("/patients/:patientID/", patientsController.details);
router.get("/patients/:patientID/details", patientsController.details);
router.get("/patients/:patientID/delete", patientsController.delete);

router.get("/about", othersController.about);

module.exports = router;
