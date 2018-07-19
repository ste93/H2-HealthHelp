#include <SoftwareSerial.h>
//ble constructor
SoftwareSerial BLEdevice (13, 15); // RX, TX


void setupBluetooth(int baudRate) {
	  BLEdevice.begin(baudRate);
}

void sendDataOverBLE(const char *text) {
	  BLEdevice.write(text);
}
