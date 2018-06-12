var request = require('request');

//use data from ble
var ble = require("../../control_unit_app/ble_controller/ble");

//local list of previuosly connected sensors
var mySensorsList = {};

//export the previously connected sensor if it is necessary elsewhere
/*module.exports.getMySensorsList = function() {
  return mySensorsList;
}*/

// use the hardware LCD.
// TODO : Inserire le stampe se lo vogliamo usare.
var lcd = require('../../control_unit_app/hardware/controlUnitLCD');
lcd.initializeLcd()


////////////// Sensor List Page ////////////////////////////////////////////////
/**
* Express function to render sensors management homepage.
* @param req Is the HTTP Request. Not used by this function.
* @param res Is the HTTP Response, returned to the requesting host with the result.
* @param sensorsList is a JSON list with the data of all the sensors registered to the current control unit.
* Accepted list must have format like : [{"sensorId" : String, "value": {"name" : String, "patientID" : String, "datatype" : String, "unit" : String } }, ...]
*/
var renderSensorHomepage = function(req, res, sensorsList){

  //save the list of previously connected sensor retrieved from the server.
  mySensorsList = sensorsList;

  //retrieve from BLE module the list of near devices.
  var nearBLEDeviceList = ble.getNearList();

  res.render('sensors', {
    title: 'H2 Sensors Manager',
    sensorsList: sensorsList,
    nearSensorsList : nearBLEDeviceList
  });
};

/**
* GET main sensors management page
* @param req Is the HTTP Request. Not used by this function.
* @param res Is the HTTP Response, returned to the requesting host with the rendered page.
*/
module.exports.sensorsHome = function(req, res){
  // request to the REST Service the list of the previously connected sensors.
  request.get('http://localhost:3000/api/sensors', function (error, response, body) {
    if (error)
      console.log('error:', error);
    console.log('statusCode:', response && response.statusCode);
    if(body){
      var sensorsList = JSON.parse(body);
      renderSensorHomepage(req,res, sensorsList);
    }
    //TODO: GESTIONE DEGLI ERRORI -> RENDER PAGINA ERRORE
  });

};
////////////////////////////////////////////////////////////////////////////////

////////////// Details Page ////////////////////////////////////////////////////
var renderSensorDetailPage = function(req, res, sensorDetails){

  res.render('sensors-details', sensorDetails );
};

/* GET detail page */
module.exports.sensorDetail = function(req, res){
  request.get('http://localhost:3000/api/sensors/' + req.params.sensorID, function (error, response, body) {
    if (error)
      console.log('error:', error);
    console.log('statusCode:', response && response.statusCode);
    //console.log('body : ' + body );
    if(body){
      //console.log("REsponse : " , responseBody)
      var sensorDetails = JSON.parse(body);
      //console.log("joson : " , body)
      renderSensorDetailPage(req,res, sensorDetails);
    }
    //TODO: GESTIONE DEGLI ERRORI -> RENDER PAGINA ERRORE

  });
};
////////////////////////////////////////////////////////////////////////////////

////////////////// use a sensor ////////////////////////////////////////////////
//TODO: QUESTO NON LO USIAMO MAI; SI PUç ANCHE TOGLIERE.
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
////////////////////////////////////////////////////////////////////////////////

////////////// connect new sensor Page /////////////////////////////////////////
/**
* Express function to render sensor addition form.
* @param req Is the HTTP Request. Not used by this function.
* @param res Is the HTTP Response, returned to the requesting host with the rendered page.
* @param patientList is a JSON list with the data of all the patient registered to the current control unit.
* Accepted list must have format like : [{"id" : String, "name": String}, ...]
*/
var renderConnectForm = function( req, res, patientList) {
  res.render('sensors-connect',  {
    id: req.params.sensorID,
    patientList : JSON.parse(patientList)
  });
}

/**
* GET form for new sensor connection.
*/
module.exports.sensorConnectNew = function(req, res){
  // Request patients data to the REST service.
  request.get('http://localhost:3000/api/patients', function (error, response, body) {
    if(error)
      console.log('error:', error); // Print the error if one occurred
    console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
    if (body){
      renderConnectForm(req,res, body);
    } //TODO: GESTIONE DEGLI ERRORI -> RENDER PAGINA ERRORE
  });

};

/**
* POST request for new sensor registration. Invocked by default when the sensor's form is submitted.
* @param req Is the HTTP POST Request. Contain the data insered by user in the form.
* @param res Is the HTTP Response, returned to the requesting host with the rendered page.
*/
module.exports.addNewSensors = function(req, res){
  // retrieve data from the submitted form.
  var postData = {
    id : req.params.sensorID,
    name : req.body.name,
    patient : req.body.patient,
    dataType: req.body.dataType,
    unit: req.body.unit
  }
  console.log(postData);

  // Send a POST request the the REST service in order to add the new registered patient.
  request({
    url: 'http://localhost:3000/api/sensors/',
    method: 'POST',
    json: postData
    }, function(error, response, body){
      if (response.statusCode === 201) {
        res.redirect('/sensors/' +  req.params.sensorID);
      } else {
        res.render('error', error);
      }
  });

  //TODO
  //ble.addPairedPeripheral(req.params.sensorID);
};

////////////////////////////////////////////////////////////////////////////////

/////////////////////////////// Delete sensor //////////////////////////////////
/**
* DELETE request for a specific sensor.
* @param req Is the HTTP Request. Used to retrieve the sensorID of the sensor to be deleted.
* @param res Is the HTTP Response, returned to the requesting host with the rendered page.
*/
module.exports.delete = function(req, res){
  // DELETE request to the REST service.
  request.delete('http://localhost:3000/api/sensors/' + req.params.sensorID, function (error, response, body) {
    if(error)
      console.log('error:', error);
    console.log('statusCode:', response && response.statusCode);
    // return to the main sensors page after deletion.
    res.redirect('/sensors/');
  });
};

////////////////////////////////////////////////////////////////////////////////
