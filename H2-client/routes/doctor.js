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
    res.render("doctorHome", homeParameter);
}

module.exports.makeRequest = function(req, res){
    if(req.body.text != undefined){
        pubSubAdvice.sendNewAdvice(req.body.pat, req.body.text, res);
    }else if(req.body.drug != undefined){
        pubSubDrug.sendNewDrug(req.body.pat, req.body.drug,res);
    }else{
        session.pat = req.body.pat;
        session.type = req.body.type;
        pubSubHistory.requestHistory(req.body.type, req.body.pat, req.body.start, req.body.end, session.user, res);
    }
}

module.exports.getPatientHistory = function(req, res){
    var homeParameter = {
        title: "WELCOME " + session.userFirstName + " " + session.userSurname
    }
    pubSubHistory.receiveHistory(res, session.user);
}

module.exports.getInfo = function(req, res){
    pubSubInfo.requestInfo(session.role, session.user, res);
    pubSubInfo.receiveInfo(res);
}