function createDeleteButton(id){
    var deleteButton = document.createElement("BUTTON");
    var textButton = document.createTextNode("DELETE");
    deleteButton.appendChild(textButton);
    deleteButton.onclick = function() { deleteNode(id) } 
    return deleteButton
}

function createAdviceNotificationNode(data) {
    var node = document.createElement('div');
    node.id = JSON.parse(data).timestamp
    node.appendChild(createTitleParagraph(
        JSON.parse(data).doctorId))
    node.appendChild(createTextParagraph(    
        JSON.parse(data).advice + " " +
        JSON.parse(data).timestamp))
    node.appendChild(createDeleteButton(node.id))
    return node
}
  
function createNotificationNode(data) {
    var node = document.createElement('div');
    node.id = JSON.parse(data).message.patientId + 
             JSON.parse(data).message.timestamp
    node.appendChild(createTitleParagraph(
        JSON.parse(data).message.patientId + " " + 
        JSON.parse(data).type))
    node.appendChild(createTextParagraph(    
        JSON.parse(data).message.output.description + " " +
        JSON.parse(data).message.value + " " +
        JSON.parse(data).message.unit + " " +
        JSON.parse(data).message.timestamp)
    )
    node.appendChild(createDeleteButton(node.id))
    return node
}
  
function createTitleParagraph(text){
    var title = document.createTextNode(text)
    var titleParagraph = document.createElement("h3"); 
    titleParagraph.appendChild(title); 
    return titleParagraph
}
  
function createTextParagraph(text) {
    var messageContent = document.createTextNode(text)
    var documentParagraph = document.createElement("p")
    documentParagraph.appendChild(messageContent)
    return documentParagraph;
}
  
function deleteNode(id){
    document.getElementById(id).remove()
}