var approot = process.env.PWD;

var db = require(approot + '/lib/db');
var encrypt = require(approot + '/lib/encrypt');



exports = module.exports = function (req, res, next) {
	console.log('processing authorization...');
	var session = req.session;
	session.loginRetried = typeof session.loginRetried != 'undefined' ? session.loginRetried : 5;
	if (session.userId) {
		session.loginRetried = 5;
		// res.send(204);
		console.log('in session');
		next();
	} else {
		console.log('out of session');
		var auth = req.headers.authorization;  // auth is in base64(username:password)  so we need to decode the base64
		console.log("Authorization Header is: ", auth);
		
		if(!auth) {     // No Authorization header was passed in so it's the first time the browser hit us
			// Sending a 401 will require authentication, we need to send the 'WWW-Authenticate' to tell them the sort of authentication to use
			// Basic auth is quite literally the easiest and least secure, it simply gives back  base64( username + ":" + password ) from the browser
			res.setHeader('WWW-Authenticate', 'Basic realm="need login"');
			console.log('No authorization found, send 401.');
			res.send(401);
		} else {    // The Authorization was passed in so now we validate it

			var tmp = auth.split(' ');   // Split on a space, the original auth looks like  "Basic Y2hhcmxlczoxMjM0NQ==" and we need the 2nd part

			var buf = new Buffer(tmp[1], 'base64'); // create a buffer and tell it the data coming in is base64
			var plain_auth = buf.toString();	// read it back out as a string

			console.log("Decoded Authorization ", plain_auth);

			// At this point plain_auth = "username:password"

			var creds = plain_auth.split(':');      // split on a ':'
			var username = creds[0];
			var password = creds[1];
			
			
			db.User.find({where: {username: username}}).success(function (result) {
				if (result && result.password == encrypt(password)) {
					// res.send(204);
					session.userId = result.id;
					session.loginRetried = 5;
					console.log('login success!');
					next();
				} else if (session.loginRetried) {
					res.setHeader('WWW-Authenticate', 'Basic realm="Retry left ' +
						session.loginRetried + ' times"');
					session.loginRetried--;
					console.log('login failed. wrong username or password.');
					res.send(401);
				} else {
					console.log('login tried too many times, forbidden!');
					res.send(403);
				}
			});
		}
	}
};