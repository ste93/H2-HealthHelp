var advice = require('../models/advice');

/** Add an advice in the DB 
 * 
 * @param message - advice to insert in json format
 * @param res - response of RESTful request
 */
function addAdvice(message, res){
    var mess  = JSON.parse(message);

    advice.create(mess, function(err, value){
        if(err){
            res.send(500);
        }

        res.send(200);
    });
}

/** Returns all advices of a specific patient
 * 
 * @param idCode - patient identifier
 * @param res - response of RESTful request
 */
function getAdvices(idCode, res){
    advice.find({'patientId': idCode}, {'_id':0, 'patientId':0}, function(err, value){
        if(err){
            res.send(500);
        } else {
            res.json(value);
        }
    });
}

module.exports = {addAdvice, getAdvices}