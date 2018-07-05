//var session = require('client-sessions');
var webSocket = require('./socket');
var constants = require('./constants')
var rabbitMQLibrary = require('./rabbitmqLibrary');
module.exports = {receiveNotificationLevel2, receiveNotificationLevel3};

function receiveNotificationLevel2(idCode) {
    rabbitMQLibrary.subscribeToServer(constants.notificationLevelExchangeName,
                                    constants.exchangeTypeConstant,
                                    constants.notificationLevel2QueueName,
                                    "doctor."+idCode+".receive.alert",
                                    false,
                                    function(message) {
                                        webSocket.sendMessageToUsers(idCode, message.content.toString(), constants.doctorRoleConstant);
                                    }
                                );

}


function receiveNotificationLevel3(idCode) {
    rabbitMQLibrary.subscribeToServer(constants.notificationLevelExchangeName,
                                    constants.exchangeTypeConstant,
                                    constants.notificationLevel3QueueName,
                                    "doctor."+idCode+".receive.emergency",
                                    false,
                                    function(message) {
                                        webSocket.sendMessageToUsers(idCode, message.content.toString(), constants.doctorRoleConstant);
                                    }
                                );

}

// function receiveNotification (idCode, level){
//     if(!connection) {
//         amqp.connect(constants.amqpAddress, function(err, conn) {
//             connection = conn;
//             receiveNotification(idCode, level);
//         });
//     } else {
//         var exchangeName = 'level';
//         var queue;
//         var routingKey;
//         if (level == 2) {
//             queue = "level2.queue";
//             routingKey = "doctor."+idCode+".receive.alert";    
//         } else {
//             queue = "level3.queue";
//             routingKey = "doctor."+idCode+".receive.emergency";    
//         }
//         connection.createChannel(function(err, ch) {
//             ch.assertExchange(exchangeName, 'topic', {durable: false});
//             ch.assertQueue(queue, {exclusive: false}, function(err, q) {
//                 ch.bindQueue(q.queue, exchangeName, routingKey);
//                 ch.consume(q.queue, function(message) {
//                     webSocket.sendMessagesToUser(idCode, message.content.toString());
//                 }, {noAck: true});
//             });
//         });  
//     }
// }



