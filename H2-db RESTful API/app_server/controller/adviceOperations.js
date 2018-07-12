/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */

/** Model Schema collection */
var advice = require('../models/advice');

/** Callback to add an advice related to a particular patient
 * 
 * @throws 200 - OK
 *         500 - Internal Server Error
 */
module.exports.addAdvice = function(req, res, next){
    var message = req.param('message');
   
    var mess  = JSON.parse("{"+message+"}");
    
    advice.create(mess, function(err, value){
        if(err){
            res.send(500);
        }else {
            res.send(200);
        }
    });
};

/** Callback to return all advices or a range of advices related to a specific patient
 * 
 *  @throws 500 - Internal Server Error
 */
module.exports.getAdvices = function(req, res, next){
    var idCode = req.param('idCode');
    var start = req.param('start');
    var end = req.param('end');
    
    if(start == undefined){
        advice.find({'patientId': idCode}, {'_id':0, 'patientId':0}, function(err, value){
            if(err){
                res.send(500);
            } else {
                res.json(value);
            }
        });
    }else{
        var enddate;
        var startdate = new Date(start);
        end == undefined ? enddate = new Date() : enddate = new Date(end);
    
        advice.find({'patientId': idCode, "timestamp": { $gte: startdate, $lt: enddate}},{"_id":0, "patientId":0},function(err, value){
            if(err){
                res.send(500);
            } else {
                res.json(value);
            }
        });
    }
};