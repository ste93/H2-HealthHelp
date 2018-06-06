//var pub_sub = require('../pub_sub/pub_sub_test')
//import the noble module for manage ble devices
var noble = require('noble');
//moment module for getting timestamp
var moment = require('moment');
//for saving sensor permanently if raspberry turns off
var request = require('request');

var seen={}; // devices seen and when
var peripheral_name_address_association = {};
//retrieved each time the application starts and crashes
//saved each time a new sensor is added
var addresses_saved_ble = [];
var nearList = []

//start scanning nearby ble devices
noble.on('stateChange', function(state) {
	//retrieve all sensors connected
  if (state === 'poweredOn') {
    // allow duplicates
	  addresses_saved_ble =   request.get('http://localhost:3000/api/sensors/', function (error, response, sensorList) {
		console.log('error:', error); // Print the error if one occurred
		console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
		console.log('body : ' + sensorInfo );
		if (sensorList === {}) {
			addresses_saved_ble = [];
		} else {
			for (var index in sensorList) {
				addresses_saved_ble.push(sensorList[index].sensorId);
			}
		}
	  });
    noble.startScanning([],true);
  }
});

noble.on('discover', function(peripheral) {
    dev=peripheral.uuid;
    var new_one=(typeof seen[dev] === 'undefined' || seen[dev] < (moment() - 5000));
    if (new_one && addresses_saved_ble.includes(dev)) {
		addCallbacksToDeviceConnections(dev);
	}
    seen[dev]=moment();
	//peripheral_name_addresses_association[peripheral.advertisement.localName] = dev
	getNearListLocal();
});



//--------------------------------------------------------------------
module.exports.getNearList = function() {
	var nearList = getNearListLocal();
	return nearList;
};
	
function getNearListLocal() {
	var nearList = [];
	for (var address in seen){
		if (seen[address] < (moment - 5000)) {
			nearList[noble._peripherals[address].advertisement.localName] = address
		}
	}
	return nearList;
}

//---------------------------------------------------------------------
module.exports.doSomethingWith = function(id) {
	
  console.log("doing something with " + id + " sensors !");
  pub_sub.connectToTopic();
  //pub_sub.publishMessage("Test pub sub message");
  //save the sensor
}



function addCallbacksToDeviceConnections(deviceAddress) {
	peripheral = noble._peripherals[deviceAddress];

	//discover the services available when connects to the peripheral
	peripheral.on('connect', function() {
		console.log('on -> connect');

		//this is the default service for this bluetooth device
		peripheral.on('servicesDiscover', function(services) {
			for( var index in services) {
				var service = services[index];
				if(service.uuid == 'ffe0') {
					setCharacteristicCallback(service);
					service.discoverCharacteristics();
				}
			}
		});

		peripheral.discoverServices();
		console.log(peripheral.state);
	});
	//connection to the peripheral
	peripheral.connect();
}

//this is the default characteristic for this bluetooth device
function setCharacteristicCallback(service) {
	service.on('characteristicsDiscover', function(characteristics){
		for (var index in characteristics) {
			var characteristic = characteristics[index];
			if(characteristic.uuid == 'ffe1') {
				setCharacteristicDataReader(characteristic);
			}
		}
	});
}

function setCharacteristicDataReader(characteristic)  {
	characteristic.on('read', function(data, isNotification) {
		console.log('on -> characteristic read ' + data + ' ' + isNotification);
		console.log(data);
	});
}
