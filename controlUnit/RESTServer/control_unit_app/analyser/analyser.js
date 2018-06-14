/**
* Module to analyse patient's parameter locally in order to handle emergency protocol even if the control unit is not connected to the network.
*/

var publisher = require('../pub_sub/controlUnitPublisher');
var request = require('request');
var moment = require('moment');
var emergencyManager = require ('../hardware/emergencyManager');

var sensorsList = [];

/**
* Function to retrieve sensor's information necessary for the analysis.
*/
function getSensorInfo(id, callback) {
  var found = false;
  var sensorInfo = "{}";
  // check if it's already loaded locally :
  sensorsList.forEach(function(element) {
    json = JSON.parse(element)
    if (json.sensorId === id){
      console.log("Found sensor info locally");
      found = true;
      sensorInfo = json;
      callback(JSON.stringify(sensorInfo));
    }
  });

  if (! found){
  // retrieve sensor informations from data storage using REST service.
	  console.log(id);
    request.get('http://localhost:3000/api/sensors/' + id, function (error, response, info) {
      console.log('error:', error); // Print the error if one occurred
      console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
      console.log('body : ' + info );
      if ( info != undefined && info != ''){
        sensorsList.push(info);
        console.log("Found sensor info remotelly");
        sensorInfo = info;
        callback(sensorInfo);
      } else {
        callback(sensorInfo)
      }
    });
  }
}

/**
* Perform simple analysis on the data received from the sensors.
*/
module.exports.analyseData = function (data) {
  console.log("Received data to analyse : " , data);
  var sensorData = data;

  var sensorID = sensorData.sensorID;
  sensorInfo = getSensorInfo(sensorID, function (sensorInfo) {
    if (sensorInfo === '{}'){
      analysisError(sensorData);
    } else {
      analyse(sensorData, JSON.parse(sensorInfo));
    }
  });
}

/**
* Handle error due to the lack of information about the sensor that send data.
* ( analysis cannot be performed without dataType of the sensor).
*/
function analysisError(sensorData) {
  console.log("This sensor is not connected to the system, cannot perform analysis on : " , sensorData);
}

/**
* Perform simple analysis on the data received from the sensors.
*/
function analyse(sensorData, sensorInfo) {
  console.log("Analysis started ! ");
  console.log("Analysing ", sensorInfo.value.dataType);

  var value = sensorData.data;
  var level;
  var description;

  switch(sensorInfo.value.dataType) {
    case 'heartbeat':
        if (value <= 20 || value >= 250 ){
          level = 3;
          description = 'just start digging';
          emergencyManager.startEmergency() ;
        } else if ( value <= 40 || value >= 180) {
          level = 2;
          description = 'warning, something strange is happening';
        } else {
          level = 1;
          description = 'everything ok';
        }
        break;

    case 'temperature':
      if (value >= 33 && value < 37 ){
        level = 1;
        description = 'everything ok';
      } else if ( value <= 41) {
        level = 2;
        description = 'warning, something strange is happening';
      } else {
        level = 3;
        description = 'just start digging';
        emergencyManager.startEmergency() ;
      }
      break;

    default:
      level = 2;
      description = 'warning, cannot analyze this parameter !';
    }

    console.log("Analysis completed ! ");

    /*
    var tryDate = moment(new Date()).format('YYYY-MM-DD HH:mm:ss:SSS');//.toDate();
    console.log("date: " , tryDate);
    */

  var sensorMeasure = {
     type : sensorInfo.value.dataType,
     message: {
		   patientId: sensorInfo.value.patientID,
		   value: sensorData.data,
		   unit: sensorInfo.value.unit,
  		 timestamp: moment(new Date()).format('YYYY-MM-DD HH:mm:ss:SSS'),
  		 output: {
			   level: level,
			   description: description
       }
    }
  }

  sendToDataCenter(sensorMeasure);
}


function sendToDataCenter(data) {
  console.log("Received data to publish : ");
  console.log("--- type : ", data.type);
  console.log("--- message : ");
  console.log("----- patientID : ", data.message.patientId);
  console.log("----- value  : ", data.message.value);
  console.log("----- unit : ", data.message.unit);
  console.log("----- timestamp : ", data.message.timestamp);
  console.log("----- output");
  console.log("------- level : ", data.message.output.level);
  console.log("------- description : ", data.message.output.description);

  publisher.publishMessage(JSON.stringify(data));
}

module.exports.invalidateList = function() {
  sensorsList = [];
}
