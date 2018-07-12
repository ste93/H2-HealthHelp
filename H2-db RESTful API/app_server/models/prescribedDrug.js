var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var drugSchema = Schema({
        doctorId: {
            type: String,
            required: true
        },
        timestamp: {
            type: Date,
            required: true
        },
        drugName:{
            type: String,
            required: true
        }
    },
    {
        versionKey: false
    });



module.exports = drugSchema;