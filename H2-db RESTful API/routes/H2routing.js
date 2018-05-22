/** H2 ROUTING - RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */
//var session = require('express-session');
var express = require('express');
var router = express.Router();

/** files .js that include the callback of request*/
var userAuthentication = require('./userAuthentication');
var sensorDataOperation = require('./sensorDataOperations');
var adviceOperations = require('./adviceOperations');
var drugsOperations = require('./drugsOperations');

/** Connect Mongo DB */
require('../database');

/** GET home page. */
router.get('/', function(req, res, next) {
});

/** User registration in H2 application
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST(missing parameters or wrong idCode)
 *              401 - UNAUTHORIZED (role is not valid)
 * 
 * @param role - "doctor" or "patient" label
 * @param idCode - personal identifier of italian national service 
 * @param password 
 * @param name
 * @param surname
 * @param cf
 * @param phone - array of user phones
 * @param mail 
 * 
 */
router.post('/registration', function(req, res, next){
    var role = req.param('role');
    var idCode = req.param('idCode');
    var password = req.param('password');
    var name = req.param('name');
    var surname = req.param('surname');
    var cf = req.param('cf');
    var phone = req.param('phone')
    var mail = req.param('mail')

    var phoneArray = phone.split(" ");

    var user = {
        "idCode": idCode,
        "password": password,
        "name" : name,
        "surname": surname,
        "cf" : cf,
        "phone": phoneArray,
        "mail": mail
    };
    
    userAuthentication.registation(role, user, res);
    
});

router.get('/login', function(req, res, next){
    var idCode = req.param('idCode');
    var role = req.param('role');
    var password = req.param('password');
    
    userAuthentication.login(idCode, role, password, res);
});

/** Get all sensor types related to a specific patient
 * 
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * 
 * @param idCode - patient identifier 
 * 
 */
router.get('/sensors', function(req,res, next){
    var idCode = req.param('idCode');

    sensorDataOperation.getSensorTypes(idCode,res);
});

/** Modify array of patient' sensor types 
 *  and create a related collection for values.
 * 
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param idCode - patient identifier
 * @param type - sensor type to add
 */
router.put('/sensors', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');

    sensorDataOperation.addSensorType(idCode, type, res);
});   

/** ADD request to add a value of a particular sensor
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param idCode - patient identifier
 * @param type - sensor type to add
 * @param message - message containing the value and other informations correlated
 */
router.post('/sensors/values', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');
    var message = req.param('message');

    sensorDataOperation.addValue(idCode, type, message, res);
});   

/** DELETE request to cancel all values of a particular sensor
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param idCode - patient identifier
 * @param type - sensor type to add
 */
router.delete('/sensors/values', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');

    sensorDataOperation.deleteAllValues(idCode, type, res);
});

/** GET request to return all values of a particular sensor type
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param idCode - patient identifier
 * @param type - sensor type to add
 */
router.get('/sensors/values', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');

    sensorDataOperation.getAllValuesOfSpecificSensor(idCode, type, res);
});

/** GET request to return all values of a particular sensor type
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param idCode - patient identifier
 * @param type - sensor type to add
 */
router.get('/sensors/values', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');

    sensorDataOperation.getAllValuesOfSpecificSensor(idCode, type, res);
});

/** POST request to post an advice in the DB
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param message - message containing the advice and other informations correlated
 */
router.post('/advices', function(req, res, next){
    var message = req.param('message');

    adviceOperations.addAdvice(message, res);
});

/** GET request to return all advices of a specific patient
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param idCode - patient identifier
 */
router.get('/advices', function(req, res, next){
    var idCode = req.param('idCode');

    adviceOperations.getAdvices(idCode, res);
});



/** POST request to post a drug prescibed for a specific patient
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param idCode - patient identifier
 * @param message - message containing the advice and other informations correlated
 */
router.post('/drugs', function(req, res, next){
    var idCode = req.param('idCode');
    var message = req.param('message');

    drugsOperations.addDrug(idCode, message, res);
});

/** GET request to return all drugs prescribed to a specific patient
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param idCode - patient identifier
 */
router.get('/drugs', function(req, res, next){
    var idCode = req.param('idCode');

    drugsOperations.getDrugs(idCode, res);
});


module.exports = router;