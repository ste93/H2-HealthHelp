/** Model Scheme collections*/
var doctors = require('../models/doctor');
var associations = require('../models/patientDoctor');


/** Search the doctor information bt identifier 
 * 
 * @param {String} id - doctor identifier
 * @param {Response} res - response of request 
 */
function findDoctor(id, res){
    doctors.findOne({"_id": id },function(err, doc) {
        if (err) {
            console.error(400);
        }
        res.json(doc); 
      });
}

/** Insert the new doctor in collection
 * 
 * @param {String} id - doctor identifier
 * @param {String} name 
 * @param {String} surname 
 * @param {String} cf 
 * @param {Response} res - response of request 
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
        }
       res.send(200);
     });
}

/** Remove from database the doctor and remove also the association related.
 * 
 * @param {String} id - doctor identifie
 * @param {Response} res -result of request
 */
function removeDoctor(id, res){

    doctors.remove({"_id": id}, function(err, ass){
        if (err){
            res.send(400);
        } 

        associations.remove({"idDoctor":id}, function(err, ass){
          console.log('removed the dependecy')
        });
        
        res.send(200)
    });
}

module.exports = {findDoctor, insertDoctor, removeDoctor};