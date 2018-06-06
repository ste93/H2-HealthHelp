var publisher = require('../pub_sub/controlUnitPublisher');
var request = require('request');

var sensorsList = new Array();

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

  //var testString = "test";
  //publisher.publishMessage(testString);
  publisher.publishMessage(JSON.stringify(data));
}

module.exports.analyseData = function (data) {
  console.log("Received data to analyse : " , data);
  //var sensorData = JSON.parse(data);
  var sensorData = data;

  // retrieve sensor informations
  var sensorID = sensorData.sensorID;
  request.get('http://localhost:3000/api/sensors/' + sensorID, function (error, response, sensorInfo) {
    console.log('error:', error); // Print the error if one occurred
    console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
    console.log('body : ' + sensorInfo );
    if (sensorInfo === ''){
      // the sensor is not connected to the system
      analyseDefault(sensorData);
    } else {
      sensorsList.push(sensorData)
      analyse(sensorData, JSON.parse(sensorInfo));
    }
  });
}


function analyseDefault(sensorData) {
  console.log("This sensor is not connected to the system, cannot perform analysis");
}

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
          //TODO
          //startEmergency();
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
        //TODO
        //startEmergency();
      }
      break;

    default:
      level = 2;
      description = 'warning, cannot analyze this parameter !';
    }

    console.log("Analysis completed ! ");

  var sensorMeasure = {
     type : sensorInfo.value.dataType,
     message: {
		   patientId: sensorInfo.value.patientID,
		   value: sensorData.data,
		   unit: sensorInfo.value.unit,
  		 timestamp: "31/02/1492-00:00:00", //TODO
  		 output: {
			   level: level,
			   description: description
       }
    }
  }

  //TODO
  sendToDataCenter(sensorMeasure);
}
