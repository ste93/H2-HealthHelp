/**
* Module to analyse patient's parameter locally in order to handle emergency protocol even if the control unit is not connected to the network.
*/

var request = require('request');
var moment = require('moment');
var publisher = require('../pub_sub/controlUnitPublisher');
var emergencyManager = require ('../hardware/emergencyManager');
var lcd = require('../hardware/controlUnitLCD');
lcd.initializeLcd();
var lcdHasMessage = false;

var sensorsList = [];

// start emergency protocol after some critical data detected, in order to avoid  occasional reading errors in sensors
var emergencyThreshold = 10;
var emergencyCount = 0;

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
* Perform simple analysis on the data received from the sensors based on information available for this sensor.
*/
function analyse(sensorData, sensorInfo) {
  console.log("Analysis started ! ");
  console.log("Analysing ", sensorInfo.value.dataType);

  var value = sensorData.data;
  var level;
  var description;

  // analysis result depend on the vital parameter measured by the sensor.
  switch(sensorInfo.value.dataType) {
    case 'heart_rate' :
    case 'heartbeat':
        if (value <= 20 || value >= 250 ){
          level = 3;
          description = 'Emergency: critical heart beat';
          lcd.write("Emergency: heart");
          lcdHasMessage = true;
          emergencyCount += 1;
          if (emergencyCount >= emergencyThreshold){
            emergencyManager.startEmergency() ;
            emergencyCount = 0;
          }
        } else if ( value <= 40 || value >= 180) {
          level = 2;
          description = 'Warning: high heart beat !';
          lcd.write("Alert: heartbeat");
          lcdHasMessage = true;
        } else {
          level = 1;
          description = 'Heart Beat OK !';
		  if (lcdHasMessage ) {
			  lcd.reset();
			  lcdHasMessage = false;
		  }
        }
        break;

    case 'temperature':
      if (value >= 33 && value < 37 ){
        level = 1;
        description = 'Temperature OK !';
		if (lcdHasMessage ) {
			  lcd.reset();
			  lcdHasMessage = false;
		  }
      } else if ( value >= 37 && value <= 41) {
        level = 2;
        description = 'Warning: high temperature';
        lcd.write("Alert: high temp");
		lcdHasMessage = true;
      } else {
        level = 3;
        description = 'Emergency: critical temperature';
        lcd.write("Emergency: temp");
        lcdHasMessage = true;
        emergencyCount += 1;
        if (emergencyCount >= emergencyThreshold){
          emergencyManager.startEmergency() ;
          emergencyCount = 0;
        }
      }
      break;

    case 'glycemia' :
      if (value >= 60 && value < 110 ){
        level = 1;
        description = 'Glycemia OK !';
		if (lcdHasMessage ) {
			  lcd.reset();
			  lcdHasMessage = false;
		  }
      } else if ( value >= 100 && value <= 200) {
        level = 2;
        description = 'Warning: high glycemia level';
        lcd.write("Alert: glycemia");
		lcdHasMessage = true;
      } else {
        level = 3;
        description = 'Emergency: Critical Glycemia level';
        lcd.write("Emergency: glyc");
        lcdHasMessage = true;
        emergencyCount += 1;
        if (emergencyCount >= emergencyThreshold){
          emergencyManager.startEmergency() ;
          emergencyCount = 0;
        }
      }
      break;
    default:
      level = 2;
      description = 'Warning: Cannot analyze this parameter !';
      lcd.write("Alert: Error");
	  lcdHasMessage = true;
    }

    console.log("Analysis completed ! ");

// After analysis create the message with the right format and try to send it to the rabbitMQ server.
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

/**
* Function that try to send the data message to the publish-subscribe server.
* Message format must be :
* {"type" : String (dataType),
*  "message" : {
*    "patientID" : String,
*    "value" : String,
*    "unit" : String,
*    "timestamp" : String,
*    "output" : {
*       "level" : String,
*       "description" : String
*     }
*   }
* }
*/
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

/**
* Function that invalidate the local cached sensors list and force the system to retrive it remotely again.
*/
module.exports.invalidateList = function() {
  sensorsList = [];
}
