var amqp = require('amqplib/callback_api');
var session = require('client-sessions');
var constants = require('./constants')
var rabbitMQLibrary = require('./rabbitmqLibrary');


function requestHistory (type, patId, start, end, requester, res){
    var message = '{"type":"' + type 
    + '", "patientId":"' + patId
    + '", "start":"' + start 
    + '", "end":"' + end 
    + '", "requesterId":"' + requester 
    + '", "requesterRole":"' + session.role
    + '"}';
    rabbitMQLibrary.publishToServer(
        constants.datacentreHistoryRequestExchangeName,
        constants.exchangeTypeConstant,
        constants.datacentreHistoryRequestRoutingKey,
        false,
        message, 
        function() {
            var path = "/" + session.role + "/" + session.user + "/history";
            res.redirect(path);
        }
    )
    // var ex = 'historyRequest';
    // var key = 'datacentre.request.history';

    // connection.createChannel(function(err, ch) {
    //     ch.assertExchange(ex, 'topic', {durable: false});
    //     ch.publish(ex, key, new Buffer(message));
    //     console.log(" [x] Sent %s:'%s'", key, message);

    // });
}

function receiveHistory (res, idCode){
    rabbitMQLibrary.subscribeToServer(
        constants.receiveHistoryExchangeName,
        constants.exchangeTypeConstant,
        constants.receiveHistoryQueueName,
        session.role+"."+idCode+".receive.history",
        false,
        function(msg, channel) {
            console.log(" [x] %s", msg.content.toString());
            if(msg.content.toString() == "[500]"){
                var path = "/" + session.role + "/" + session.user;
                res.redirect(path);
            } else {
                var message = msg.content.toString();
                var infoHistory = {
                    role: session.role,
                    user: session.user,
                    title: 'Data History',
                    patient: 'patient: '+session.pat, 
                    type: "sensor type: "+session.type,
                    values: message
                }
                res.render('historyPage', infoHistory);
            }
            channel.close();
        });
    // var ex = 'historyRequest';
    // var queue = "history";
    // var key = session.role+"."+idCode+".receive.history";
    // console.log(key);
    // connection.createChannel(function(err, ch) {
    //     ch.assertExchange(ex, 'topic', {durable: false});
    
    //     ch.assertQueue('history', {exclusive: false}, function(err, q) {
    //         console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
    //         ch.bindQueue(q.queue, ex, key);
    
    //         ch.consume(q.queue, function(msg) {
    //             console.log(" [x] %s", msg.content.toString());
    //             if(msg.content.toString() == "[500]"){
    //                 var path = "/" + session.role + "/" + session.user;
    //                 res.redirect(path);
    //             } else {
    //                 var message = msg.content;
    //                 var infoHistory = {
    //                     role: session.role,
    //                     user: session.user,
    //                     title: 'Data History',
    //                     patient: 'patient: '+session.pat, 
    //                     type: "sensor type: "+session.type,
    //                     values: message
    //                 }
    //                 res.render('historyPage', infoHistory);
    //             }
    //         }, {noAck: true});
    //     });
    // });
}

module.exports = {receiveHistory, requestHistory};
