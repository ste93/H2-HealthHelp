var express = require('express');
var router = express.Router();

var session= require('client-sessions');

var userAuthentication = require('./userAuthentication');
var pubSubAdvice = require('./pubSubAdvice');
var pubSubHistory = require('./pubSubHistory');
var pubSubInfo = require('./pubSubInfo');
var pubSubDrug = require('./pubSubDrug');

var Client = require('node-rest-client').Client;
 
var client = new Client();

var userId;
var patientId;
var type;

router.get('/', function (req, res) {
    var homeParameter = {
        title: 'H2 - Login'
    };
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
            res.redirect("/"); // pagine per errore
        }
    });    
});

router.get("/patient", function(req, res) {
    session.role = "patient";
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")
    };
    res.render('patientHome', homeParameter);
});

router.post("/patient", function(req, res) {
    session.pat = session.user;
    if(req.body.startAdvice != undefined){
        pubSubAdvice.requestAdvices(session.user, req.body.startAdvice, req.body.end, res);
    }else if(req.body.startDrug != undefined){
        pubSubDrug.requestDrugs(session.user, req.body.startDrug, req.body.end, res);
    }else{
        session.type = req.body.type;
        pubSubHistory.requestHistory(req.body.type, session.user, req.body.start, req.body.end, session.user, res);
    }    
});

router.get("/patient/history", function(req, res) {
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")
    };
    pubSubHistory.receiveHistory(res, session.user)
});

router.get("/patient/advice", function(req, res) {
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")
    };
    pubSubAdvice.receiveAdvices(res, session.user)
});

router.get("/patient/drug", function(req, res) {
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")
    };
    pubSubDrug.receiveDrugs(res, session.user)
});

router.get("/patient/info", function(req, res){
    pubSubInfo.requestInfo(session.role, session.user, res);
    pubSubInfo.receiveInfo(res);
});

router.get("/doctor", function(req, res){
    session.role = "doctor";
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")
    };
    res.render("doctorHome", homeParameter);
});

router.post("/doctor", function(req, res){
    if(req.body.text != undefined){
        pubSubAdvice.sendNewAdvice(req.body.pat, req.body.text, res);
    }else if(req.body.drug != undefined){
        pubSubDrug.sendNewDrug(req.body.pat, req.body.drug,res);
    }else{
        session.pat = req.body.pat;
        session.type = req.body.type;
        pubSubHistory.requestHistory(req.body.type, req.body.pat, req.body.start, req.body.end, session.user, res);
    }
});

router.get("/doctor/history", function(req, res){
    pubSubHistory.receiveHistory(res, session.user);
});

router.get("/doctor/info", function(req, res){
    pubSubInfo.requestInfo(session.role, session.user, res);
    pubSubInfo.receiveInfo(res);
});

module.exports = router;
