var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var adviceSchema = Schema({
        patientId: {
            type: String,
            required: true
        },
        doctorId: {
            type: String,
            required: true
        },
        advice: {
            type: String,
            required: true
        },
        timestamp:{
            type: Date,
            required: true
        }
    },
    {
        versionKey: false,
        collection: 'advices'
    });

var advices = mongoose.model('advices', adviceSchema);

module.exports = advices;