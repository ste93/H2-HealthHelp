
var socketIo;
//var sockets = {};
//var connection;
var connections = {};
var httpServerLocal;

function initialization (httpServer, callback) {
   httpServerLocal = httpServer;
   socketIo = require('socket.io')(httpServerLocal)
   createSocket(callback);
}

function sendMessagesToUser(user, message) {
    console.log(connections)
    for (var index in connections[user]) {
        console.log("user: " + user)
        var socketName = connections[user][index];
        if (socketName) {
            console.log(socketName)
            socketIo.of('/').connected[socketName].send(message);
        }
    }
}


function createSocket(callback) {
    socketIo.on('connection', function(connection) {
        connection.on('message', function(userId) {
            console.log("connection: " + userId + " " + connection.id)
            if (!(userId in connections)) {
                connections[userId] = new Array();
            }
            connections[userId].push(connection.id);
            var res;
            callback(res, userId);
            connection.on('disconnect', function() {
                var i = connections[userId].indexOf(connection.id);
                connections[userId].splice(i, 1);
            });
        });
    });
}



module.exports = {initialization, sendMessagesToUser}