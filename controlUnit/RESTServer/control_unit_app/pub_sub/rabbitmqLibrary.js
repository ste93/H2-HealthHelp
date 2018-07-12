//RabbitMQ Publish-Subscribe utility functions.

var amqp = require('amqplib/callback_api');
var amqpConnection = null;

////////////////////////////////////////////////////////////////////////////////
//for the connection in local network
//var serverURL = "amqp://admin:exchange@192.168.2.42:5672";

//for the connection outside local network
var serverURL = "amqp://admin:exchange@213.209.230.94:8088";
////////////////////////////////////////////////////////////////////////////////

var localDataFileManager = require('../data_manager/unsent_data_manager');

/**
* function that try to connect to the remote RabbitMQ server.
*/
module.exports.connectToServer = function(callback) {
  start(callback);
}

/**
* function that try to connect to the RabbitMQ server and initialize the module.
*/
function start(callback) {
  amqp.connect(serverURL, function(error, connection) {

    if (error) {
      console.error("[AMQP connection]", error.message);
      return setTimeout(start, 1000, serverURL);
    }

    connection.on("error", function(error) {
      if (error.message !== "Connection closing") {
     //   console.error("[AMQP] conn error", error.message);
      }
    });

    connection.on("close", function() {
      console.error("[AMQP] reconnecting");
      return setTimeout(start, 1000, serverURL);
    });

    // Now it's connected to the rabbitMQ server.
    console.log("[AMQP] connected");
    amqpConnection = connection;
    whenConnected(callback);
  });
}

function whenConnected(callback){

  // Try to send locally stored messages never sent to the server.
  var exchangeName = 'patientData';
  var routingKey = 'datacentre.receive.patientdata';
  var exchangeType = 'topic'
  var isDurable = false;

  // Retrive data stored locally when connection is missing.
  localDataFileManager.getDataList( function(error, previousDataList) {
    if (error) {
      console.log(error);
    } else {
      previousDataList.forEach(function(element){
        publish(exchangeName, exchangeType, routingKey, isDurable, element);
      });
      // Once sent data is deleted.
      localDataFileManager.clearList();
    }
  });

  callback();
}

/**
* Function to publish a message to a remote RabbitMQ server.
* @param exchangeName is the name of the RabbitMQ exchange in which the message has to be published.
* @param exchangeType is the type of the exchange( e.g. topic).
* @param routingKey is the key used to route and identify the message inside the exchange.
* @param isDurable set the durability of the messages on the queue.
* @param message is the message to be sent.
*/
function publish (exchangeName, exchangeType, routingKey, isDurable, message) {
  if(amqpConnection) {
    amqpConnection.createChannel(function(error, channel) {
      if(error) {
        console.log('[AMQP] Error creating channel');
        //return setTimeout(start, 1000, serverURL);
        console.log('[AMQP] Storing data locally');
        localDataFileManager.addData(JSON.parse(message));
      } else {
        // console.log(" [AMQP] Channel created")
        channel.assertExchange(exchangeName, exchangeType, {durable: isDurable});
        console.log(" [AMQP] Exchange created : ", exchangeName )
        // Note: on Node 6 Buffer.from(msg) should be used
        channel.publish(exchangeName, routingKey, new Buffer(message));
        console.log(" [x] Sent :" + message);
      }
    });
  }
  else {
    //TODO save data locally
    console.log("[AMQP] No Connection available - storing data locally");
    localDataFileManager.addData(JSON.parse(message));
  }
}

/**
* Function to publish a message to a remote RabbitMQ server.
* @param exchangeName is the name of the RabbitMQ exchange in which the message has to be published.
* @param exchangeType is the type of the exchange( e.g. topic).
* @param routingKey is the key used to route and identify the message inside the exchange.
* @param isDurable set the durability of the messages on the queue.
* @param message is the message to be sent.
*/
module.exports.publishToServer = function(exchangeName, exchangeType, routingKey, isDurable, message) {
  publish(exchangeName, exchangeType, routingKey, isDurable, message);
}
