var amqp = require('amqplib/callback_api');
var session = require('client-sessions');
var constants = require('./constants')
var rabbitMQLibrary = require('./rabbitmqLibrary');

// var connection;

// amqp.connect(constants.amqpAddress, function(err, conn) {
//     connection = conn;
// });

function requestAdvices(patId, start, end, res) {
    var message = '{"patientId":"' + patId 
    + '", "start":"' + start 
    + '", "end":"' + end 
    + '"}';
    rabbitMQLibrary.publishToServer(constants.adviceRequestExchangeName,
                                    constants.exchangeTypeConstant,
                                    constants.adviceRoutingKey,
                                    false, 
                                    message,
                                    function() {
                                        res.redirect("/patient/" + session.user + "/advice");
                                    });


    // )
    // var ex = 'adviceRequest';
    // var key = 'datacentre.request.advice';

    
    // connection.createChannel(function(err, ch) {
    //     ch.assertExchange(ex, 'topic', {durable: false});
    //     ch.publish(ex, key, new Buffer(message));
    //     console.log(" [x] Sent %s:'%s'", key, message);
    // });
}

function receiveAdvices (res, idCode){
    rabbitMQLibrary.subscribeToServer(
        constants.adviceRequestExchangeName,
        constants.exchangeTypeConstant,
        constants.adviceReceiveQueueName,
        session.role+"."+idCode+".receive.advice",
        false,
        function(msg) {
            console.log(" [x] %s", msg.content);
            if(msg.content.toString() == "[500]"){
                var path = "/" + session.role + "/" + session.user;
                res.redirect(path);
            } else {
                var message = msg.content;
                
                var advices = {
                    role: session.role,
                    user: session.user,
                    title: 'Advices',
                    patient: 'patient: ' + session.pat, 
                    values: "" + msg.content.toString(),
                    values: message
                }
                res.render('advicesPage', advices);
            }
        });
    // var ex = 'adviceRequest';
    // var queue = "advice";
    // var key = session.role+"."+idCode+".receive.advice";
    // console.log(key);
    // connection.createChannel(function(err, ch) {
    //     ch.assertExchange(ex, 'topic', {durable: false});
    
    //     ch.assertQueue('advice', {exclusive: false}, function(err, q) {
    //       console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
    //       ch.bindQueue(q.queue, ex, key);
    
    //         ch.consume(q.queue, function(msg) {
    //             console.log(" [x] %s", msg.content);
    //             if(msg.content.toString() == "[500]"){
    //                 var path = "/" + session.role + "/" + session.user;
    //                 res.redirect(path);
    //             } else {
    //                 var message = msg.content;
                    
    //                 var advices = {
    //                     role: session.role,
    //                     user: session.user,
    //                     title: 'Advices',
    //                     patient: 'patient: ' + session.pat, 
    //                     values: "" + msg.content.toString(),
    //                     values: message
    //                 }
    //                 res.render('advicesPage', advices);
    //             }
    //         }, {noAck: true});
    //     });
    // });  
}

function sendNewAdvice(patientID,advice,res){
    var date = new Date().toISOString();
    var message = '{ "patientId":"'
                    + patientID + '", "doctorId":"'
                    + session.user + '", "advice":"'
                    + advice +'", "timestamp":"'
                    + date
                    + '"}';
    rabbitMQLibrary.publishToServer(
        constants.advicePublishExchangeName,
        constants.exchangeTypeConstant,
        constants.advicePublishRoutingKey,
        false,
        message,
        function() {
            res.redirect("/doctor/" + session.user);
        });
        console.log("sendAdvice")
    // ex = 'advice';
    // key = 'datacentre.receive.advice';
    // var date = new Date().toISOString();
    // var message = '{ "patientId":"'
    //                 + patientID + '", "doctorId":"'
    //                 + session.user + '", "advice":"'
    //                 + advice +'", "timestamp":"'
    //                 + date
    //                 + '"}';
    // connection.createChannel(function(err, ch) {
    //     ch.assertExchange(ex, 'topic', {durable: false});
    //     ch.publish(ex, key, new Buffer(message));
    //     console.log(" [x] Sent %s:'%s'", key, message);
    //     res.redirect("/doctor/" + session.user);
    // });   
}

module.exports = {sendNewAdvice, receiveAdvices, requestAdvices};
