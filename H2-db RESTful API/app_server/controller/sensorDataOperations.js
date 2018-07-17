/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */


/** Model scheme of collections */
var sensorData = require('../models/sensorData');
var patients = require('../models/patientData');

/** Callback to add a sensor data related to a secific patient. */
module.exports.addValue = function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');
    var message = req.param('message');

    _addValue(idCode, type, message, res);
};

/** Callback to delete a particular sensor data or a range of sensor data. */
module.exports.deleteValue = function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');
    var start = req.param('start');
    var end = req.param('end');
    
    if(start == undefined){
        _deleteAllValues(idCode, type, res);
    }else{
        _deleteAllValuesOnRange(idCode, type, start, end, res);
    }
    
}

/** Callback to get a particular sensor data or a range of sensor data. */
module.exports.getValues = function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');
    var start = req.param('start');
    var end = req.param('end');
    
    if(start == undefined){
        _getAllValuesOfSpecificSensor(idCode, type, res);
    }else{
        _getAllValuesOnRange(idCode, type, start, end, res);
    }
    
};


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
function _addValue(idCode, type, message, res){
    var collection = _getCollection(idCode,type);

    var entireMessage = _sensorValueJsonFormat(message)
    var messagetoInsert = JSON.parse(entireMessage)
    console.log("ENTRATO");
    patients.findOne({"idCode": idCode}, function(err, response){
        if(response == null) {
            console.log("ENTRATO"+err);
            res.send(404);
        } else {
        collection.save(messagetoInsert, function(err, value){
            if(err){
                console.log("ENTRATO"+err);
                res.send(500);
            } else {
                console.log("ENTRATO"+err);
                res.send(200);
            }
        });
    }});
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
function _deleteAllValues(idCode, type, res){
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

/** Deletes the values of a one sensor in a particular date's range
 * 
 * @throws 200 - OK
 *         404 - sensor type not found
 *         500 - Internal Server Error
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - sensor type of values to cancel
 * @param {String} start - start date to get the values
 * @param {String} end - start date to get the values
 * @param {Response} res - response of RESTful request
 */
function _deleteAllValuesOnRange(idCode, type, start, end, res){
    var collection = _getCollection(idCode,type);  var nameCollection= ""+idCode+"."+type;
    
    var enddate;
    var startdate = new Date(start);
    end == undefined ? enddate = new Date() : enddate = new Date(end);
    
    patients.findOne({"sensors": type}, function(err, response){
        if(response == null) {
            res.send(404);
        } else {
            
            collection.remove({ "timestamp": { $gte: startdate, $lt: enddate}}, function(err, value){
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
function _getAllValuesOfSpecificSensor(idCode, type, res){
    var collection = _getCollection(idCode,type);
    
    collection.find({},{"_id":0, "patientId":0},function(err, value){
        if(err){
            res.send(500);
        } else {
            res.json(value);
        }
    });
}

/** 
* @throws 200 - OK
*         
* @returns an array of all sensor's values related to the patient in a particular date's range
* 
* @param {String} idCode - patient identifier
* @param {String} type - sensor type of values to return
* @param {String} start - start date to get the values
* @param {String} end - start date to get the values
* @param {Response} res - response of RESTful request
*/
function _getAllValuesOnRange(idCode, type, start, end, res){
   var collection = _getCollection(idCode,type);

   var enddate;
   var startdate = new Date(start);
   end == undefined ? enddate = new Date() : enddate = new Date(end);

   collection.find({ "timestamp": { $gte: startdate, $lt: enddate}},{"_id":0, "patientId":0},function(err, value){
       if(err){
           res.send(500);
       } else {
           res.json(value);
       }
   });
}

/** creates or takes a dynamic collection to save, delete o get the information of sensor data
 * @private function used to take a right collection
 * 
 * @param {String} idCode - patient identifier
 * @param {String} type - sensor type 
 */
function _getCollection(idCode,type){
    var mongoose = require('mongoose');
    var nameCollection= ""+idCode+"."+type;
    var Schema = require('../models/sensorData');
    return mongoose.model( idCode, Schema, nameCollection );   
}

/**Returns the sensor's value message in format to covert in JSON object after.
 * 
 * @private function used to generate a message in right format
 * 
 * @param {String} message 
 */
function _sensorValueJsonFormat(message){
    var mess  = message.split("\"output\":");
    var entireMessage  = mess[0].concat("\"output\": {").concat(mess[1]);

    var mess2  = entireMessage.split(",\"unit\":");
    var x  = mess2[0].concat("},\"unit\":").concat(mess2[1]);
    
    console.log(x);
    return "{"+x+"}"
}

