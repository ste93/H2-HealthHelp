//var pub_sub = require('../pub_sub/pub_sub_test')
//import the noble module for manage ble devices
var noble = require('noble');
//moment module for getting timestamp
var moment = require('moment');

var seen={}; // devices seen and when
var peripheral_name_address_association = {};
//retrieved each time the application starts and crashes
//saved each time a new sensor is added
var addresses_saved_ble = ['c8df842a45bc'];
var nearList = []

//start scanning nearby ble devices
noble.on('stateChange', function(state) {
	//retrieve all sensors connected
  if (state === 'poweredOn') {
    // allow duplicates
    noble.startScanning([],true);
  }
});

noble.on('discover', function(peripheral) {
    dev=peripheral.uuid;
    var new_one=(typeof seen[dev] === 'undefined' || seen[dev] < (moment() - 5000));
    if (new_one && addresses_saved_ble.includes(dev)) {
		console.log(dev);
		addCallbacksToDeviceConnections(dev);
	    	console.log(seen);
		//getNearListLocal();
	}
	console.log("found" + dev);
    seen[dev]=moment();
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
