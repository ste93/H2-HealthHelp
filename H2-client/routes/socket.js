var subNotificationLevelCallback2
var subNotificationLevelCallback3
var subNotificationAdviceCallback
var contstants = require('./constants')
var session = require('client-sessions')
var socketIo;
var doctorConnections = {};
var patientConnections = {};
var httpServerLocal;
module.exports = {initialization, 
                sendMessageToUsers,
                setNotificationAdviceCallback,
                setNotificationLevelCallback3, 
                setNotificationLevelCallback2}

function setNotificationLevelCallback2(notificationLevelCallback2){ 
    subNotificationLevelCallback2 = notificationLevelCallback2;
}

function setNotificationLevelCallback3(notificationLevelCallback3){ 
    subNotificationLevelCallback3 = notificationLevelCallback3;
}

function setNotificationAdviceCallback(notificationAdviceCallback) {
    subNotificationAdviceCallback = notificationAdviceCallback;
}

function initialization (httpServer) {
   httpServerLocal = httpServer;
   socketIo = require('socket.io')(httpServerLocal)
   setOnConnection();
}

function sendMessageToUsers(user, message, roleSender) {
    console.log(user + " " + message  + " " + roleSender)
    var connections;
    if (roleSender === contstants.doctorRoleConstant) {
        connections = doctorConnections
    } else if (roleSender === contstants.patientRoleConstant) {
        connections = patientConnections 
    }
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


function setOnConnection() {
    socketIo.on('connection', function(connection) {
        console.log("connecting " + session.user + " " + connection.id + session.role)
        if (session.role === contstants.patientRoleConstant){
            createSession(connection, patientConnections, session.user, function(userId) {
                subNotificationAdviceCallback(userId)
            });
        
        } else if (session.role === contstants.doctorRoleConstant) {
            createSession(connection, doctorConnections, session.user, function(userId) {
                subNotificationLevelCallback2(userId)
                subNotificationLevelCallback3(userId)
            });
        }
    });

}


function createSession(connection, connectionsArray, user, callback) {
    if (!(session.user in connectionsArray)) {
        connectionsArray[user] = new Array();
    }
    connectionsArray[user].push(connection.id);
    callback(user)
    connection.on('disconnect', function() {
        var i = connectionsArray[user].indexOf(connection.id);
        connectionsArray[user].splice(i, 1);
    });
}


