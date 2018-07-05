

/**
 * it reads the temperature from the DS18B20 sensor declared in the main module.
 * @returns the value of the temperature.
 */
float readTemperature() {
  sensors.requestTemperatures(); // Invia il comando di lettura delle temperatura
  float correctTemperature = sensors.getTempCByIndex(0);
  return correctTemperature;
}

/**
 * This function reads the current temperature, 
 * compares it to the previous temperature read and if the difference is less than 0.5 adds it to the array, 
 * otherwise it restarts filling the array from the beginning. 
 * when the array is full it takes the average of the medium values and sends it via bluetooth
 */
void processAndSendTemperature(){
  float data = readTemperature();
  if (abs(lastTemperatureValue - data) < 0.5) {    
    Serial.println("retrieving");
    Serial.println(data);
    setValueForAverage(readTemperaturesCounter, data);
    readTemperaturesCounter ++;
    isValid = true;
    if (readTemperaturesCounter > getNumValuesForAverage()) {
      float average = mediumValue();
      char temperatureValuetext[4] = "";
      dtostrf(average, 6, 1, temperatureValuetext);
      drawTextCentered (temperatureValuetext);
 //     BLEdevice.write(temperatureValuetext);
      sendDataOverBLE(temperatureValuetext);  
      delay(1000);
      readTemperaturesCounter = 0;
    }
  }
  if (!isValid) {
    readTemperaturesCounter = 0;
  }
  lastTemperatureValue = data;
  isValid = false;
}

