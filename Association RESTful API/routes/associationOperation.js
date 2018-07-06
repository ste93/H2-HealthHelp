/** RESTful API Associations
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Model Schema collection */
var patients = require('../models/patient');
var doctors = require('../models/doctor');
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
module.exports.insertAssociation = function(req, res){
    var idPatient = req.param('idPatient');
    var idDoctor = req.param('idDoctor');

    var association = { 
        "idPatient": idPatient,
        "idDoctor": idDoctor
    };

    associations.findOne(association,{"_id":0}, function(err, ass) {
        if(ass == null) {
            patients.findOne({"_id": idPatient },function(err, pat) {
                if(pat != null) {
                    doctors.findOne({"_id": idDoctor },function(err, doc) {
                        if(doc != null) {
                            associations.create(association, function(err, ass) {
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
        } else if (err){
            res.send(400);
        } else {
            res.send(200);
        }
    });
    
}

module.exports.getAssociations = function(req, res){
    var idPatient = req.param('idPatient');
    var idDoctor = req.param('idDoctor');

    if(idPatient == undefined){
       _getPatients(idDoctor, res);
    } else if(idDoctor == undefined){
       _getDoctors(idPatient, res);
    } else {
       _getOneAssociation(idPatient, idDoctor, res);
    }
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
function _getOneAssociation(idPatient, idDoctor, res){
    var association = { 
        "idPatient" : idPatient,
        "idDoctor": idDoctor
    };

    associations.findOne(association,{"_id":0}, function(err, ass) {
        if(ass == null) {
            res.send(404);
        } else if (err){
            res.send(400);
        } else {
            res.json(ass);
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
function _getPatients(idDoctor, res){
    doctors.findOne({"_id": idDoctor},function(err, doc) {
        if(doc != null) {
            associations.find({"idDoctor": idDoctor},{"_id":0, "idPatient":1},function(err, pat) {
                if (err){
                    res.send(400);
                } else {
                    res.json(pat);
                }
            });
        } else {
            res.send(404);
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
function _getDoctors(idPatient, res){
    patients.findOne({"_id": idPatient },function(err, pat) {
        if(pat != null) {
            associations.find({"idPatient": idPatient},{"_id":0, "idDoctor":1},function(err, doc) {
                if (err){
                    res.send(400);
                } else {
                    res.json(doc);
                }
            });
        } else {
            res.send(404);
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
module.exports.removeAssociation = function (req, res){
    var idPatient = req.param('idPatient');
    var idDoctor = req.param('idDoctor');

    associations.findOne({"idPatient" : idPatient,"idDoctor": idDoctor},function(err, ass) {
        if(ass == null) {
            res.send(404);
        } else if (err){
            res.send(400);
        } else {
            associations.deleteOne({"idPatient" : idPatient,"idDoctor": idDoctor},function(err, assoc) {
                if (err){
                    res.send(400);
                } else {
                    res.send(200);
                }
             });
        }
     });
}