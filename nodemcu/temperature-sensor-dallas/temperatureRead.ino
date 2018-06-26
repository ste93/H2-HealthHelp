
void processAndSendTemperature(float data ){
  if (abs(lastTemperatureValue - data) < 0.5) {    
    Serial.println("retrieving");
    Serial.println(data);
    setValueForAverage(counter, data);
    counter ++;
    isValid = true;
    if (counter > getNumValuesForAverage()) {
      float average = mediumValue();
      dtostrf(average, 3, 1, temperatureValuetext);
      drawTextCentered (temperatureValuetext);
      sendDataOverBLE(temperatureValuetext);  
      delay(1000);
      counter = 0;
    }
  }
  if (!isValid) {
    counter = 0;
  }
  lastTemperatureValue = data;
  isValid = false;
}

float readTemperature() {
  sensors.requestTemperatures(); // Invia il comando di lettura delle temperatura
  float correctTemperature = sensors.getTempCByIndex(0);
  return correctTemperature;
}

