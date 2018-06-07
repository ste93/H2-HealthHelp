var express = require('express');
var router = express.Router();

var doctor = require('./doctor');
var history = require('./subscriberHistory');
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
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")+ "."
      }

    res.render('patientHome', homeParameter);
});

router.post("/patient", function(req, res) {
    console.log("soooooooooooooooooooooooonoooooooooooooooooooooo");
    console.log(req.body.type);
    console.log(req.body.start);
    console.log(req.body.end);
    patient.getDataHistory(req.body.type, userId, req.body.start, req.body.end, res);
});



router.get("/patient/history", function(req, res) {
    console.log("soooooooooooooooooooooooonoooooooooooooooooooooo in history");
    res.render('history', {title: 'Data History'});
});

router.get("/patient/advice");
router.get("/patient/drug");
router.get("/patient/info");


router.get("/doctor", function(req, res){
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")+ "."
    }
    
    res.render("doctorHome", homeParameter);
});

router.post("/doctor", function(req, res){
    session.pat = req.body.pat;
    session.type = req.body.type;
    doctor.getDataHistory(req.body.type, req.body.pat, req.body.start, req.body.end, session.user,res);
});

router.get("/doctor/history", function(req, res){
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")+ "."
    }
    history.getDataHistory(res, "doctor", session.user)
   // res.render('historyPage', {title: 'Data History', patient: 'patient: '+session.pat, type: "sensor type: "+session.type });
});
router.get("/doctor/advice/edit");
router.get("/doctor/drug/edit");
router.get("/doctor/info");



module.exports = router;