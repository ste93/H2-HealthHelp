var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var patientSchema = new mongoose.Schema({
        _id: {
            type: String,
            required: true
        },
        name: {
            type: String,
            required: true
        },
        surname: {
            type: String,
            required: true
        },
        cf:{
            type: String,
            required: true
        }
    },
    {
        versionKey: false,
        collection: 'patients'
    });

var patients = mongoose.model('patients', patientSchema);

module.exports = patients;