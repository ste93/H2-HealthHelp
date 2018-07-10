var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var associationSchema = new mongoose.Schema({
        idPatient: {
            type: String
        },
        idDoctor: {
            type: String
        }
    },
    {
        versionKey: false,
        collection: 'relationship'
    });

var association = mongoose.model('relationship', associationSchema);

module.exports = association;