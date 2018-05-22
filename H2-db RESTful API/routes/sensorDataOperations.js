/** Take the mongoDB connection */
var mongoose = require('mongoose');
var db = mongoose.connection;

/** Model scheme of collections */
var sensorData = require('../models/sensorData');
var patients = require('../models/patientData');

/** Search the sensor types
 * 
 * @param id - patient identifier
 * @param res - result of RESTful request 
 */
function getSensorTypes(id, res){   
    
    patients.findOne({"idCode": id},{"_id":0,"sensors":1}, function(err, sensor){
        if (err){
            res.send(500);
        }
        res.json(sensor);
    });
}

/** Insert the new sensor type and create a collection related to it
 * 
 * @param id - patient identifier
 * @param type - sensor type inserted
 * @param res - result of RESTful request 
 */
function addSensorType(id, type, res){

    patients.findOne({"sensors": type}, function(err, response){
        if(response == null) {
            console.log("sensor type created");
            patients.update({"idCode": id},{ $push: { "sensors": [type] }},function(err, sensor){
                if(err){
                    res.send(500);
                }
        
                var nameCollection= ""+id+"."+type;
                db.createCollection(nameCollection);
                
                var schema = require('../models/sensorData');
                var collection =  mongoose.model(nameCollection, schema);
                
                res.send(200);
            });
        } else {
            console.log("already present");
            res.send(200);
        }
    });

}

/** Add the particular sensor type's value 
 * 
 * @param id - patient identifier
 * @param type - sensor type related to value
 * @param message - value to insert in json format
 * @param res - response of RESTful request
 */
function addValue(id, type, message, res){
    var nameCollection= ""+id+"."+type;
    var mess  = JSON.parse(message);
    db.collection(nameCollection).insert(mess, function(err, value){
        if(err){
            res.send(500);
        }

        res.send(200);
    });
}

/** Delete all values of a sensor type
 * 
 * @param {String} id 
 * @param {String} type 
 * @param {Response} res 
 */
function deleteAllValues(id, type, res){
    var nameCollection= ""+id+"."+type;
    
    patients.findOne({"sensors": type}, function(err, response){
        if(response == null) {
            res.send(404);
        } else {
            db.collection(nameCollection).remove({}, function(err, value){
                if(err){
                    res.send(500);
                }
        
                res.send(200);
            });
        }
    });
}

function getAllValuesOfSpecificSensor(id, type, res){
    var nameCollection= ""+id+"."+type;
    
    db.collection(nameCollection).find({}).toArray(function(err, value){
        if(err){
            res.send(500);
        }
        
        res.json(value);
    });
}

module.exports = {getSensorTypes, addSensorType, addValue, deleteAllValues, getAllValuesOfSpecificSensor}