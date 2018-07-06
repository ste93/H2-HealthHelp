/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */


/** Model scheme of collections */
var doctors = require('../models/doctorData');
var patients = require('../models/patientData');

var users = null;

/** Callback to get a personal information related to a specific patient 
 * @throws  404 - patient not found
 *          400 - wrong request
 *          200 - OK
*/
module.exports.getPatient = function(req, res, next){
    var idCode = req.param('idCode');

    patients.findOne({"idCode": idCode}, {"_id":0, "password":0}, function(err, pat) {
        if(pat == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.json(pat); 
        }
    });
}

/** Callback to get a personal information related to a specific doctor 
 * @throws  404 - patient not found
 *          400 - wrong request
 *          200 - OK
*/
module.exports.getDoctor = function(req, res, next){
    var idCode = req.param('idCode');

    doctors.findOne({"idCode": idCode}, {"_id":0, "password":0}, function(err, doc) {
        if(doc == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.json(doc); 
        }
    });
};

/** Callback to delete a personal information related to a specific patient 
 * @throws  404 - patient not found
 *          400 - wrong request
 *          200 - OK
*/
module.exports.deletePatient = function(req,res,next){
    var idCode = req.param('idCode');
    
    patients.findOneAndRemove({"idCode": idCode}, function(err, pat){
        if(pat == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.send(200);
        }
    });
}

/** Callback to delete a personal information related to a specific patient 
 * @throws  404 - patient not found
 *          400 - wrong request
 *          200 - OK
*/
module.exports.deleteDoctor = function(req, res, next){
    var idCode = req.param('idCode');
    
    doctors.findOneAndRemove({"idCode": idCode}, function(err, doc) {
        if(doc == null) {
            res.send(404);
        } else if (err) {
            res.send(400);
        } else {
            res.send(200);
        }
    });
};



