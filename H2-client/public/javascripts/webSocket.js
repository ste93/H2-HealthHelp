setTimeout(function(){ $('#doctorModal').modal("show")}, 1000);
var webSocket = io();
webSocket.send(document.getElementById("user").innerHTML)
var json;
webSocket.on('message', function(data) {
  console.log(JSON.parse(data).message)
  $('#doctorModal').modal("show")
});

function addEmergency(text) {
  var emergencyNode = document.createElement('div')
  //emergencyNode.id = 
  document.getElementById("emergencyNotifications").appendChild(emergency) 
}