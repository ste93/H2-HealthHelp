var express = require('express');
var router = express.Router();

 /** Connect Mongo DB */
require('../database');

/** files .js that include the callback of request*/
var patientOperation = require('./patientOperation');
var doctorOperation = require('./doctorOperation');
var asssociationOperation = require('./associationOperation');

/** GET home page. */
router.get('/', function(req, res, next) {});

/** GET request to find the patient information by idPatient
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST
 * 
 * @param id - patient id
 * @param res - response of request
 */
router.get('/patients', function(req, res, next){
  var id = req.param('_id');
  
  patientOperation.findPatient(id, res);
});

/** POST request to insert a new patient in database
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST(missing or wrong parameters)
 *             
 * 
 * @param id - patient identifier
 * @param name 
 * @param surname
 * @param cf
 * @param res - response of request
 */
router.post('/patients', function(req,res,next){
    var id = req.param('_id');
    var name = req.param('name');
    var surname = req.param('surname');
    var cf = req.param('cf');
   
   patientOperation.insertPatient(id, name, surname, cf, res);
});

/** DELETE request to remove a patient by id
 *  and remove also the associations with doctors
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST(missing or wrong parameters)
 *             
 * 
 * @param id - patient identifier
 * @param res - response of request
 */
router.delete('/patients', function(req, res, next){
    var id = req.param('_id');
    
    patientOperation.removePatient(id, res);
})

/** GET request to find the doctor information by idPatient
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST
 * 
 * @param id - doctor id
 * @param res - response of request
 */
router.get('/doctors', function(req, res, next){
    var id = req.param('_id');
    
   doctorOperation.findDoctor(id, res);
});
  
/** POST request to insert a new doctor in database
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST(missing or wrong parameters)
 *             
 * 
 * @param id - doctor identifier
 * @param name 
 * @param surname
 * @param cf
 * @param res - response of request
 */
router.post('/doctors', function(req,res,next){
    var id = req.param('_id');
    var name = req.param('name');
    var surname = req.param('surname');
    var cf = req.param('cf');

    doctorOperation.insertDoctor(id, name, surname, cf, res);
});
 
/** DELETE request to remove a doctor by id
 *  and remove also the associations with patients
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST(missing or wrong parameters)
 *             
 * 
 * @param id - doctor identifier
 * @param res - response of request
 */
router.delete('/doctors', function(req, res, next){
    var id = req.param('_id');

    doctorOperation.removeDoctor(idPatient, idDoctor, res);
});


/** POST request to insert a new medical association of doctor with patient
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST(missing or wrong parameters)
 *             
 * 
 * @param idPatient - patient identifier
 * @param idDoctor - doctor identifier
 * @param res - response of request
 */
router.post('/relationship', function(req, res, next){
    var idPatient = req.param('idPatient');
    var idDoctor = req.param('idDoctor');

    asssociationOperation.insertAssociation(idPatient, idDoctor, res);
});

/** GET request to find the doctor information by doctor identifier
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST
 * 
 * @param id - doctor id
 * @param res - response of request
 */
router.get('/doctors', function(req, res, next){
    var id = req.param('_id');
    
   doctorOperation.findDoctor(id, res);
});

/** GET request to find the association between a specific doctor and patient
 *  or to find the doctor's or patient's association related to them
 *  
 *  @response:  200 - OK
 *              400 - BAD REQUEST
 * 
 * @param id - doctor id
 * @param res - response of request
 */
router.get('/relationship', function(req, res, next){
    var idPatient = req.param('idPatient');
    var idDoctor = req.param('idDoctor');

    if(idPatient == undefined){
        asssociationOperation.getPatients(idDoctor, res);
    }else if(idDoctor == undefined){
        asssociationOperation.getDoctors(idPatient, res);
    }else{
       asssociationOperation.getOneAssociation(idPatient, idDoctor, res);
    }
});

/** DELETE request to remove a particular association
 *   
 *  @response:  200 - OK
 *              400 - BAD REQUEST(missing or wrong parameters)
 *             
 * 
 * @param idPatient - patient identifier
 * @param idDoctor - doctor identifier
 * @param res - response of request
 */
router.delete('/relationship', function(req, res, next){
    var idPatient = req.param('idPatient');
    var idDoctor = req.param('idDoctor');
    
   asssociationOperation.removeAssociation(idPatient, idDoctor, res);
});

module.exports = router;