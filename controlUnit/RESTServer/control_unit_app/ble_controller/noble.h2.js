//import the noble module for manage ble devices
var noble = require('noble');
//moment module for getting timestamp
var moment = require('moment');

var seen={}; // devices seen and when
var peripheral_name_address_association = {};
//retrieved each time the application starts and crashes
//saved each time a new sensor is added


var addresses_saved_ble = ['c8df842a45bc'];

//start scanning nearby ble devices
noble.on('stateChange', function(state) {
  if (state === 'poweredOn') {
    // allow duplicates
    noble.startScanning([],true);
  }
});

noble.on('discover', function(peripheral) {
    dev=peripheral.uuid;
    var new_one=(typeof seen[dev] === 'undefined' || seen[dev] < (moment() - 5000));
    if (new_one && addresses_saved.includes(dev)) {
		dev.connect();
	}
    seen[dev]=moment();
});

//noble.stopScanning();
//noble._peripherals
//noble._peripherals['c8df842a45bc']


//get the name of the perippheral from GUI
//for the connection 
for (var peripheral_address in noble._peripherals) {
	var peripheral_name = noble._peripherals[peripheral_address].advertisement.localName;

	if (peripheral_name != undefined) { 
		peripheral_name_address_association[peripheral_name] = peripheral_address;
		console.log(peripheral_name)
	}

}

function addCallbacksToDeviceConnections(device) {
	//here I create the buttons for connecting to the various peripherals
	buttonbla.on('click', function(nameFromGUI) {
		peripheral = noble._peripherals[peripheral_name_address_association[nameFromGUI]];
		//connection to the peripheral
		peripheral.connect();

		//discover the services available when connects to the peripheral
		peripheral.on('connect', function() {
			console.log('on -> connect');
			peripheral.discoverServices();
		});

		//this is the default service for this bluetooth device
		peripheral.on('servicesDiscover', function(service) {
			if(service.uuid == 'ffe0') {
				setCharacteristicCallback(service);
				service.discoverCharacteristics();
			}
		});
	});
}


//this is the default characteristic for this bluetooth device
function setCharacteristicCallback(service) {
	service.on('characteristicsDiscover', function(characteristic){
		if(characteristic.uuid == 'ffe1') {
			setCharacteristicDataReader(characteristic);
		}
	});
}

function setCharacteristicDataReader(characteristic)  {
	characteristic.on('read', function(data, isNotification) {
		console.log('on -> characteristic read ' + data + ' ' + isNotification);
		console.log(data);
	});
}
