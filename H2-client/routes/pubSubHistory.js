var amqp = require('amqplib/callback_api');
var session = require('client-sessions');

var connection;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});


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
                    console.log(" [x] %s", msg.content);
                    if(msg.content.toString() == "500"){
                        res.redirect("/doctor");
                    }
                    var message = msg.content;
                    var infoHistory = {
                        role: session.role,
                        title: 'Data History',
                        patient: 'patient: '+session.pat, 
                        type: "sensor type: "+session.type,
                        values: message
                    }
                    res.render('historyPage', infoHistory);
          }, {noAck: true});
        });
      });
    
    
}

function requestHistory (type, patId, start, end, requester ,res){
    var ex = 'historyRequest';
    var args = process.argv.slice(2);
    var key = (args.length > 0) ? args[0] : 'datacentre.request.history';
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


module.exports = {receiveHistory, requestHistory};
