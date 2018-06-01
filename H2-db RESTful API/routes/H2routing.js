/** H2 ROUTING - RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** setting express */
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
router.get('/', function(req, res, next) {});

/** POST request to sign up a new user in H2 application.
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST(missing parameters or wrong idCode)
 *         401 - UNAUTHORIZED (role is not valid)
 * 
 * @param {String} role - user's role: "doctor" or "patient" label
 * @param {String} idCode - user identifier
 * @param {String} password - user's password
 * @param {String} name - user's name
 * @param {String} surname - user's surname
 * @param {String} cf - user's CF
 * @param {Array(String)} phone - array of user's phone numbers
 * @param {String} mail - user's mail
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

/** GET request to sign in a user already registered in H2 application
 * 
 * @throws 200 - OK
 *         400 - BAD REQUEST(missing or wrong parameters)
 *         401 - UNAUTHORIZED (role is not valid)
 *
 * @param {String} role - user's role: "doctor" or "patient" label
 * @param {String} idCode - user identifier
 * @param {String} password - user's password
 * 
 */
router.get('/login', function(req, res, next){
    var idCode = req.param('idCode');
    var role = req.param('role');
    var password = req.param('password');
    userAuthentication.login(idCode, role, password, res);
});

/** GET request to return all sensor types related to a specific patient
 * 
 * @throws 500 - Internal Server Error
 * 
 * @returns a JSON with a list of sensors related to a specific patient
 * 
 * @param {String} idCode - patient identifier 
 * 
 */
router.get('/sensors', function(req,res, next){
    var idCode = req.param('idCode');
    
    sensorDataOperation.getSensorTypes(idCode,res);
});

/** PUT request to add a new sensor types related to a specific patient
 *  and create a related collection to save all sensor's values
 * 
 * @throws 200 - OK
 *         500 - Internal Server Error
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - sensor type to add
 */
router.put('/sensors', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');

    sensorDataOperation.addSensorType(idCode, type, res);
});

/** POST request to add a new value of a particular sensor related to a patient
 * 
 * @throws 200 - OK
 *         500 - Internal Server Error
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - value's sensor type
 * @param {String} message - message containing the value and other informations correlated to it
 */
router.post('/sensors/values', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');
    var message = req.param('message');

    console.log(message);
   
    sensorDataOperation.addValue(idCode, type, message, res);
});   

/** DELETE request to cancel all values of a particular sensor
 * 
 * @throws 200 - OK
 *         404 - sensor type not found
 *         500 - Internal Server Error
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - sensor type of values to cancel
 * @param {String} start - the date from which delete patien's data of a specific sensor (it's optional)
 * @param {String} end - the date up to which delete patien's data of a specific sensor (it's optional)
 */
router.delete('/sensors/values', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');
    var start = req.param('start');
    var end = req.param('end');
    
    if(start == undefined){
        sensorDataOperation.deleteAllValues(idCode, type, res);
    }else{
        sensorDataOperation.deleteAllValuesOnRange(idCode, type, start, end, res);
    }
    
});

/** GET request to return all values of a particular sensor type related to a patient
 * 
 * @throws 200 - OK
 *         
 * @returns an array of all sensor's values related to the patient
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - sensor type of values to return
 * @param {String} start - the date from which get patien's data of a specific sensor (it's optional)
 * @param {String} end - the date up to which get patien's data of a specific sensor (it's optional)
 */
router.get('/sensors/values', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');
    var start = req.param('start');
    var end = req.param('end');
    
    if(start == undefined){
        sensorDataOperation.getAllValuesOfSpecificSensor(idCode, type, res);
    }else{
        sensorDataOperation.getAllValuesOnRange(idCode, type, start, end, res);
    }
    
   
});

/** POST request to add an advice related to a particular patient
 * 
 * @throws 200 - OK
 *         500 - Internal Server Error
 * 
 * @param {String} message - message containing the advice and other informations correlated to it
 */
router.post('/advices', function(req, res, next){
    var message = req.param('message');
   
    adviceOperations.addAdvice(message, res);
});

/** GET request to return all advices of a specific patient
 * 
 * @throws 500 - Internal Server Error
 *         
 * @returns a JSON with a list of all advices related to the patient
 * 
 * @param {String} idCode - patient identifier
 * @param {String} start - the date from which get patien's advices (it's optional)
 * @param {String} end - the date up to which get patien's advices (it's optional)
 */
router.get('/advices', function(req, res, next){
    var idCode = req.param('idCode');
    var start = req.param('start');
    var end = req.param('end');
    
    if(start == undefined){
        adviceOperations.getAdvices(idCode, res);
    }else{
        adviceOperations.getAdvicesOnRange(idCode, start, end, res);
    }
});

/** POST request to add a drug prescibed to a specific patient
 * 
 * @throws 200 - OK
 *         500 - Internal Server Error
 * 
 * @param {String} idCode - patient identifier
 * @param {String} message - message containing the drug and other informations correlated to it
 */
router.post('/drugs', function(req, res, next){
    var idCode = req.param('idCode');
    var message = req.param('message');

    drugsOperations.addDrug(idCode, message, res);
});

/** GET request to return all drugs prescribed to a specific patient
 * 
 * @throws 500 - Internal Server Error
 *         
 * @returns an array of all drugs related to the patient
 * 
 * @param {String} idCode - patient identifier
 * @param {String} start - the date from which get patien's drugs (it's optional)
 * @param {String} end - the date up to which get patien's drugs (it's optional)
 */
router.get('/drugs', function(req, res, next){
    var idCode = req.param('idCode');
    var start = req.param('start');
    var end = req.param('end');
    
    if(start == undefined){
        drugsOperations.getDrugs(idCode, res);
    }else{
        drugsOperations.getDrugsOnRange(idCode, start, end, res);
    }
});

module.exports = router;