
 var socketIo;
 var connection;
 var connections = {};
 
 function initialization (httpServer) {
    httpServerLocal = httpServer;
    socketIo = require('socket.io')(httpServer);
 }

 function sendMessagesToUser(user, message) {
     for (connection in connections[user]) {
        function sendMessageOverSocket (message) {
            console.log("sending message");
            connection.send(message);
        }
    }
 }

 function setOnConnection(res, user, callback) {

    socketIo.on('connection', function(connection) {
        if (!(user in connections)) {
            connections[user] = new Array();
        }
        connections[user].push(connection);
        connection.on('disconnect', function() {
            console.log('Got disconnect!');
      
            var i = connections[user].indexOf(connection);
            connections[user].splice(i, 1);
         });

         callback(res, user, sendMessageOverSocket);
    });
  
 }

 module.exports = {initialization, setOnConnection, sendMessagesToUser}