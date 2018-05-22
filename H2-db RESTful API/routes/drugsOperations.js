/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Take the mongoDB connection */
var mongoose = require('mongoose');
var db = mongoose.connection;

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
    var mess  = JSON.parse(message);
    var nameCollection= ""+idCode+".drugs";
    db.collection(nameCollection).insert(mess, function(err, value){
        if(err){
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
    var nameCollection= ""+idCode+".drugs";
    db.collection(nameCollection).find({}).toArray(function(err, value){
        if(err){
            res.send(500);
        } else {
            res.json(value);
        }
    });
}

module.exports = {addDrug, getDrugs}