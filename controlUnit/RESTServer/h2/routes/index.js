/**
* Router class to handle different request to the variuos web application's pages and allow the users to surf the site.
* Every istruction handle a different HTTP Method for a specific url.
* Every path not specified here doesn't exist on H2 Web site and requests fails.
*/

var express = require('express');
var router = express.Router();
var homepageController = require('../controllers/home');
var sensorsController = require('../controllers/sensors');
var patientsController = require('../controllers/patients');
var othersController = require('../controllers/others');

// Request for Control unit Home Page
router.get('/', homepageController.home);

//////// Sensors-related Request ///////////////////////////////////////////////
// Request for the sensors management main page.
router.get("/sensors", sensorsController.sensorsHome);

// Request for a specific sensor page.
router.get("/sensors/:sensorID/", sensorsController.sensorDetail);
router.get("/sensors/:sensorID/details", sensorsController.sensorDetail);

// Request to connect a previously used sensor to the system
// TODO: QUESTO NON LO USIAMO MAI; FORSE SI PUç TOGLIERE
router.get("/sensors/:sensorID/connect", sensorsController.sensorConnect);

// Request of the form page to insert data for a new sensor connected to the system.
router.get("/sensors/:sensorID/connectNew", sensorsController.sensorConnectNew);

// Request to handle new sensor creation procedure ( Invocked by defult when the form is submitted ).
router.post("/sensors/:sensorID/connectNew", sensorsController.addNewSensors);

// Request to delete a previously connected sensor.
router.get("/sensors/:sensorID/delete", sensorsController.delete);
////////////////////////////////////////////////////////////////////////////////

//////// Patients-related Request //////////////////////////////////////////////
// Request for the patients management main page.
router.get("/patients", patientsController.patientsHome);

// Request for a specific patient page.
router.get("/patients/:patientID/", patientsController.details);
router.get("/patients/:patientID/details", patientsController.details);

// Request of the form page to insert data for a new patient connected to the system.
router.get("/patients/new", patientsController.newPatientForm);

// Request to handle new patient creation procedure ( Invocked by defult when the form is submitted ).
router.post("/patients/new", patientsController.addNew);

// Request to delete a previously connected sensor.
router.get("/patients/:patientID/delete", patientsController.delete);
////////////////////////////////////////////////////////////////////////////////

//////// About Pages  //////////////////////////////////////////////////////////
router.get("/about", othersController.about);
////////////////////////////////////////////////////////////////////////////////

module.exports = router;
