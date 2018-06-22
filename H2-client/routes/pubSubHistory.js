var amqp = require('amqplib/callback_api');
var session = require('client-sessions');

var connection;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});

function requestHistory (type, patId, start, end, requester, res){
    var ex = 'historyRequest';
    var args = process.argv.slice(2);
    var key = (args.length > 0) ? args[0] : 'datacentre.request.history';
    var message = '{"type":"' + type 
        + '", "patientId":"' + patId
        + '", "start":"' + start 
        + '", "end":"' + end 
        + '", "requesterId":"' + requester 
        + '", "requesterRole":"' + session.role
        + '"}';
    connection.createChannel(function(err, ch) {
        ch.assertExchange(ex, 'topic', {durable: false});
        ch.publish(ex, key, new Buffer(message));
        console.log(" [x] Sent %s:'%s'", key, message);
        var path = "/" + session.role + "/" + session.user + "/history";
        res.redirect(path);
    });
}

function receiveHistory (res, idCode){
    var ex = 'historyRequest';
    var args = process.argv.slice(2);
    var queue = "history";
    var key = (args.length > 0) ? args[0] : ""+session.role+"."+idCode+".receive.history";
    console.log(key);
    connection.createChannel(function(err, ch) {
        ch.assertExchange(ex, 'topic', {durable: false});
    
        ch.assertQueue('history', {exclusive: false}, function(err, q) {
            console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
            ch.bindQueue(q.queue, ex, key);
    
            ch.consume(q.queue, function(msg) {
                console.log(" [x] %s", msg.content.toString());
                if(msg.content.toString() == "[500]"){
                    var path = "/" + session.role + "/" + session.user;
                    res.redirect(path);
                } else {
                    var message = msg.content;
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
            }, {noAck: true});
        });
    });
}

module.exports = {receiveHistory, requestHistory};
