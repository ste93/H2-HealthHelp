/** RESTful API Associations
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Model Scheme collections*/
var patients = require('../models/patient');
var associations = require('../models/patientDoctor');


/** Returns patient's informations
 * 
 * @throws 400 - BAD REQUEST
 *         404 - NOT FOUND
 * 
 * @returns a JSON with all patient's informations
 * 
 * @param {String} id - patient id
 * @param {Response} res - response of RESTful request
 * 
 */
function findPatient(id, res){
    patients.findOne({"_id": id },function(err, pat) {
        if(pat == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.json(pat); 
        }
      });
}

/** Inserts a new patient
 * 
 * @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)   
 * 
 * @param {String} id - patient identifier
 * @param {String} name - patient's name
 * @param {String} surname - patient's surname
 * @param {String} cf - patient's CF
 * @param {Response} res - response of RESTful request
 * 
 */
function insertPatient(id, name, surname, cf, res){
    var patient = { 
        "_id" : id,
        "name" : name,
        "surname":surname,
        "cf" : cf
    }; 
    patients.create(patient, function(err, pat) {
        if (err){
            res.send(400);
        } else {
            res.send(200);
        }
    });
}

/** Removes a patient and all his associations with doctors
 * 
 * @throws 200 - OK
 *          400 - BAD REQUEST (missing or wrong parameters)
 *             
 * @param {String} id - patient identifier
 * @param {Response} res - response of RESTful request
 * 
 */
function removePatient(id, res){
    patients.remove({"_id": id}, function(err, pat){
        if (err){
            res.send(400);
        } else {        
            associations.remove({"idPatient":id}, function(err, ass){
                console.log('removed all the dependencies')
            });        
            res.send(200)
        }
    });
}

module.exports = {findPatient, insertPatient, removePatient};