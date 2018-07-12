/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Callback to add a prescibed drug related to a specific patient
 * 
 * @throws 200 - OK
 *         500 - Internal Server Error
 */
module.exports.addDrugs = function(req, res, next){
    var idCode = req.param('idCode');
    var message = req.param('message');

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

/** Callback to return all prescribed drugs or range of prescribed drugs related to a specific patient
 * 
 * @throws 500 - Internal Server Error
 */
module.exports.getAllDrugs = function(req, res, next){
    var idCode = req.param('idCode');
    var start = req.param('start');
    var end = req.param('end');
    
    if(start == undefined){
        var collection = _getCollection(idCode);

        collection.find({},{"_id":0}, function(err, value){
            if(err){
                res.send(500);
            } else {
                res.json(value);
            }
        });
    }else{
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
}

/**
 * @private function used to take a right collection
 * 
 * @param {String} idCode - patient identifier
 * 
 * @returns the right collection to query.
 */
function _getCollection(idCode){
    var mongoose = require('mongoose');
    var nameCollection= idCode+".drugs";
    var Schema = require('../models/prescribedDrug');
    return mongoose.model( idCode, Schema, nameCollection );   
    
}