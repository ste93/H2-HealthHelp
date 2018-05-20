/** H2 ROUTING - RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var app = require('../app.js');


var session;

/** */
var userAuthentication = require('./userAuthentication');
var sensorDataOperation = require('./sensorDataOperations');

/** Connect Mongo DB */
require('../database');


/* Model Scheme */
var advices = require('../models/advice');
var drugs = require('../models/prescribedDrug');
var sensorData = require('../models/sensorData');

/* GET home page. */
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
   // session = req.session;
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

/** Add a value of a particular sensor
 * @response:  200 - OK
 *             500 - Internal Server Error
 * 
 * @param idCode - patient identifier
 * @param type - sensor type to add
 */
router.post('/sensors', function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');
    var message = req.param('message');

    sensorDataOperation.addValue(idCode, type, message , res);
});




module.exports = router;