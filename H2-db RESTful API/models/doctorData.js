var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var doctorSchema = new mongoose.Schema({
        idCode: {
            type: String,
            required: true,
            unique: true
        },
        password:{
            type:String,
            required:true
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
        },
        phone:{
            type: Array,
            items:{
                type: Number
            },
            required: true
        },
        mail:{
            type:String
        }
    },
    {
        versionKey: false,
        collection: 'doctors'
    });

var doctors = mongoose.model('doctors', doctorSchema);

module.exports = doctors;