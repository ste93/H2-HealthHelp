/** Take the mongoDB connection */
var mongoose = require('mongoose');
var db = mongoose.connection;

/** Add an advice in the DB 
 * 
 * @param message - advice to insert in json format
 * @param res - response of RESTful request
 */
function addDrug(idCode, message, res){
    var mess  = JSON.parse(message);

    var nameCollection= ""+idCode+".drugs";
    
    db.collection(nameCollection).insert(mess, function(err, value){
        if(err){
            res.send(500);
        }

        res.send(200);
    });
}

/** Returns all advices of a specific patient
 * 
 * @param idCode - patient identifier
 * @param res - response of RESTful request
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