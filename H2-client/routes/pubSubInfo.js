var amqp = require('amqplib/callback_api');
var session = require('client-sessions');
var jsontype = require('jsontype');
   
var connection;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});


function requestInfo(role, id, res){
    var ex = 'info';
    var args = process.argv.slice(2);
    var key = (args.length > 0) ? args[0] : 'datacentre.request.info';
    var date = new Date().toISOString();
    var message = '{"role":"'
                    + session.role + '", "id":"'
                    + session.user + '"}';
    console.log( message);
    connection.createChannel(function(err, ch) {
            ch.assertExchange(ex, 'topic', {durable: false});
            ch.publish(ex, key, new Buffer(message));
            console.log(" [x] Sent %s:'%s'", key, message);
    });
}

function receiveInfo(res){
    var ex = 'receive.info';
    var args = process.argv.slice(2);
    var key = (args.length > 0) ? args[0] : 'datacentre.receive.info';
    var queue = "info";
   
    connection.createChannel(function(err, ch) {
        ch.assertExchange(ex, 'topic', {durable: false});
    
        ch.assertQueue(queue, {exclusive: false}, function(err, q) {
            console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
            ch.bindQueue(q.queue, ex, key);
    
            ch.consume(q.queue, function(msg) {
                console.log(" [x] %s", msg.content);
                if(msg.content.toString() == "500"){
                    var path = "/" + session.role;
                    res.redirect(path);
                } else {
                    var json = JSON.parse(msg.content.toString());
                    
                    var personalInfo = {
                        title: 'Personal Information',
                        id : json.idCode,
                        name: json.name,
                        surname: json.surname,
                        mail: json.mail,
                        cf : json.cf,
                        phones: json.phones,
                        role: session.role
                    }
                    res.render('infoPage', personalInfo);
                }
            }, {noAck: true});
        });
    });
}
module.exports = {requestInfo, receiveInfo};
