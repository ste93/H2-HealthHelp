/** H2 ROUTING - RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */
var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var session;

/** Connect Mongo DB */
require('../database');


/* Model Scheme */
var advices = require('../models/advice');
var doctors = require('../models/doctorData');
var patients = require('../models/patientData');
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
    
    if(role == "doctor" || role == "patient"){
       if(role == "doctor"){
            var users = doctors;
       }else{
            var users = patients;
       }
       users.create(user, function(err, user){
            if(err){
               console.log('user registation error - %s', err);
               return res.send(400);
            }
            res.send(200);
       });
    }else{
        res.send(401);
        console.log("Not Authorized to register in this software.");  
    }
});

router.get('/login', function(req, res, next){
    session = req.session;
    var idCode = req.param('idCode');
    var role = req.param('role');
    var password = req.param('password');

    if(role == "doctor" || role == "patient"){
        if(role == "doctor"){
             var users = doctors;
        }else{
             var users = patients;
        }
        users.findOne({"idCode": idCode, "password": password},{"_id":0, "password":0, "phone":0, "mail":0, "cf":0}, function(err, user){
             if(err){
                console.log('user login error - %s', err);
                return res.send(400);
             }
             res.json(user)
        });
     }else{
         res.send(401);
         console.log("Not Authorized to enter in this software.");  
     }
});

module.exports = router;