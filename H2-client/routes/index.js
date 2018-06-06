var express = require('express');
var router = express.Router();

var Client = require('node-rest-client').Client;
 
var client = new Client();
 var userId = null;


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
    userId = req.body.username;
    client.get("http://localhost:3000/database/application/login?idCode=${username}&role=${role}&password=${password}", loginArgs,
    function (data, response) {
        if(response.statusCode == 200){
            res.redirect("/"+req.body.role+"/"+req.body.username);
        }else{
            res.redirect("/"); // pagine per erroe
        }
        
    });
       
});

router.get("/patient/"+userId+"/");
router.get("/patient/"+userId+"/history");
router.get("/patient/"+userId+"/advice");
router.get("/patient/"+userId+"/drug");
router.get("/patient/"+userId+"/info");

router.get("/doctor/"+userId+"/");
router.get("/doctor/"+userId+"/history");
router.get("/doctor/"+userId+"/advice/edit");
router.get("/doctor/"+userId+"/drug/edit");
router.get("/doctor/"+userId+"/info");



module.exports = router;
