var amqp = require('amqplib/callback_api');
var session = require('client-sessions');

var connection;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});


function requestAdvices(patId, start, end, res) {
    var ex = 'adviceRequest';
    var args = process.argv.slice(2);
    var key = (args.length > 0) ? args[0] : 'datacentre.request.advice';
    var message = '{"patientId":"' + patId 
        + '", "start":"' + start 
        + '", "end":"' + end;
    
        connection.createChannel(function(err, ch) {
            ch.assertExchange(ex, 'topic', {durable: false});
            ch.publish(ex, key, new Buffer(message));
            console.log(" [x] Sent %s:'%s'", key, message);
            res.redirect("/patient/advice");
    });
}

function receiveAdvices (res, idCode){
    var ex = 'adviceRequest';
    var args = process.argv.slice(2);
    var queue = "history";
    var key = (args.length > 0) ? args[0] : ""+session.role+"."+idCode+".receive.advice";
    console.log(key);
    connection.createChannel(function(err, ch) {
        ch.assertExchange(ex, 'topic', {durable: false});
    
        ch.assertQueue('history', {exclusive: false}, function(err, q) {
          console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
          ch.bindQueue(q.queue, ex, key);
    
          ch.consume(q.queue, function(msg) {
                    console.log(" [x] %s", msg.content.toString());
                    res.render('advicesPage', {title: 'Advices', patient: 'patient: '+session.pat, values: ""+msg.content.toString() });
          }, {noAck: true});
        });
      });
    
    
}

function sendNewAdvice(patientID,advice,res){
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


module.exports = {sendNewAdvice, receiveAdvices, requestAdvices};
