var session = require('client-sessions');
var Client = require('node-rest-client').Client;
var client = new Client();

module.exports.getHome = function(req, res){
    var homeParameter = {
        title: 'H2 - Login'
      }
    res.render('index', homeParameter);
}

module.exports.login = function(req, res){
    var loginArgs = {
        path: { "username": req.body.username, "password": req.body.password, "role": req.body.role }	
    };
    
    client.get("http://localhost:3000/database/application/login?idCode=${username}&role=${role}&password=${password}", loginArgs,
    function (data, response) {
        if(response.statusCode == 200){
            session.user = req.body.username;
            session.role = req.body.role;
            var arr = session.user.replace(".", " ").split(/\d/, 1)[0].split(" ");
            session.userFirstName = arr[0].charAt(0).toUpperCase() + arr[0].slice(1);
            session.userSurname = arr[1].charAt(0).toUpperCase() + arr[1].slice(1);
            res.redirect("/"+req.body.role+"/"+req.body.username);
        }else{
            res.redirect("/");
        } 
    }); 
}