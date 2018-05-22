var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var patientSchema = new mongoose.Schema({
        idCode: {
            type: String,
            required: true,
            unique : true
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
                type: String
            },
            required: true
        },
        mail:{
            type:String
        },
        sensors:{
            type: Array,
            items:{
                type: String
            }
        }
    },
    {
        versionKey: false,
        collection: 'patients'
    });

var patients = mongoose.model('patients', patientSchema);

module.exports = patients;