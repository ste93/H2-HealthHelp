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

void loop(){
  processAndSendTemperature(readTemperature());
  delay(1000);
}
