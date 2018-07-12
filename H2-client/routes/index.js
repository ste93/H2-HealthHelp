var express = require('express');
var router = express.Router();
var session = require('client-sessions');

var userAuthentication = require('./userAuthentication');
var patient = require('./patient');
var doctor = require('./doctor');

var pubSubAdvice = require('./pubSubAdvice');
var pubSubHistory = require('./pubSubHistory');
var pubSubInfo = require('./pubSubInfo');
var pubSubDrug = require('./pubSubDrug');

var userId;
var patientId;
var type;

/** Login page and Home page */

router.get('/', userAuthentication.getHome);

router.post('/', userAuthentication.login);


/** Patient side */

router.get("/patient/:patientID", patient.getHome);

router.post("/patient/:patientID", patient.makeRequest);

router.get("/patient/:patientID/history", patient.getHistory);

router.get("/patient/:patientID/advice", patient.getAdvices);

router.get("/patient/:patientID/drug", patient.getDrugs);

router.get("/patient/:patientID/info", patient.getInfo);

/** Doctor Side */

router.get("/doctor/:doctorID", doctor.getHome);

router.post("/doctor/:doctorID", doctor.makeRequest);

router.get("/doctor/:doctorID/history", doctor.getPatientHistory);

router.get("/doctor/:doctorID/info", doctor.getInfo);


module.exports = router;
