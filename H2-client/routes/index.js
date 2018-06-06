var express = require('express');
var router = express.Router();
var doctor = require('./doctor');
var session= require('client-sessions');

var Client = require('node-rest-client').Client;
 
var client = new Client();
 var userId;


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
            userId = req.body.username;
            console.log( "     "+session.user);
            res.redirect("/"+req.body.role+"");
        }else{
            res.redirect("/"); // pagie per erroe
        }
        
    });
       
});

router.get("/patient/"+userId+"/");
router.get("/patient/"+userId+"/history");
router.get("/patient/"+userId+"/advice");
router.get("/patient/"+userId+"/drug");
router.get("/patient/"+userId+"/info");

router.get("/doctor", function(req, res){
    var homeParameter = {
        title: "Welcome " + (session.user).replace(".", " ")+ "."
      }
    
    res.render("doctorHome", homeParameter);
} );
router.get("/doctor/"+userId+"/history");
router.get("/doctor/"+userId+"/advice/edit");
router.get("/doctor/"+userId+"/drug/edit");
router.get("/doctor/"+userId+"/info");



module.exports = router;
