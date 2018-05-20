var mongoose = require('mongoose');
var db = mongoose.connection;

var sensorData = require('../models/sensorData');
var patients = require('../models/patientData');


function getSensorTypes(id, res){   
    
    patients.findOne({"idCode": id},{"_id":0,"sensors":1}, function(err, sensor){
        if (err){
            res.send(500);
        }
        res.json(sensor);
    });
}

function addSensorType(id, type, res){

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

}

function addValue(id, type, message, res){
    var nameCollection= ""+id+"."+type;
    var mess  = JSON.parse(message);
    db.collection(nameCollection).insert(mess, function(err, value){
        if(err){
            console.log("ERRORE : "+err+" erore")
            res.send(500);
        }

        res.json(value);
    });
}

module.exports = {getSensorTypes, addSensorType, addValue}