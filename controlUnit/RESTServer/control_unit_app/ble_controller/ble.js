//var pub_sub = require('../pub_sub/pub_sub_test')

//import the noble module for manage ble devices
var noble = require('noble');
//moment module for getting timestamp
var moment = require('moment');
//for saving sensor permanently if raspberry turns off
var request = require('request');

// devices seen and when
var seen={}; 
//retrieved each time the application starts and crashes
var addresses_saved_ble = [];

/**
 * Function called each time the state of the bluetooth adapter changes.
 * @param state is the current state of the bluetooth adapter.
 */
noble.on('stateChange', function(state) {
	//retrieve all sensors connected
  if (state === 'poweredOn') {
	// allow duplicates
	retrieveSavedSensorList();
	//noble.startScanning([],true);
  }
});

/**
 * Function called each time a peripheral is detached by the program.
 * @param peripheral is a structure containing all the info about the peripheral.
 */
noble.on('discover', function(peripheral) {
    dev=peripheral.uuid;
    var new_one=(typeof seen[dev] === 'undefined' || seen[dev] < (moment() - 5000));
    if (new_one && addresses_saved_ble.includes(dev)) {
		addCallbacksToDeviceConnections(dev);
	}
    seen[dev]=moment();
	//getNearListLocal();
});

/**
 * Function that adds the passed sensor id to the list of known peripherals
 * @param {string} sensorId the mac address of the sensor
 */
module.exports.addPairedPeripheral = function(sensorId) {
	addresses_saved_ble.push(sensorId);
}

/**
 * Function that retrieves the saved sensors from the data manager. 
 * It is called before discovering nearby devices
 */
function retrieveSavedSensorList() {
	addresses_saved_ble = [];
	request.get('http://localhost:3000/api/sensors/', function (error, response, sensorList) {
		if (error) {
			console.log('error:', error); // Print the error if one occurred
		}
		//console.log('statusCode:', response && response.statusCode); // Print the response status code if a response was received
		if (sensorList === {}) {
			addresses_saved_ble = [];
		} else {
			var jsonSensorList = JSON.parse(sensorList);
			jsonSensorList.forEach(function(data) {
				if (data != null) {
					console.log(data)
					addresses_saved_ble.push(data.sensorId)
				}
			});
			noble.startScanning([],true);
		}
	});
}

/**
 * Function that return a list of nearby devices seen in the last 5 seconds
 */
module.exports.getNearList = function() {
	var nearList = [];
	console.log("[getNearList]");
	for (var address in seen){
		if (moment().diff(seen[address]) < 5000) {
			nearList.push({
				"id": address,
				"name" : noble._peripherals[address].advertisement.localName
			})
		}
	}
	return nearList;
}

/**
 * function that adds a callback after the device connection and establish
 * a connection to the device.
 * @param {string} deviceAddress the mac address of the device to connect to
 */
function addCallbacksToDeviceConnections(deviceAddress) {
	peripheral = noble._peripherals[deviceAddress];
	console.log('[BLE] adding callback')
	//discover the services available when connects to the peripheral
	peripheral.on('connect', function() {
		console.log('on -> connect');

		//this is the default service for this bluetooth device
		peripheral.on('servicesDiscover', function(services) {
			for( var index in services) {
				var service = services[index];
				//ffeo is the default characteristic of SH-HC-08 device
				if(service.uuid == 'ffe0') {
					setCharacteristicCallback(service, deviceAddress);
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

/**
 * function that sets the callback called when the characteristic is discovered
 * @param {} service a service structure that contains the service that the user wants to connect to
 * @param {string} deviceAddress the mac address of the device to connect to
 */
function setCharacteristicCallback(service, deviceAddress) {
	service.on('characteristicsDiscover', function(characteristics){
		for (var index in characteristics) {
			var characteristic = characteristics[index];
			if(characteristic.uuid == 'ffe1') {
				setCharacteristicDataReader(characteristic, deviceAddress);
			}
		}
	});
}

/**
 * Function that sets the callback when the program reads a characteristic
 * @param {*} characteristic the structure that represents the characteristic that must be read
 * @param {*} deviceAddress the mac address of the device to connect to
 */
function setCharacteristicDataReader(characteristic, deviceAddress)  {
	characteristic.on('read', function(data, isNotification) {
		console.log(data);
		//TODO post data to analyser
		var options = {
			url : "http://localhost:3000/api/sensors/" + deviceAddress + "/data",
			form: {
				"id" : deviceAddress,
				"data": data
			}			
		}
		request.post(options, function(error,httpResponse,body) {
			if (error) {
				console.log(error);
			} else {
				console.log(httpResponse.statusCode);
			}
		})
	});
}
