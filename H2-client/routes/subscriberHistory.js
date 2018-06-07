var amqp = require('amqplib/callback_api');
var popup = require('window-popup');


var connection;
var ex;
var args;
var key;
var queue;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});


function getDataHistory (role, idCode){
    ex = 'historyRequest';
    args = process.argv.slice(2);
    queue = "history";
    key = (args.length > 0) ? args[0] : "doctor"+"."+idCode+".receive.history";
    console.log(key);
    connection.createChannel(function(err, ch) {
        var ex = 'logs';
    
        ch.assertExchange(ex, 'topic', {durable: false});
    
        ch.assertQueue('history', {exclusive: true}, function(err, q) {
          console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
          ch.bindQueue(q.queue, ex, '');
    
          ch.consume(q.queue, function(msg) {
            console.log(" [x] %s", msg.content.toString());
          }, {noAck: true});
        });
      });
    
    
}


module.exports = {getDataHistory};
