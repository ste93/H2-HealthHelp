var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var sensorDataSchema = Schema({
        patientId: {
            type: String,
            required: true
        },
        dataType: {
            type: String,
            required: true
        },
        value: {
            type: Number,
            required: true
        },
        unit:{
            type: String,
            required: true
        },
        timestamp:{
            type: Date,
            required: true
        },
        output:{
            level:{
                type: Number,
                required: true
            },
            description:{
                type: String,
                required: true
            }
        }
    },
    {
        versionKey: false,
        collection: 'sensorData'
    });

var sensorData = mongoose.model('sensorData', sensorDataSchema);

module.exports = sensorData;