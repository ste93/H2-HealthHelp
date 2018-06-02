var mongoose = require('mongoose');

var sensorSchema = new mongoose.Schema({
  id: {type: String, "default": "00:00:00:00", required: true},
  name : {type: String, "default": "-"},
  patient : {type: String, "default": "-", required: true},
  system : {type: String, "default": "-"},
  value: {type: String, "default": "-", required: true},
  unit : {type: String, "default": "-", required: true}
});

mongoose.model('Sensor', sensorSchema, 'sensors');
