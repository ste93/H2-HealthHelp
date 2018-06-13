var amqp = require('amqplib/callback_api');
var session = require('client-sessions');

var connection;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});

function receiveNotificationLevel2 (res, idCode){
    var ex = 'level';
    var args = process.argv.slice(2);
    var queue = "level2.queue";
    var key = (args.length > 0) ? args[0] : "doctor."+idCode+".receive.alert";
    connection.createChannel(function(err, ch) {
        ch.assertExchange(ex, 'topic', {durable: false});
    
        ch.assertQueue('level2.queue', {exclusive: false}, function(err, q) {
            console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
            ch.bindQueue(q.queue, ex, key);
    
            ch.consume(q.queue, function(msg) {
                console.log(" [x] %s", msg.content);
                

            //TODO (notifica)



            }, {noAck: true});
        });
    });  
}

function receiveNotificationLevel3 (res, idCode){
    var ex = 'level';
    var args = process.argv.slice(2);
    var queue = "level3.queue";
    var key = (args.length > 0) ? args[0] : "doctor."+idCode+".receive.emergency";
    console.log(key);
    connection.createChannel(function(err, ch) {
        ch.assertExchange(ex, 'topic', {durable: false});
    
        ch.assertQueue('level3.queue', {exclusive: false}, function(err, q) {
            console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
            ch.bindQueue(q.queue, ex, key);
    
            ch.consume(q.queue, function(msg) {
                console.log(" [x] %s", msg.content);
                

            //TODO (notifica)



            }, {noAck: true});
        });
    });  
}

module.exports = {receiveNotificationLevel2, receiveNotificationLevel3};
