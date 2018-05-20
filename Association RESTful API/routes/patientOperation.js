/** Model Scheme collections*/
var patients = require('../models/patient');
var associations = require('../models/patientDoctor');


/** Search the patient information bt identifier 
 * 
 * @param {String} id - patient identifier
 * @param {Response} res - response of request 
 */
function findPatient(id, res){
    patients.findOne({"_id": id },function(err, doc) {
        if (err) {
            res.send(400);
        }
        res.json(doc); 
      });
}

/** Insert the new patient in collection
 * 
 * @param {String} id - patient identifier
 * @param {String} name 
 * @param {String} surname 
 * @param {String} cf 
 * @param {Response} res - response of request 
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
        } 
       res.send(200);
     });
}

/** Remove the patient and the assocition related to him
 * 
 * @param {String} id 
 * @param {Response} res 
 */
function removePatient(id, res){

    patients.remove({"_id": id}, function(err, pat){
        if (err){
            res.send(400);
        } 
        
        associations.remove({"idPatient":id}, function(err, ass){
            console.log('removed the dependecy')
         });
        
        res.send(200)
    });
}
module.exports = {findPatient, insertPatient, removePatient};