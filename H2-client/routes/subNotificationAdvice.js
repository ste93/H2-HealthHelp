var session = require('client-sessions');
var webSocket = require('./socket');
var constants = require('./constants')
var rabbitMQLibrary = require('./rabbitmqLibrary');


function receiveNotificationAdvice (idCode){
    console.log("sono arrivato" + idCode)
    rabbitMQLibrary.subscribeToServer(constants.notificationAdviceExchangeName,
                                    constants.exchangeTypeConstant,
                                    constants.notificationAdviceQueueName + "." + idCode,
                                    session.role+"."+idCode+".receive.advice",
                                    false,
                                    function(message) {
                                        console.log("webSocket  " + webSocket)
                                        webSocket.sendMessageToUsers(idCode, message.content.toString(), constants.patientRoleConstant); 
                                    });
}

// function receiveNotificationAdvice (idCode){
//     console.log(connection)
//     var ex = 'advice';
//     var queue = "advice.queue";
//     var key = session.role+"."+idCode+".receive.advice";
//     connection.createChannel(function(err, ch) {
//         ch.assertExchange(ex, 'topic', {durable: false});
//         ch.assertQueue('advice.queue', {exclusive: false}, function(err, q) {
//             ch.bindQueue(q.queue, ex, key);
//             ch.consume(q.queue, function(message) {
//                 webSocket.sendMessagesToUser(idCode, message.content.toString());
//             }, {noAck: true});
//         });
//     });  
// }

module.exports = {receiveNotificationAdvice};
