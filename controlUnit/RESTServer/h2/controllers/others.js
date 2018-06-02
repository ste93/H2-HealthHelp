
// about page controller

var textParameter = {
  title: 'About Page of H2 Project',
  content : 'H2 project born as university project for course "Laboratorio di sistemi software" and "Pervasive Computing" from master s degree in Ingegneria e Scienze informatiche by Unibo @ Cesena Campus <br /><br /> This project want to help chronically hill patients to measure and monitor vital parameter and mantain them consantly monitored and connected to their doctors. <br /><br /> This part help to manage the sensors to measure the vital parameter. <br /><br />',
  otherContent: "The H2 Team is composed by Stefano B., Manuel B., Giulia L. and Margherita P. "
}


module.exports.about = function(req, res){
  //  render() is an express function.
  //  the first parameter is the name of jade file in folder 'views' to be rendered
  //  the second is the parameter used in the jade template
  res.render('generic-text', textParameter);
};
