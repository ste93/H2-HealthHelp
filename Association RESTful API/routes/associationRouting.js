/** RESTful API Associations
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** setting express */
var express = require('express');
var router = express.Router();

 /** Connect Mongo DB */
require('../database');

/** files .js that include the callback of request*/
var patientOperation = require('./patientOperation');
var doctorOperation = require('./doctorOperation');
var associationOperation = require('./associationOperation');

/** GET home page. */
router.get('/', function(req, res, next) {});

/** GET request to return patient's informations
 *  
 * @throws 400 - BAD REQUEST
 * 
 * @returns a JSON with all patient's informations
 * 
 * @param {String} id - patient id
 * 
 */
router.get('/patients', patientOperation.findPatient);

/** POST request to insert a new patient
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)   
 * 
 * @param {String} id - patient identifier
 * @param {String} name - patient's name
 * @param {String} surname - patient's surname
 * @param {String} cf - patient's CF
 * 
 */
router.post('/patients', patientOperation.insertPatient);

/** DELETE request to remove a patient and all his associations with doctors
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)
 *             
 * @param {String} id - patient identifier
 * 
 */
router.delete('/patients', patientOperation.removePatient);

/** GET request to return doctors's informations
 *  
 * @throws 400 - BAD REQUEST
 * 
 * @returns a JSON with all doctor's informations
 * 
 * @param {String} id - doctor identifier
 * 
 */
router.get('/doctors', doctorOperation.findDoctor);
  
/** POST request to insert a new doctor
 *  
 *  @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)   
 * 
 * @param {String} id - doctor identifier
 * @param {String} name - doctor's name
 * @param {String} surname - doctor's surname
 * @param {String} cf - doctor's CF
 * 
 */
router.post('/doctors', doctorOperation.insertDoctor);
 
/** DELETE request to remove a doctor and all his associations with patients
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST (missing or wrong parameters)
 *             
 * @param {String} id - doctor identifier
 * 
 */
router.delete('/doctors', doctorOperation.removeDoctor);


/** POST request to insert a new medical association of doctor and patient
 *  
 * @throws 200 - OK
 *         400 - BAD REQUEST(missing or wrong parameters)
 * 
 * @param {String} idPatient - patient identifier
 * @param {String} idDoctor - doctor identifier
 * 
 */
router.post('/relationship', associationOperation.insertAssociation);

/** GET request to find the association between specific doctor and patient
 *  or to find all doctor's or patient's associations
 *  
 * @throws 400 - BAD REQUEST
 * 
 * @returns a JSON with the association or with all doctors/patients associated to the patient/doctor
 * 
 * @param {String} idPatient - patient identifier
 * @param {String} idDoctor - doctor identifier
 * 
 */
router.get('/relationship', associationOperation.getAssociations);

/** DELETE request to remove an association between specific doctor and patient
 *   
 * @throws 200 - OK
 *         400 - BAD REQUEST *             
 * 
 * @param {String} idPatient - patient identifier
 * @param {String} idDoctor - doctor identifier
 * 
 */
router.delete('/relationship', associationOperation.removeAssociation);

module.exports = router;