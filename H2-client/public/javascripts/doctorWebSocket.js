var webSocket = io();
//non sono sicuro, posso utilizzare la sessione???

webSocket.on('message', function(data) {
  console.log(data)
  if (JSON.parse(data).message.output.level == 2) {
    addAlert(data)
  }
  else {
    addEmergency(data)
  }
  $('#doctorModal').modal("show")
});

function addEmergency(data) {
  var node = createNotificationNode(data)
  document.getElementById("emergencyNotifications").appendChild(node) 
}

function addAlert(data){
  var node = createNotificationNode(data)
  document.getElementById("alertNotifications").appendChild(node) 
}

