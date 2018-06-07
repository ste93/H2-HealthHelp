var request = require('request');

//use data from ble
var ble = require("../../control_unit_app/ble_controller/ble");

//list of previuosly connected sensors
var mySensorsList = {};

//export the previously connected sensor if it is necessary elsewhere
/*module.exports.getMySensorsList = function() {
  return mySensorsList;
}*/


////////////// Sensor List Page /////////////////////////////////////
var renderSensorHomepage = function(req, res, responseBody){

  var sensorsList = JSON.parse(responseBody);

  //save the list of previously connected sensor retrieved from the server.
  mySensorsList = sensorsList;

  res.render('sensors', {
    title: 'H2 Sensors Manager',
    sensorsList: sensorsList,
    nearSensorsList : [{}]//ble.getNearList()
    //action : 'console.log("doing something");'
  });
};

/* GET sensors page */
module.exports.sensorsHome = function(req, res){
  request.get('http://localhost:3000/api/sensors', function (error, response, body) {
    console.log('error:', error); // Print the error if one occurred
    console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
    renderSensorHomepage(req,res, body);
  });

};
/////////////////////////////////////////////////////

////////////// Detail Page //////////////////////////
var renderSensorDetailPage = function(req, res, responseBody){
  console.log("REsponse : " , responseBody)
  var body = JSON.parse(responseBody);
  console.log("joson : " , body)
  res.render('sensors-details', body );
};

/* GET detail page */
module.exports.sensorDetail = function(req, res){
  request.get('http://localhost:3000/api/sensors/' + req.params.sensorID, function (error, response, body) {
    console.log('error:', error); // Print the error if one occurred
    console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
    console.log('body : ' + body );
    renderSensorDetailPage(req,res, body);
  });
};
/////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////

////////////////// use a sensor //////////////////////////////
// this is an orrible trick to make things work i think....
// execute server side code and then reload the page.
module.exports.sensorConnect = function(req, res){
  //res.render('sensors-connect', sensorConnectParameter);
  console.log("doing something to connect sensors !");
  ble.doSomethingWith(req.params.sensorID);
  console.log("add sensor " + req.params.sensorID  + " to my sensor");
  console.log("remove from near sensor");
  res.redirect("/sensors");
};
/////////////////////////////////////////////////////////////////

////////////// connect new sensor Page //////////////////////////
var renderConnectForm = function( req, res, list) {
  res.render('sensors-connect',  {
    id: req.params.sensorID,
    patientList : JSON.parse(list)
  });
}

module.exports.sensorConnectNew = function(req, res){
  request.get('http://localhost:3000/api/patients', function (error, response, body) {
    console.log('error:', error); // Print the error if one occurred
    console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
    if (body)
      renderConnectForm(req,res, body);
  });

};

module.exports.addNewSensors = function(req, res){
  var postData = {
    id : req.params.sensorID,
    name : req.body.name,
    patient : req.body.patient,
    dataType: req.body.dataType,
    unit: req.body.unit
  }
  console.log(postData);

  request({
  url: 'http://localhost:3000/api/sensors/',
  method: 'POST',
  json: postData
  }, function(error, response, body){
    if (response.statusCode === 201) {
      res.redirect('/sensors/' +  req.params.sensorID);
      //res.redirect('/sensors/');
    } else {
      res.render('error', error);
    }

  });

  //TODO
  //ble.addPairedPeripheral(req.params.sensorID);
};

/////////////////////////////////////////////////////


module.exports.delete = function(req, res){

  request.delete('http://localhost:3000/api/sensors/' + req.params.sensorID, function (error, response, body) {
    console.log('error:', error); // Print the error if one occurred
    console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
    res.redirect('/sensors/');
  });
};
