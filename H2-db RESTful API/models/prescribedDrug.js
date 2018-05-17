var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var drugSchema = Schema({
        patientId: {
            type: String,
            required: true
        },
        doctorId: {
            type: String,
            required: true
        },
        timestamp: {
            type: Data,
            required: true
        },
        drugName:{
            type: String,
            required: true
        }
    },
    {
        versionKey: false,
        collection: 'drugs'
    });

var drugs = mongoose.model('drugs', drugSchema);

module.exports = drugs;