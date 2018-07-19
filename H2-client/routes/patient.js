var session = require('client-sessions');

var pubSubAdvice = require('./pubSubAdvice');
var pubSubHistory = require('./pubSubHistory');
var pubSubInfo = require('./pubSubInfo');
var pubSubDrug = require('./pubSubDrug');


module.exports.getHome = function(req, res){
    var homeParameter = {
        user: session.user,
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    res.render('patientHome', homeParameter);
}

module.exports.makeRequest = function(req, res){
    session.pat = session.user;
    if(req.body.startAdvice != undefined){
        pubSubAdvice.requestAdvices(session.user, req.body.startAdvice, req.body.end, res);
    }else if(req.body.startDrug != undefined){
        pubSubDrug.requestDrugs(session.user, req.body.startDrug, req.body.end, res);
    }else{
        session.type = req.body.type;
        pubSubHistory.requestHistory(req.body.type, session.user, req.body.start, req.body.end, session.user, res);
    }
}

module.exports.getHistory = function(req, res){
    var homeParameter = {
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    pubSubHistory.receiveHistory(res, session.user)
}

module.exports.getAdvices = function(req, res){
    var homeParameter = {
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    pubSubAdvice.receiveAdvices(res, session.user)
}

module.exports.getDrugs = function(req, res){
    var homeParameter = {
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    pubSubDrug.receiveDrugs(res, session.user)
}

module.exports.getInfo = function(req, res){
    pubSubInfo.requestInfo(session.role, session.user, res);
    pubSubInfo.receiveInfo(res);
}