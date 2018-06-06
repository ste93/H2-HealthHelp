#include <OneWire.h>
#include <DallasTemperature.h>

#define temperaturePin  14
//sensor must be connected to 3V3 VCC

OneWire oneWire(temperaturePin);
DallasTemperature sensors(&oneWire);

int counter;
bool isValid;
bool isAlreadyRetrieved;
float lastTemperatureValue;
char temperatureValuetext[4] = "";

void setup()
{
  isValid = false;
  counter = 0;
  lastTemperatureValue = 0;
  isAlreadyRetrieved = false;
  Serial.begin(9600); 
  setupBluetooth(9600); // RX, TX
  setupDisplay();
  sensors.begin();
}

void sendDataToProcessing(float data ){
  if (abs(lastTemperatureValue - data) < 0.5) {    
    dtostrf(data, 3, 1, temperatureValuetext);
    Serial.println("retrieving");
    Serial.println(temperatureValuetext);
    counter ++;
    isValid = true;
    if (counter > 10) {
      drawTextCentered (temperatureValuetext);
      sendDataOverBLE(temperatureValuetext);  
      delay(1000);
      drawTextCentered ("STOP");
      isAlreadyRetrieved = true;
    }
  }
  if (!isValid) {
    counter = 0;
  }
  lastTemperatureValue = data;
  isValid = false;
}

float readTemperature() {
  //int analogTemperatureRead = 0;
  //analogTemperatureRead = analogRead(temperaturePin);
  //Serial.println("analog");
  //Serial.println(analogTemperatureRead);
  //sensors.requestTemperaturesByAddress(tempSensor);
  sensors.requestTemperatures(); // Invia il comando di lettura delle temperatura
  float correctTemperature = sensors.getTempCByIndex(0);
  Serial.println(correctTemperature);
  Serial.println(DallasTemperature::toFahrenheit(correctTemperature),4);
  return correctTemperature;
}

void loop(){
  if (!isAlreadyRetrieved) {
    sendDataToProcessing(readTemperature());
  } else {
    sendDataOverBLE(temperatureValuetext); 
  }
  delay(1000);
}
