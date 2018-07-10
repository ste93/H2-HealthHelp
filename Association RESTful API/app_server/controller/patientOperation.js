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
 */
module.exports.findPatient =  function(req, res){
    var id = req.param('_id');
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
 */
module.exports.insertPatient = function(req, res){
    var id = req.param('_id');
    var name = req.param('name');
    var surname = req.param('surname');
    var cf = req.param('cf');

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
 *         400 - BAD REQUEST (missing or wrong parameters)
 */
module.exports.removePatient = function(req, res){
    var id = req.param('_id');
    patients.findByIdAndRemove({"_id": id}, function(err, pat){
        if(pat == null) {
            res.send(404);
        } else if (err){
            res.send(400);
        } else {        
            associations.remove({"idPatient":id}, function(err, ass){
                console.log('removed all the dependencies')
            });        
            res.send(200)
        }
    });
}