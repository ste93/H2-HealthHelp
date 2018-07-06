
var session = require('client-sessions');
var constants = require('./constants')
var rabbitMQLibrary = require('./rabbitmqLibrary');

function requestInfo(role, id, res){
    var message = '{"role":"'
    + session.role + '", "id":"'
    + session.user + '"}';
    rabbitMQLibrary.publishToServer(
        constants.datacentreRequestInfoExchangeName,
        constants.exchangeTypeConstant,
        constants.datacentreRequestInfoRoutingKey,
        false,
        message,
        function() {})
    // var ex = 'info';
    // var key = 'datacentre.request.info';
    // var date = new Date().toISOString();

    // connection.createChannel(function(err, ch) {
    //     ch.assertExchange(ex, 'topic', {durable: false});
    //     ch.publish(ex, key, new Buffer(message));
    //     console.log(" [x] Sent %s:'%s'", key, message);
    // });
}

function receiveInfo(res){
    console.log("subscribe to server")
    rabbitMQLibrary.subscribeToServer(
        constants.datacentreReceiveInfoExchangeName,
        constants.exchangeTypeConstant,
        constants.datacentreReceiveInfoQueueName,
        constants.datacentreReceiveInfoRoutingKey,
        false,
        function(msg) {
            console.log(" [x] %s", msg.content);
            if(msg.content.toString() == "500"){
                console.log("500")
                var path = "/" + session.role + "/" + session.user;
                res.redirect(path);
            } else {
                var json = JSON.parse(msg.content.toString()); 
                console.log("json.parse")          
                var personalInfo = {
                    title: 'Personal Information',
                    id : json.idCode,
                    name: json.name,
                    surname: json.surname,
                    mail: json.mail,
                    cf : json.cf,
                    phones: json.phones,
                    role: session.role,
                    user: session.user
                }
                console.log("INFO: " + personalInfo);
                res.render('infoPage', personalInfo);
            }
        });
    // var ex = 'receive.info';
    // var key = 'datacentre.receive.info';
    // var queue = "info";
   
    // connection.createChannel(function(err, ch) {
    //     ch.assertExchange(ex, 'topic', {durable: false});
    //     ch.assertQueue(queue, {exclusive: false}, function(err, q) {
    //         console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
    //         ch.bindQueue(q.queue, ex, key);
    
    //         ch.consume(q.queue, function(msg) {
    //             console.log(" [x] %s", msg.content);doctor/mario.rossi/info
    //             if(msg.content.toString() == "500"){
    //                 var path = "/" + session.role + "/" + session.user;
    //                 res.redirect(path);
    //             } else {
    //                 var json = JSON.parse(msg.content.toString());           
    //                 var personalInfo = {
    //                     title: 'Personal Information',
    //                     id : json.idCode,
    //                     name: json.name,
    //                     surname: json.surname,
    //                     mail: json.mail,
    //                     cf : json.cf,
    //                     phones: json.phones,
    //                     role: session.role,
    //                     user: session.user
    //                 }
    //                 res.render('infoPage', personalInfo);
    //             }
    //         }, {noAck: true});
    //     });
    // });
}

module.exports = {requestInfo, receiveInfo};
