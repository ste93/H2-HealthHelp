var session = require('client-sessions');
var constants = require('./constants')
var rabbitMQLibrary = require('./rabbitmqLibrary');

function requestDrugs (patId, start, end, res){
    var message = '{"patientId":"' + patId 
    + '", "start":"' + start 
    + '", "end":"' + end
    + '"}';
    rabbitMQLibrary.publishToServer(constants.drugRequestExchangeName,
                                    constants.exchangeTypeConstant,
                                    constants.datacentreDrugRequestRoutingKey,
                                    false,
                                    message, 
                                    function(){
                                        res.redirect("/patient/" + session.user + "/drug");
                                    });

    
    // connection.createChannel(function(err, ch) {
    //     ch.assertExchange(ex, 'topic', {durable: false});
    //     ch.publish(ex, key, new Buffer(message));
    //     console.log(" [x] Sent %s:'%s'", key, message);
    // });
}

function receiveDrugs(res, idCode){
    rabbitMQLibrary.subscribeToServer(
        constants.drugRequestExchangeName,
        constants.exchangeTypeConstant, 
        constants.drugRequestQueueName,
        session.role+"."+idCode+".receive.drug",
        false, 
        function(msg) {
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
        });
    // var ex = 'drugRequest';
    // var key = ""+session.role+"."+idCode+".receive.drug";
    // console.log(key);
    // connection.createChannel(function(err, ch) {
    //     ch.assertExchange(ex, 'topic', {durable: false});
    
    //     ch.assertQueue('drug', {exclusive: false}, function(err, q) {
    //         console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
    //         ch.bindQueue(q.queue, ex, key);
    
    //         ch.consume(q.queue, function(msg) {
    //             console.log(" [x] %s", msg.content);
    //             if(msg.content.toString() == "[500]"){
    //                 var path = "/" + session.role + "/" + session.user;
    //                 res.redirect(path);
    //             } else {        
    //                 var message = msg.content;
                    
    //                 var drugs = {
    //                     role: session.role,
    //                     user: session.user,
    //                     title: 'Drugs',
    //                     patient: 'patient: ' + session.pat, 
    //                     values: "" + msg.content.toString(),
    //                     values: message
    //                 }
    //                 res.render('drugsPage', drugs);
    //             }
    //         }, {noAck: true});
    //     });
    // });
}

function sendNewDrug(patientID,drug,res){
    var date = new Date().toISOString();
    var message = '{ "patientId":"'
    + patientID + '", "message": { "doctorId":"'
    + session.user + '", "timestamp":"'
    + date +'", "drugName":"'
    + drug
    + '"}}';
    rabbitMQLibrary.publishToServer(
        constants.newDrugSendExchangeName,
        constants.exchangeTypeConstant,
        constants.newDrugSendRoutingKey,
        false,
        message,
        function() {
            res.redirect("/doctor/" + session.user);
        });
    // var ex = 'drug';
    // var key = 'datacentre.receive.drug';
    // var date = new Date().toISOString();
    // connection.createChannel(function(err, ch) {
    //         ch.assertExchange(ex, 'topic', {durable: false});
    //         ch.publish(ex, key, new Buffer(message));
    //         console.log(" [x] Sent %s:'%s'", key, message);
    // });
    
}

module.exports = {sendNewDrug, requestDrugs, receiveDrugs};
