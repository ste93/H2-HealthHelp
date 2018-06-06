var express = require('express');
var router = express.Router();
var doctor = require('./doctor');
var session= require('client-sessions');

var Client = require('node-rest-client').Client;
 
var client = new Client();

//var prova = require('../web_server/prova');
//router.get('/', prova.connectToTopic);
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

router.get("/patient");
router.get("/patient/history");
router.get("/patient/advice");
router.get("/patient/drug");
router.get("/patient/info");

router.get("/doctor", function(req, res){
    var homeParameter = {
        title: "WELCOME " + (session.user).replace(".", " ")+ "."
      }
    
    res.render("doctorHome", homeParameter);
} );
router.get("/doctor/history");
router.get("/doctor/advice/edit");
router.get("/doctor/drug/edit");
router.get("/doctor/info");



module.exports = router;
