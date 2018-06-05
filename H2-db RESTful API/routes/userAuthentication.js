/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Model scheme of collections */
var doctors = require('../models/doctorData');
var patients = require('../models/patientData');

var users = null;

/** Sings up a new user in H2 application.
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST(missing parameters or wrong idCode)
 *         401 - UNAUTHORIZED (role is not valid)
 * 
 * @param {String} role - user's role: "doctor" or "patient" label
 * @param {String} user - a description of the user containing all his informations in JSON format 
 * @param {Response} res - response of RESTful request
 */
function registation(role, user, res){
    setCollection(role);
    if(users == null){
        res.send(404);
        console.log("Not Authorized to register in this software.");  
    } else {
        users.create(user, function(err, user){
            if(err){
                console.log('user registation error - %s', err);
                return res.send(400);
            } else {
                res.send(200);
            }
       });
       users = null;
    }
}

/** Singns in a user already registered in H2 application
 * 
 * @throws 200 - OK
 *         400 - BAD REQUEST(missing or wrong parameters)
 *         401 - UNAUTHORIZED (role is not valid)
 *
 * @param {String} role - user's role: "doctor" or "patient" label
 * @param {String} idCode - user identifier
 * @param {String} password - user's password
 * @param {Response} res - response of RESTful request
 */
function login(idCode, role, password, res){
    setCollection(role);
    if(users == null){
        res.send(401);
        console.log("Not Authorized to enter in this software.");  
    } else {
        users.findOne({"idCode": idCode, "password": password},{"_id":0, "password":0, "phone":0, "mail":0, "cf":0}, function(err, user){
            if(err){
                console.log('user login error - %s', err);
                return res.send(400);
            } else {
                res.json(user);
            }
       });
       users = null;
    }
}

function findPatient(idCode, res){
    patients.findOne({"idCode": idCode}, function(err, pat) {
        if(pat == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.json(pat); 
        }
    });
}

function findDoctor(idCode, res){
    doctors.findOne({"idCode": idCode}, function(err, doc) {
        if(doc == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.json(doc); 
        }
    });
}

function deletePatient(idCode, res){
    patients.findOneAndRemove({"idCode": idCode}, function(err, pat){
        if(pat == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.send(200);
        }
    });
}

function deleteDoctor(idCode, res){
    doctors.findOneAndRemove({"idCode": idCode}, function(err, doc) {
        if(doc == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.send(200);
        }
    });
}

/** Private function to set the right user's collection
 * 
 * @param {String} role - user's role: patient or doctor
 */
function setCollection(role){
    if(role == "doctor"){
        users = doctors;
    } else if(role == "patient"){
        users = patients;
    }
}

module.exports = {registation, login, findPatient, findDoctor, deletePatient, deleteDoctor}