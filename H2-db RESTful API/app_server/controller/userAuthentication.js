/** RESTful API H2db
 * @author Giulia Lucchi
 * @author Margherita Pecorelli
 */


/** Model scheme of collections */
var doctors = require('../models/doctorData');
var patients = require('../models/patientData');

var users = null;



 /** Callback to sign up a new user in H2 application.
 * 
 * @private 
 * @throws 200 - OK
 *         400 - BAD REQUEST(missing parameters or wrong idCode)
 *         401 - UNAUTHORIZED (role is not valid)
 */
module.exports.register = function(req, res){
    var role = req.param('role');
    var idCode = req.param('idCode');
    var password = req.param('password');
    var name = req.param('name');
    var surname = req.param('surname');
    var cf = req.param('cf');
    var phone = req.param('phone');
    var mail = req.param('mail');
    
    var phoneArray = phone.split(" ");
    
    var user = {
        "idCode": idCode,
        "password": password,
        "name" : name,
        "surname": surname,
        "cf" : cf,
        "phone": phoneArray,
        "mail": mail
    };

    _setCollection(role);
    if(users == null){
        res.send(404);
        console.log("Not Authorized to register in this software.");  
    } else {
        users.create(user, function(err, user){
            if(err){
                //console.log('user registation error - %s', err);
                return res.send(400);
            } else {
                res.send(200);
            }
       });
       users = null;
    }
    
};

/** Callback to singn in a user already registered in H2 application
 * 
 * @private
 * @throws 200 - OK
 *         400 - BAD REQUEST(missing or wrong parameters)
 *         401 - UNAUTHORIZED (role is not valid) 
 */
module.exports.login = function(req, res){
    var idCode = req.param('idCode');
    var role = req.param('role');
    var password = req.param('password');

    _setCollection(role);
    if(users == null){
        res.send(401);
        console.log("Not Authorized to enter in this software.");  
    } else {
        users.findOne({"idCode": idCode, "password": password},{"_id":0, "password":0, "phone":0, "mail":0, "cf":0}, function(err, user){
            if(err){
                //console.log('user login error - %s', err);
                return res.send(400);
            } else if(user == null) {
                res.send(404);
            }else{
                res.send(200);
            }
       });
       users = null;
    }
};

 /** Private function to set the right user's collection
 * 
 * @param {String} role - user's role: patient or doctor
 */
function _setCollection(role){
    if(role == "doctor"){
        users = doctors;
    } else if(role == "patient"){
        users = patients;
    }
}

