// var socketIo;

// function initialization (httpServer, res) {
//     socketIo = require('socket.io')(httpServer);
//     socketIo.on('connection', function(socket) {
//         console.log('A user connected');
//         setTimeout(function () {
//            socket.send('Sent a message 4 seconds after connection!');
//         }, 4000);
     
//         //Whenever someone disconnects this piece of code executed
//         socket.on('disconnect', function () {
//            console.log('A user disconnected');
//         });
//     });
// }

// module.exports = {initialization}