var express = require('express');
var router = express.Router();
var app = express();
var subNotificationLevel = require('./subNotificationLevel')

var session = require('client-sessions');

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
      }
    //   var webSocket = require('./external');
    //   webSocket.setOnConnection();
    //   //TODO
      
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
            session.role = req.body.role;
            var arr = session.user.replace(".", " ").split(/\d/, 1)[0].split(" ");
            session.userFirstName = arr[0].charAt(0).toUpperCase() + arr[0].slice(1);
            session.userSurname = arr[1].charAt(0).toUpperCase() + arr[1].slice(1);
            //console.log( "     "+session.user);
            res.redirect("/"+req.body.role+"/"+req.body.username);
        }else{
            res.redirect("/"); // pagine per errore
        } 
    });    
});

//PATIENT

router.get("/patient/:patientID", function(req, res) {
    var homeParameter = {
        user: session.user,
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    res.render('patientHome', homeParameter);
});

router.post("/patient/:patientID", function(req, res) {
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

router.get("/patient/:patientID/history", function(req, res) {
    var homeParameter = {
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    pubSubHistory.receiveHistory(res, session.user)
});

router.get("/patient/:patientID/advice", function(req, res) {
    var homeParameter = {
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    pubSubAdvice.receiveAdvices(res, session.user)
});

router.get("/patient/:patientID/drug", function(req, res) {
    var homeParameter = {
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    pubSubDrug.receiveDrugs(res, session.user)
});

router.get("/patient/:patientID/info", function(req, res){
    pubSubInfo.requestInfo(session.role, session.user, res);
    pubSubInfo.receiveInfo(res);
});

//DOCTOR

router.get("/doctor/:doctorID", function(req, res){
    var homeParameter = {
        user: session.user,
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    //TODO
    var webSocket = require('./socket');
    webSocket.setOnConnection(res, session.user, subNotificationLevel.receiveNotificationLevel2);

    //webSocket.setOnConnection();
    res.render("doctorHome", homeParameter);
});

router.post("/doctor/:doctorID", function(req, res){
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

router.get("/doctor/:doctorID/history", function(req, res){
    var homeParameter = {
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    pubSubHistory.receiveHistory(res, session.user);
});

router.get("/doctor/:doctorID/info", function(req, res){
    pubSubInfo.requestInfo(session.role, session.user, res);
    pubSubInfo.receiveInfo(res);
});

module.exports = router;
