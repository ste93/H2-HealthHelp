var amqp = require('amqplib/callback_api');
var session = require("client-sessions");



var connection;
var ex;
var args;
var key;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});


function getDataHistory (type, patId, start, end, requester ,res){
    ex = 'historyRequest';
    args = process.argv.slice(2);
    key = (args.length > 0) ? args[0] : 'datacentre.request.history';
    var message = '{"type":"' + type 
                    + '", "patientId":"'
                    + patId + '", "start":"'
                    + start + '", "end":"'
                    + end +'", "requesterId":"'
                    + requester + '", "requesterRole": "doctor"}';
    connection.createChannel(function(err, ch) {
            ch.assertExchange(ex, 'topic', {durable: false});
            ch.publish(ex, key, new Buffer(message));
            console.log(" [x] Sent %s:'%s'", key, message);
            res.redirect("/doctor/history");
    });
}

function sendAdvice(patientID,advice,res){
    ex = 'advice';
    args = process.argv.slice(2);
    key = (args.length > 0) ? args[0] : 'datacentre.receive.advice';
    var date = new Date().toISOString();
    var message = '{ "patientId":"'
                    + patientID + '", "doctorId":"'
                    + session.user + '", "advice":"'
                    + advice +'", "timestamp":"'
                    + date+'"}';
    connection.createChannel(function(err, ch) {
            ch.assertExchange(ex, 'topic', {durable: false});
            ch.publish(ex, key, new Buffer(message));
            console.log(" [x] Sent %s:'%s'", key, message);
            res.redirect("/doctor");
    });
    
}

function sendDrug(patientID,advice,res){
    ex = 'advice';
    args = process.argv.slice(2);
    key = (args.length > 0) ? args[0] : 'datacentre.receive.advice';
    var date = new Date().toISOString();
    var message = '{ "patientId":"'
                    + patientID + '", "doctorId":"'
                    + session.user + '", "advice":"'
                    + advice +'", "timestamp":"'
                    + date+'"}';
    connection.createChannel(function(err, ch) {
            ch.assertExchange(ex, 'topic', {durable: false});
            ch.publish(ex, key, new Buffer(message));
            console.log(" [x] Sent %s:'%s'", key, message);
            res.redirect("/doctor");
    });
    
}


module.exports = {getDataHistory, sendAdvice};