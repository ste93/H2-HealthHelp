var amqp = require('amqplib/callback_api');
var session = require('client-sessions');

var connection;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});

function receiveNotificationAdvice (res, idCode){
    var ex = 'advice';
    var args = process.argv.slice(2);
    var queue = "advice.queue";
    var key = (args.length > 0) ? args[0] : ""+session.role+"."+idCode+".receive.advice";
    connection.createChannel(function(err, ch) {
        ch.assertExchange(ex, 'topic', {durable: false});
        
        ch.assertQueue('advice.queue', {exclusive: false}, function(err, q) {
            console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
            ch.bindQueue(q.queue, ex, key);
            
            ch.consume(q.queue, function(msg) {
                console.log(" [x] %s", msg.content);
                

            //TODO (notifica)



            }, {noAck: true});
        });
    });  
}

module.exports = {receiveNotificationAdvice};
