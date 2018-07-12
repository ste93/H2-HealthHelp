/** RESTful API Associations
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** setting express */
var express = require('express');
var router = express.Router();

 /** Connect Mongo DB */
require('../../database');

/** files .js that include the callback of request*/
var patientOperation = require('../controller/patientOperation');
var doctorOperation = require('../controller/doctorOperation');
var associationOperation = require('../controller/associationOperation');

/** GET home page. */
router.get('/', function(req, res, next) {});

/** GET request to return patient's informations */
router.get('/patients', patientOperation.findPatient);

/** POST request to insert a new patientv */
router.post('/patients', patientOperation.insertPatient);

/** DELETE request to remove a patient and all his associations with doctors */
router.delete('/patients', patientOperation.removePatient);

/** GET request to return doctors's informations */
router.get('/doctors', doctorOperation.findDoctor);
  
/** POST request to insert a new doctor */
router.post('/doctors', doctorOperation.insertDoctor);
 
/** DELETE request to remove a doctor and all his associations with patients */
router.delete('/doctors', doctorOperation.removeDoctor);


/** POST request to insert a new medical association of doctor and patient */
router.post('/relationship', associationOperation.insertAssociation);

/** GET request to find the association between specific doctor and patient
 *  or to find all doctor's or patient's associations */
router.get('/relationship', associationOperation.getAssociations);

/** DELETE request to remove an association between specific doctor and patient */
router.delete('/relationship', associationOperation.removeAssociation);

module.exports = router;