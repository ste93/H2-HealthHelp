/** RESTful API Associations
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Model Scheme collections*/
var doctors = require('../models/doctor');
var patients = require('../models/patient');
var associations = require('../models/patientDoctor');

var users;


/** Returns user's informations
 *  
 * @throws 400 - BAD REQUEST
 * 
 * @returns a JSON with all user's informations
 * 
 * @param {String} id - user identifier
 * @param {String} role - user's role: patient or doctor
 * @param {Response} res - response of RESTful request
 * 
 */
function findUser(id, role, res){
    setCollection(role);
    users.findOne({"_id": id },function(err, user) {
        if (err) {
            console.error(400);
        } else {
            res.json(user); 
        }
    });
}

/** Inserts a new user
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)   
 * 
 * @param {String} role - user's role: patient or doctor
 * @param {String} id - user identifier
 * @param {String} name - user's name
 * @param {String} surname - user's surname
 * @param {String} cf - user's CF
 * @param {Response} res - response of RESTful request
 * 
 */
function insertUser(role, id, name, surname, cf, res){
    setCollection(role);
    var user = { 
        "_id" : id,
        "name" : name,
        "surname":surname,
        "cf" : cf
    }; 
    users.create(user, function(err, user) {
        if (err){
            res.send(400);
        } else {
            res.send(200);
        }
     });
}

/** Removes a user and all his associations with patients/doctors
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)
 *             
 * @param {String} role - user's role: patient or doctor
 * @param {String} id - user identifier
 * @param {Response} res - response of RESTful request
 * 
 */
function removeUser(role, id, res){
    setCollection(role);
    users.remove({"_id": id}, function(err, ass){
        if (err){
            res.send(400);
        } else {
            if(role == 'patient'){
                associations.remove({"idPatient":id}, function(err, ass){
                    console.log('removed all the dependencies')
                });
            } else {
                associations.remove({"idDoctor":id}, function(err, ass){
                    console.log('removed all the dependencies')
                });
            }
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

module.exports = {findDoctor, insertDoctor, removeDoctor};