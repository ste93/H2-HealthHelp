
var amqp = require('amqplib/callback_api');

var ex = 'patientData';
var args = process.argv.slice(2);
var key = (args.length > 0) ? args[0] : 'datacentre.receive.patientdata';
var msg = ' {"temperature": { "patientID": "giulia.lucchi7",  "value": "37", "unit": "celsius", "timestamp": "2018-04-24 08:00:03", "output": { "level": "3", "description": "febbre" } }}';

module.exports.connectToTopic = function () {

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    conn.createChannel(function(err, ch) {
      ch.assertExchange(ex, 'topic', {durable: false});
      ch.publish(ex, key, new Buffer(msg));
      console.log(" [x] Sent %s:'%s'", key, msg);
    });

    //setTimeout(function() { conn.close();  }, 500);
  });
}

module.exports.publishMessage = function(message) {
  ch.publish(ex, key, new Buffer(msg));
}
