var rabbitMQ = require('../pub_sub/rabbitmqLibrary');
var exchangeName = 'patientData';
var routingKey = 'datacentre.receive.sensor';
var exchangeType = 'topic'
var isDurable = false;

var connected = false;

function connect() {
  rabbitMQ.connectToServer();
  connected = true;
}

module.exports.publishMessage = function (data) {
  console.log("Connected ? " , connected);
  if (connected){
    //TODO: Non servirebbe un valore di ritorno per sapere se è andato bene o no ?
    rabbitMQ.publishToServer(exchangeName, exchangeType, routingKey, isDurable, data);
  } else {
    connect();
    rabbitMQ.publishToServer(exchangeName, exchangeType, routingKey, isDurable, data);
  }
}
