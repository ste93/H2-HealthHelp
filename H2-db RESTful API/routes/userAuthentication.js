var doctors = require('../models/doctorData');
var patients = require('../models/patientData');

function registation(role, user, res){
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
}

function login(idCode, role, password, res){
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
}

module.exports = {registation, login}