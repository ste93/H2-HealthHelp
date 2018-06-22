   var webSocket = io();
    webSocket.send(document.title.split(" ").splice(1,3).join("."))
    webSocket.on('message', function(data) {
      alert(data);
    });