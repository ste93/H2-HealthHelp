var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var doctor = Schema({
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
            type: String
        }
    },
    {
        versionKey: false,
        collection: 'doctors'
    });

var doctor = mongoose.model('doctors', doctor);

module.exports = doctor;