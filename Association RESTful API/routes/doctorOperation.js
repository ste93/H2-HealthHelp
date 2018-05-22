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
 * 
 * @returns a JSON with all doctor's informations
 * 
 * @param {String} id - doctor identifier
 * @param {Response} res - response of RESTful request
 * 
 */
function findDoctor(id, res){
    doctors.findOne({"_id": id },function(err, doc) {
        if (err) {
            console.error(400);
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
function insertDoctor(id, name, surname, cf, res){
    var doctor = { 
        "_id" : id,
        "name" : name,
        "surname":surname,
        "cf" : cf
    }; 
    doctors.create(doctor, function(err, pat) {
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
function removeDoctor(id, res){
    doctors.remove({"_id": id}, function(err, ass){
        if (err){
            res.send(400);
        } else {
            associations.remove({"idDoctor":id}, function(err, ass){
                console.log('removed all the dependencies')
            });
            res.send(200);
        }
    });
}

module.exports = {findDoctor, insertDoctor, removeDoctor};