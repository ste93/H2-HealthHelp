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
    
   var mess  = JSON.parse("{"+message+"}");
   collection.create(mess, function(err, value){
        if(err){
            console.log("errore "+ err);
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

/** Returns all drugs prescribed to a specific patient
 * 
 * @throws 500 - Internal Server Error
 *         
 * @returns an array of all drugs related to the patient
 * 
 * @param {String} idCode - patient identifier
 * @param {String} start - the date from which get patien's drugs (it's optional)
 * @param {String} end - the date up to which get patien's drugs (it's optional)
 * @param {Response} res - response of RESTful request
 */
function getDrugsOnRange(idCode, start, end, res){
    var collection = _getCollection(idCode);

    var enddate;
    var startdate = new Date(start);
    end == undefined ? enddate = new Date() : enddate = new Date(end);
 
    collection.find({"timestamp": { $gte: startdate, $lt: enddate}},{"_id":0, "patientId":0},function(err, value){
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
    var mongoose = require('mongoose');
    var nameCollection= idCode+".drugs";
    var Schema = require('../models/prescribedDrug');
    return mongoose.model( idCode, Schema, nameCollection );   
    
}

module.exports = {addDrug, getDrugs, getDrugsOnRange}