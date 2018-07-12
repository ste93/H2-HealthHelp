
// RabbitMQ Publisher that send message when a new data is received from a sensor.

var rabbitMQ = require('../pub_sub/rabbitmqLibrary');
var exchangeName = 'patientData';
var routingKey = 'datacentre.receive.patientdata';
var exchangeType = 'topic'
var isDurable = false;

var connected = false;

/**
* Connect to the remote server.
*/
function connect(callback) {
  rabbitMQ.connectToServer(callback);
  connected = true;
}

/**
* Publish the message to the remote exchange.
*/
module.exports.publishMessage = function (data) {
  console.log("Connected ? " , connected);
  if (connected){
    rabbitMQ.publishToServer(exchangeName, exchangeType, routingKey, isDurable, data);
  } else {
    connect(function() {rabbitMQ.publishToServer(exchangeName, exchangeType, routingKey, isDurable, data);});
  }
}
