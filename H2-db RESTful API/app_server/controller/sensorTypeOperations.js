/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */


/** Model scheme of collections */
var sensorData = require('../models/sensorData');
var patients = require('../models/patientData');

/** Callback to get the sensor type list related to a specific patient   */
module.exports.getSensorTypes = function(req,res, next){
    var idCode = req.param('idCode');
    
    patients.findOne({"idCode": idCode},{"_id":0,"sensors":1}, function(err, sensor){
        if (err){
            res.send(500);
        } else {
            res.json(sensor);
        }
    });
};

/** Callback to modify the sensor type list related to a specific patient */
module.exports.putSensorType = function(req, res, next){
    var idCode = req.param('idCode');
    var type = req.param('type');

    patients.findOne({"sensors": type}, function(err, response){
        if(response == null) {
            console.log("sensor type created");
            patients.update({"idCode": idCode},{ $push: { "sensors": [type] }},function(err, sensor){
                if(err){
                    res.send(500);
                } 
                res.send(200);
            });
        } else {
            console.log("already present");
            res.send(200);
        }
    });
};


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

