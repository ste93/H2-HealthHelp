//RabbitMQ Publish-Subscribe utility functions.
var amqp = require('amqplib/callback_api');
var constants = require('./constants')

////////////////////////////////////////////////////////////////////////////////
//for the connection in local network
//var serverURL = "amqp://admin:exchange@192.168.2.42:5672";

//for the connection outside local network
//var serverURL = "amqp://admin:exchange@213.209.230.94:8088";
////////////////////////////////////////////////////////////////////////////////

/**
* function that try to connect to the remote RabbitMQ server.
*/
// module.exports.connectToServer = function(callback) {
//   start(callback);
// }

/**
* function that try to connect to the RabbitMQ server and initialize the module.
*/
function start(callback) {
    amqp.connect(constants.amqpAddress, function(error, connection) {
      if (error) {
        console.log(error)
      } else {
        connection.on("error", function(error) {
          console.log(error)
          if (error.message !== "Connection closing") {
        //   console.error("[AMQP] conn error", error.message);
            //setTimeout(start, 4000, callback())//????????
          }
        });

        connection.on("close", function() {
          console.error("[AMQP] connection closed");
          //return setTimeout(start, 4000, callback());
        });
        if (error) {
          console.error("[AMQP connection]", error.message);
          //return setTimeout(start, 4000, function() {});
        } else {
          // Now it's connected to the rabbitMQ server.
          console.log("[AMQP] connected");
          //amqpConnection = connection;
          callback(connection);
        }
      }
    });
  }



/**
* Function to publish a message to a remote RabbitMQ server.
* @param exchangeName is the name of the RabbitMQ exchange in which the message has to be published.
* @param exchangeType is the type of the exchange( e.g. topic).
* @param routingKey is the key used to route and identify the message inside the exchange.
* @param isDurable set the durability of the messages on the queue.
* @param message is the message to be sent.
*/
function publish (exchangeName, exchangeType, routingKey, isDurable, message, callback, amqpConnection) {
  amqpConnection.createChannel(function(error, channel) {
    if(error) {
      console.log('[AMQP] Error creating channel');
    } else {
      // console.log(" [AMQP] Channel created")
      channel.assertExchange(exchangeName, exchangeType, {durable: isDurable});
      channel.publish(exchangeName, routingKey, new Buffer(message));
      console.log("publishhhhhhhhhhhhhh")
      callback();
    }
  });
}



function subscribe(exchangeName,exchangeType,  queue, routingKey, isDurable, callback, amqpConnection) {

  console.log()
  amqpConnection.createChannel(function(error, ch) {
    if(error) {
      console.log('[AMQP] Error creating channel');
    } else {
      ch.assertExchange(exchangeName, exchangeType, {durable: isDurable});
      ch.assertQueue(queue, {exclusive: false}, function(err, q) {
        if(!err) {
          ch.bindQueue(q.queue, exchangeName, routingKey);
          ch.consume(q.queue, function(message) {
            callback(message);
          }, {noAck: true});
        }
        else {
          console.log(err)
          console.log(exchangeName)
          console.log(queue)
          console.log(routingKey)
        }
      });
    }
  }); 
}


/**
* Function to publish a message to a remote RabbitMQ server.
* @param exchangeName is the name of the RabbitMQ exchange in which the message has to be published.
* @param exchangeType is the type of the exchange( e.g. topic).
* @param routingKey is the key used to route and identify the message inside the exchange.
* @param isDurable set the durability of the messages on the queue.
* @param message is the message to be sent.
*/

module.exports.subscribeToServer = function(exchangeName,exchangeType,  queue, routingKey, isDurable, callback) {
  start(function(amqpConnection) {
    subscribe(exchangeName,exchangeType,  queue, routingKey, isDurable, callback, amqpConnection);
  });
}

module.exports.publishToServer = function(exchangeName, exchangeType, routingKey, isDurable, message, callback) {
  start(function(amqpConnection) {
    publish(exchangeName, exchangeType, routingKey, isDurable, message, callback, amqpConnection);
  });
}
