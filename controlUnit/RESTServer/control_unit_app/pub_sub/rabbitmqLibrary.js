//for the rabbitmq library
var amqp = require('amqplib/callback_api');
var amqpConnection = null;
//var serverURL = "amqp://admin:exchange@192.168.2.42:5672";
//for the connection outside local network
var serverURL = "amqp://admin:exchange@213.209.230.94:8088";
var dataSavedLocally = null;


module.exports.connectToServer = function start() {
  amqp.connect(serverURL, function(error, connection) {
    console.log('amqp trying to connect');
    if (error) {
      console.error("[AMQP connection]", error.message);
      return setTimeout(start, 1000, serverURL);
    }
    connection.on("error", function(error) {
      if (error.message !== "Connection closing") {
        console.error("[AMQP] conn error", error.message);
      }
    });
    connection.on("close", function() {
      console.error("[AMQP] reconnecting");
      return setTimeout(start, 1000, serverURL);
    });
    console.log("[AMQP] connected");
    amqpConnection = connection;
    //this function is called when the server is connected
    //whenConnected();
    //TODO here I send the data saved locally
  });
}

module.exports.publishToServer = function(exchangeName, exchangeType, routingKey, isDurable, message) {
  if(amqpConnection) {
    amqpConnection.createChannel(function(error, channel) {
      if(error) {
        console.log('[AMQP] error creating channel');
        //return setTimeout(start, 1000, serverURL);
        //here how many times I try the createchannel??
        //TODO save data locally
      } else {
        channel.assertExchange(exchangeName, exchangeType, {durable: isDurable});
        // Note: on Node 6 Buffer.from(msg) should be used
        channel.publish(exchangeName, routingKey, new Buffer(message));
        console.log(" [x] Sent " + message);
      }
    });
  }
  else {
    //TODO save data locally
    connectToServer()
  }
}
