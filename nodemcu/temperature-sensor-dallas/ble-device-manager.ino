

/**
 * begins the software serial for the communication with the bluetooth module.
 * @param baudrate the baud rate for the serial communication (usual 9600)
 */
void setupBluetooth(int baudRate) {
	  BLEdevice.begin(baudRate);
}

/**
 * write into the serial made for bluetooth the text given as input (the bluetooth sends them on service and characteristic...
 * @param text the address of the array with the text to write
 */
void sendDataOverBLE(const char *text) {
	  BLEdevice.write(text);
}
