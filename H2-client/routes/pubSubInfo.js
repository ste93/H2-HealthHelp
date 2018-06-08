var amqp = require('amqplib/callback_api');
var session = require('client-sessions');

var connection;
var ex;
var args;
var key;
var queue;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});


function requestInfo(role, id, res){
    ex = 'info';
    args = process.argv.slice(2);
    key = (args.length > 0) ? args[0] : 'datacentre.request.info';
    var date = new Date().toISOString();
    var message = '{ "role":"'
                    + session.role + '", "id":"'
                    + session.user + '}';
    console.log( message);
    connection.createChannel(function(err, ch) {
            ch.assertExchange(ex, 'topic', {durable: false});
            ch.publish(ex, key, new Buffer(message));
            console.log(" [x] Sent %s:'%s'", key, message);
            res.redirect("/doctor/info");
    });
}


module.exports = {requestInfo};
