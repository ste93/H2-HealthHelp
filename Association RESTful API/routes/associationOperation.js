/** RESTful API Associations
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Model Schema collection */
var associations = require('../models/patientDoctor');

/** Inserts a new medical association of doctor and patient
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)
 *         404 - NOT FOUND (patient or doctor not found)
 * 
 * @param {String} idPatient - patient identifier
 * @param {String} idDoctor - doctor identifier
 * @param {Response} res - response of RESTful request
 * 
 */
function insertAssociation(idPatient, idDoctor, res){
    var association = { 
        "idPatient": idPatient,
        "idDoctor": idDoctor
    };
    patients.findOne({"_id": idPatient },function(err, pat) {
        if(pat != null) {
            doctors.findOne({"_id": idDoctor },function(err, doc) {
                if(doc != null) {
                    associations.create(association, function(err, doc) {
                        if (err){
                            res.send(400);
                        } else {
                            res.send(200);
                        }
                    });
                } else {
                    res.send(404);
                }
            });
        } else {
            res.send(404);
        }
    });
    
}

/** Finds all patients associated to the doctor
 * 
 * @throws 400 - BAD REQUEST
 * 
 * @returns a JSON with all patients associated to the doctor
 * 
 * @param {String} idDoctor - doctor identifier
 * @param {Response} res - response of RESTful request
 * 
 */
function getPatients(idDoctor, res){
    associations.find({"idDoctor": idDoctor},{"_id":0, "idPatient":1},function(err, patients) {
        if (err){
            res.send(400);
        } else {
            res.json(patients);
        }
    });
}

/** Finds all doctors associated to the patient
 * 
 * @throws 400 - BAD REQUEST
 * 
 * @returns a JSON with all doctors associated to the patient
 * 
 * @param {String} idPatient - patient identifier 
 * @param {Response} res - response of RESTful request
 * 
 */
function getDoctors(idPatient, res){{String}
    associations.find({"idPatient": idPatient},{"_id":0, "idDoctor":1},function(err, doctors) {
        if (err){
            res.send(400);
        } else {
            res.json(doctors);
        }
    });
}

/** Finds the association between a specific doctor and patient
 *  
 * @throws 400 - BAD REQUEST
 * 
 * @returns a JSON with the association
 * 
 * @param {String} idPatient - patient identifier
 * @param {String} idDoctor - doctor identifier
 * @param {Response} res - response of RESTful request
 * 
 */
function getOneAssociation(idPatient, idDoctor, res){
    var association = { 
        "idPatient" : idPatient,
        "idDoctor": idDoctor
    };
    associations.findOne(association,{"_id":0}, function(err, association) {
        if (err){
            res.send(400);
        } else {
            res.json(association);
        }
    });
}

/** Removes an association between specific doctor and patient
 *   
 * @throws 200 - OK
 *         400 - BAD REQUEST             
 * 
 * @param {String} idPatient - patient identifier
 * @param {String} idDoctor - doctor identifier
 * @param {Response} res - response of RESTful request
 * 
 */
function removeAssociation(idPatient, idDoctor, res){
    associations.deleteOne({"idPatient" : idPatient,"idDoctor": idDoctor},function(err, doc) {
        if (err){
            res.send(400);
        } else {
            res.send(200);
        }
     });
}

module.exports = {insertAssociation, getDoctors, getOneAssociation, getPatients, removeAssociation}