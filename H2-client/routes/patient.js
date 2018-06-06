var amqp = require('amqplib/callback_api');

var ex;
var args;
var key;
var msg;

function getDataHistory(type, patId, start, end, res) {
    console.log("sono quiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
    console.log(start);
    console.log(end);

    ex = 'historyRequest';
    args = process.argv.slice(2);
    key = (args.length > 0) ? args[0] : 'datacentre.request.history';
    msg = ' {"type": ' + type + ', "patientId": ' + patId + ', "start": ' + start + ', "end": ' + end + ', "requesterId": ' + patId + ', "requesterRole": "patient"}';
    
    amqp.connect('amqp://admin:exchange@213.209.230.94:8088', function(err, conn) {
        conn.createChannel(function(err, ch) {
            ch.assertExchange(ex, 'topic', {durable: false});
            ch.publish(ex, key, new Buffer(msg));
            console.log(" [x] Sent %s:'%s'", key, msg);
        });
    });

   res.redirect("/patient/history");
}

function getAdvices(req, res) {
    res.render('advices', {title: 'Advice'});
}

function getDrugs(req, res) {
    res.render('drugs', {title: 'Drugs'});
}

function getInfo(req, res) {
    res.render('info', {title: 'Info'});
}

module.exports = {getDataHistory, getAdvices, getDrugs, getInfo}