/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Adds a drug prescibed to a specific patient
 * 
 * @throws 200 - OK
 *         500 - Internal Server Error
 * 
 * @param {String} idCode - patient identifier
 * @param {String} message - drug to insert in JSON format
 * @param {Response} res - response of RESTful request
 */
function addDrug(idCode, message, res){
    var collection = _getCollection(idCode);
    
    var mess  = JSON.parse(message);
   collection.create(mess, function(err, value){
        if(err){
            console.log(err);
            res.send(500);
        } else {
            res.send(200);
        }
    });
}

/** Returns all drugs prescribed to a specific patient
 * 
 * @throws 500 - Internal Server Error
 *         
 * @returns an array of all drugs related to the patient
 * 
 * @param {String} idCode - patient identifier
 * @param {Response} res - response of RESTful request
 */
function getDrugs(idCode, res){
    var collection = _getCollection(idCode);

    collection.find({},{"_id":0}, function(err, value){
        if(err){
            res.send(500);
        } else {
            res.json(value);
        }
    });
}

/**
 * @private function used to take a right collection
 * 
 * @param {String} idCode - patient identifier
 */
function _getCollection(idCode){
    var nameCollection= idCode+".drugs";
    var Schema = require('../models/prescribedDrug');
    return mongoose.model( idCode, Schema, nameCollection );   
    
}

module.exports = {addDrug, getDrugs}