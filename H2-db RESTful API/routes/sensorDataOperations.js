/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Take the mongoDB connection */
var mongoose = require('mongoose');
var db = mongoose.connection;

/** Model scheme of collections */
var sensorData = require('../models/sensorData');
var patients = require('../models/patientData');

/** Returns all sensor types related to a specific patient
 * 
 * @throws 500 - Internal Server Error
 * 
 * @returns a JSON with a list of sensors related to a specific patient
 * 
 * @param {String} idCode - patient identifier
 * @param {Response} res - response of RESTful request
 */
function getSensorTypes(idCode, res){   
    patients.findOne({"idCode": idCode},{"_id":0,"sensors":1}, function(err, sensor){
        if (err){
            res.send(500);
        } else {
            res.json(sensor);
        }
    });
}

/** Adds a new sensor types related to a specific patient
 *  and create a related collection to save all sensor's values
 * 
 * @throws 200 - OK
 *         500 - Internal Server Error
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - sensor type to add
 * @param {Response} res - response of RESTful request
 */
function addSensorType(idCode, type, res){
    patients.findOne({"sensors": type}, function(err, response){
        if(response == null) {
            console.log("sensor type created");
            patients.update({"idCode": idCode},{ $push: { "sensors": [type] }},function(err, sensor){
                if(err){
                    res.send(500);
                } 
                    var nameCollection= ""+idCode+"."+type;
                    db.createCollection(nameCollection);              
                    res.send(200);
            });
        } else {
            console.log("already present");
            res.send(200);
        }
    });

}

/** Adds a new value of a particular sensor related to a patient
 * 
 * @throws 200 - OK
 *         500 - Internal Server Error
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - value's sensor type
 * @param {String} message - message containing the value and other informations correlated to it
 * @param {Response} res - response of RESTful request
 */
function addValue(idCode, type, message, res){
    var collection = _getCollection(idCode,type);

    var mess  = JSON.parse(message);
    collection.create(mess, function(err, value){
        if(err){
            res.send(500);
        } else {
            res.send(200);
        }
    });
}

/** Deletes all values of a particular sensor
 * 
 * @throws 200 - OK
 *         404 - sensor type not found
 *         500 - Internal Server Error
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - sensor type of values to cancel
 * @param {Response} res - response of RESTful request
 */
function deleteAllValues(idCode, type, res){
    var collection = _getCollection(idCode,type);

    patients.findOne({"sensors": type}, function(err, response){
        if(response == null) {
            res.send(404);
        } else {
            collection.remove({}, function(err, value){
                if(err){
                    res.send(500);
                } else {
                    res.send(200);
                }
            });
        }
    });
}

/** Returns all values of a particular sensor type related to a patient
 * 
 * @throws 200 - OK
 *         
 * @returns an array of all sensor's values related to the patient
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - sensor type of values to return
 * @param {Response} res - response of RESTful request
 */
function getAllValuesOfSpecificSensor(idCode, type, res){
    var collection = _getCollection(idCode,type);
    collection.find({}).toArray(function(err, value){
        if(err){
            res.send(500);
        } else {
            res.json(value);
        }
    });
}

/** create o take a dynamic collection to save, delete o get the information of sensor data
 * @private function used to take a right collection
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - sensor type 
 */
function _getCollection(idCode,type){
    var nameCollection= ""+idCode+"."+type;
    var Schema = require('../models/sensorData');
    return mongoose.model( idCode, Schema, nameCollection );   
    
}

module.exports = {getSensorTypes, addSensorType, addValue, deleteAllValues, getAllValuesOfSpecificSensor}