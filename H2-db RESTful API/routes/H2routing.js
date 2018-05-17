var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var session;
 /** Connect Mongo DB */
require('../database');
var db = mongoose.connection;

/* Model Scheme */
var advices = require('../models/advice');
var doctors = require('../models/doctorData');
var patients = require('../models/patientData');
var drugs = require('../models/prescribedDrug');
var sensorData = require('../models/sensorData');

/* GET home page. */
router.get('/', function(req, res, next) {
});

router.post('/registration', function(req, res, next){
    var role = req.param('role');
    var idCode = req.param('idCode');
    var password = req.param('password');
    var name = req.param('name');
    var surname = req.param('surname');
    var cf = req.param('cf');
    var phone = req.param('phone')
    var mail = req.param('mail')

    if(role == "doctor"){
        
    }else if(role == "patient"){

    }else{
        response.writeHead(401, {
            'Content-Length': Buffer.byteLength(body),
            'Content-Type': 'text/plain'
         });
        console.log("Not Authorized to register in this software.");  
    }

});

router.get('/login', function(req, res, next){
    session = req.session;
    var id = req.param('id');
    var role = req.param('role');
    var password = req.param('psw');

    if(session.id == id){
        
    }

    if(role == "doctor"){
        db.collection('')
    }else if(role == "patient"){

    }else{
        response.writeHead(401, {
            'Content-Length': Buffer.byteLength(body),
            'Content-Type': 'text/plain'
         });
        console.log("Not Authorized to enter in this software.");
    }
});

module.exports = router;