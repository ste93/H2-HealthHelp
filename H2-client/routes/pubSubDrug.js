var amqp = require('amqplib/callback_api');
var session = require('client-sessions');

var connection;

amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
    connection = conn;
});

function requestDrugs (patId, start, end, res){
    var ex = 'drugRequest';
    var args = process.argv.slice(2);
    var key = (args.length > 0) ? args[0] : 'datacentre.request.drug';
    var message = '{"patientId":"' + patId 
        + '", "start":"' + start 
        + '", "end":"' + end
        + '"}';
    
    connection.createChannel(function(err, ch) {
        ch.assertExchange(ex, 'topic', {durable: false});
        ch.publish(ex, key, new Buffer(message));
        console.log(" [x] Sent %s:'%s'", key, message);
        res.redirect("/patient/" + session.user + "/drug");
    });
}

function receiveDrugs(res, idCode){
    var ex = 'drugRequest';
    var args = process.argv.slice(2);
    var queue = "drug";
    var key = (args.length > 0) ? args[0] : ""+session.role+"."+idCode+".receive.drug";
    console.log(key);
    connection.createChannel(function(err, ch) {
        ch.assertExchange(ex, 'topic', {durable: false});
    
        ch.assertQueue('drug', {exclusive: false}, function(err, q) {
            console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
            ch.bindQueue(q.queue, ex, key);
    
            ch.consume(q.queue, function(msg) {
                console.log(" [x] %s", msg.content);
                if(msg.content.toString() == "[500]"){
                    var path = "/" + session.role + "/" + session.user;
                    res.redirect(path);
                } else {        
                    var message = msg.content;
                    
                    var drugs = {
                        role: session.role,
                        user: session.user,
                        title: 'Drugs',
                        patient: 'patient: ' + session.pat, 
                        values: "" + msg.content.toString(),
                        values: message
                    }
                    res.render('drugsPage', drugs);
                }
            }, {noAck: true});
        });
    });
}

function sendNewDrug(patientID,drug,res){
    var ex = 'drug';
    var args = process.argv.slice(2);
    var key = (args.length > 0) ? args[0] : 'datacentre.receive.drug';
    var date = new Date().toISOString();
    var message = '{ "patientId":"'
        + patientID + '", "message": { "doctorId":"'
        + session.user + '", "timestamp":"'
        + date +'", "drugName":"'
        + drug
        + '"}}';
    connection.createChannel(function(err, ch) {
            ch.assertExchange(ex, 'topic', {durable: false});
            ch.publish(ex, key, new Buffer(message));
            console.log(" [x] Sent %s:'%s'", key, message);
            res.redirect("/doctor/" + session.user);
    });
    
}

module.exports = {sendNewDrug, requestDrugs, receiveDrugs};
