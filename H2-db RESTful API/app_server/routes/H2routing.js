/** H2 ROUTING - RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** setting express */
var express = require('express');
var router = express.Router();

/** files .js that include the callback of request*/
var userAuthentication = require('../controller/userAuthentication');
var userOperations = require('../controller/UserOpertions');
var sensorDataOperation = require('../controller/sensorDataOperations');
var sensorTypeOperations = require('../controller/sensorTypeOperations');
var adviceOperations = require('../controller/adviceOperations');
var drugsOperations = require('../controller/drugsOperations');

/** Connect Mongo DB */
require('../../database');

/** GET home page. */
router.get('/', function(req, res, next) {});

/** POST request to sign up a new user in H2 application.*/
router.post('/registration', userAuthentication.register);

/** GET request to sign in a user already registered in H2 application */
router.get('/login', userAuthentication.login );

/** GET request to return all sensor types related to a specific patient */
 
router.get('/sensors', sensorTypeOperations.getSensorTypes);

/** PUT request to add a new sensor types related to a specific patient
 *  and create a related collection to save all sensor's values */
router.put('/sensors', sensorTypeOperations.putSensorType );

/** POST request to add a new value of a particular sensor related to a patient */
router.post('/sensors/values', sensorDataOperation.addValue);   

/** DELETE request to cancel all values of a particular sensor */
router.delete('/sensors/values', sensorDataOperation.deleteValue);

/** GET request to return all values of a particular sensor type related to a patient */
router.get('/sensors/values', sensorDataOperation.getValues);

/** POST request to add an advice related to a particular patient */
router.post('/advices', adviceOperations.addAdvice);

/** GET request to return all advices of a specific patient */
router.get('/advices', adviceOperations.getAdvices);

/** POST request to add a drug prescibed to a specific patient */
router.post('/drugs', drugsOperations.addDrugs);

/** GET request to return all drugs prescribed to a specific patient */
router.get('/drugs', drugsOperations.getAllDrugs);

/** GET request to return the patient's personal information */
router.get('/patients', userOperations.getPatient);

/** GET request to return the doctor's personal information */
router.get('/doctors', userOperations.getDoctor);

/** DELETE request to unregister a specific patient in the application */
router.delete('/patients', userOperations.deletePatient);

/** DELETE request to unregister a specific doctor in the application */
router.delete('/doctors', userOperations.deleteDoctor);

module.exports = router;