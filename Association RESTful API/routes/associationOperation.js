/** Model Schema collection */
var associations = require('../models/patientDoctor');

/** insert new association in the database
 * 
 * @param  {String} idPatient - patient identifier
 * @param  {String} idDoctor - doctor identifier
 * @param  {Response} res - response of request
 */
function insertAssociation(idPatient, idDoctor, res){
    var association = { 
        "idPatient" : idPatient,
        "idDoctor": idDoctor
    };
    
    associations.create(association, function(err, doc) {
        if (err){
            res.send(400);
        } 
        res.send(200);
    });
}

/** Search a particular association between one doctor and one patient
 * 
 * @param  {String} idPatient - patient identifier
 * @param  {String} idDoctor - doctor identifier
 * @param  {Response} res - response of request- 
 */
function getOneAssociation(idPatient, idDoctor, res){
    var association = { 
        "idPatient" : idPatient,
        "idDoctor": idDoctor
    };

    associations.findOne(association,{"_id":0}, function(err, doc) {
        if (err){
            res.send(400);
        }  
      res.json(doc);
    });
}

/** Search all patients related to the doctor identifie by idDoctor
 * 
 * @param {String} idDoctor - doctor identifier
 * @param {Response} res - response of request
 */
function getPatients(idDoctor, res){
    associations.find({"idDoctor": idDoctor},{"_id":0, "idPatient":1},function(err, doc) {
        if (err){
            res.send(400);
        }  
      res.json(doc);       
    });
}

/** Search all doctors related to the patient identifie by idPatient
 * 
 * @param {String} idPatient - patient identifier 
 * @param {Response} res -response of request
 */
function getDoctors(idPatient, res){{String}
    associations.find({"idPatient": idPatient},{"_id":0, "idDoctor":1},function(err, doc) {
        if (err){
            res.send(400);
        }  
        res.json(doc);
    });
}

/**
 * 
 * @param {String} idPatient - patient identifier
 * @param {String} idDoctor - doctor identifier
 * @param {Response} res - response of request
 */
function removeAssociation(idPatient, idDoctor, res){
    associations.deleteOne({"idPatient" : idPatient,"idDoctor": idDoctor},function(err, doc) {
        if (err){
            res.send(400);
        } 
        res.send(200);
     });
}

module.exports = {insertAssociation, getDoctors, getOneAssociation, getPatients, removeAssociation}