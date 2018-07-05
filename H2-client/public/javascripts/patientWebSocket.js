var webSocket = io();
//non sono sicuro, posso utilizzare la sessione???

webSocket.on('message', function(data) {
  addAdvice(data)
  $('#patientModal').modal("show")

});

function addAdvice(data) {
  var node = createAdviceNotificationNode(data)
  document.getElementById("adviceNotification").appendChild(node) 
}

