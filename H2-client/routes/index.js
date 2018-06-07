var express = require('express');
var router = express.Router();

var doctor = require('./doctor');
var history = require('./subscriberHistory');
var advices = require('./subscriberAdvices');
var session= require('client-sessions');
var patient = require('./patient')

var Client = require('node-rest-client').Client;
 
var client = new Client();

var userId;
var patientId;
var type;

router.get('/', function (req, res) {
    var homeParameter = {
        title: 'H2 - Login'
      }

    res.render('index', homeParameter);
});

router.post('/', function (req, res) {
    var loginArgs = {
        path: { "username": req.body.username, "password": req.body.password, "role": req.body.role }	
    };
   
    client.get("http://localhost:3000/database/application/login?idCode=${username}&role=${role}&password=${password}", loginArgs,
    function (data, response) {
        if(response.statusCode == 200){
            session.user = req.body.username;
            console.log( "     "+session.user);
            res.redirect("/"+req.body.role+"");
        }else{
            res.redirect("/"); // pagie per erroe
        }
        
    });
       
});

router.get("/patient", function(req, res) {
    session.role = "patient";
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")
      }

    res.render('patientHome', homeParameter);
});

router.post("/patient", function(req, res) {
    session.type = req.body.type;
    session.pat = session.user;
    patient.getDataHistory(req.body.type, session.user, req.body.start, req.body.end,res);
});

router.get("/patient/history", function(req, res) {
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")
    }
    history.getDataHistory(res, session.user)
});

router.get("/patient/advice", function(req, res) {
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")
    }
    advices.getAdvices(res, session.user)
});

router.get("/patient/drug");
router.get("/patient/info");





router.get("/doctor", function(req, res){
    session.role = "doctor";
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")+ "."
    }
    
    res.render("doctorHome", homeParameter);
});

router.post("/doctor", function(req, res){
    if(req.body.text != undefined){
        doctor.sendAdvice(req.body.pat, req.body.text, res);
    }else if(req.body.drug != undefined){
        
    }else{
        session.pat = req.body.pat;
        session.type = req.body.type;
        doctor.getDataHistory(req.body.type, req.body.pat, req.body.start, req.body.end, session.user,res);
    }
    
});

router.get("/doctor/history", function(req, res){
    history.getDataHistory(res, "doctor", session.user);
});

router.get("/doctor/drug/edit");
router.get("/doctor/info");



module.exports = router;
