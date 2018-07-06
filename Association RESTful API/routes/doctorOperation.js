/** RESTful API Associations
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Model Scheme collections*/
var doctors = require('../models/doctor');
var associations = require('../models/patientDoctor');


/** Returns doctor's informations
 *  
 * @throws 400 - BAD REQUEST
 *         404 - NOT FOUND
 * 
 * @returns a JSON with all doctor's informations
 * 
 * @param {String} id - doctor identifier
 * @param {Response} res - response of RESTful request
 * 
 */
module.exports.findDoctor = function(req, res){
    var id = req.param('_id');
    doctors.findOne({"_id": id },function(err, doc) {
        if(doc == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.json(doc); 
        }
      });
}

/** Inserts a new doctor
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)   
 * 
 * @param {String} id - doctor identifier
 * @param {String} name - doctor's name
 * @param {String} surname - doctor's surname
 * @param {String} cf - doctor's CF
 * @param {Response} res - response of RESTful request
 * 
 */
module.exports.insertDoctor = function(req, res){
    var id = req.param('_id');
    var name = req.param('name');
    var surname = req.param('surname');
    var cf = req.param('cf');

    var doctor = { 
        "_id" : id,
        "name" : name,
        "surname":surname,
        "cf" : cf
    }; 

    doctors.create(doctor, function(err, doc) {
        if (err){
            res.send(400);
        } else {
            res.send(200);
        }
    });
}

/** Removes a doctor and all his associations with patients
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)
 *             
 * @param {String} id - doctor identifier
 * @param {Response} res - response of RESTful request
 * 
 */
module.exports.removeDoctor = function(req, res){
    var id = req.param('_id');
    doctors.findByIdAndRemove({"_id": id}, function(err, doc){
        if(doc == null) {
            res.send(404);
        } else if (err){
            res.send(400);
        } else {        
            associations.remove({"idDoctor":id}, function(err, ass){
                console.log('removed all the dependencies')
            });        
            res.send(200)
        }
    });
}