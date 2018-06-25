#include <OneWire.h>
#include <DallasTemperature.h>

#define temperaturePin  14
//sensor must be connected to 3V3 VCC

OneWire oneWire(temperaturePin);
DallasTemperature sensors(&oneWire);

const int valuesForAverage = 10;
int counter;
bool isValid;
bool isAlreadyRetrieved;
float lastTemperatureValue;
char temperatureValuetext[4] = "";
float readValues[valuesForAverage];


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

int cmp (const void* num1,const void* num2)
{
  float a = *((float *)num1);
  float b = *((float *)num2);
  if (a < b) return -1;
  else if (a == b) return 0;
  return 1;
}

float mediumValue(float* values) {
  float average;
  int numItems;
  
  qsort (values ,valuesForAverage,sizeof(float),cmp);
                    
  for (int i = 2; i < (valuesForAverage)-2;i++) {
    average = average + values[i];
    numItems ++ ;
  }
  return average/numItems;
}


void sendDataToProcessing(float data ){
  if (abs(lastTemperatureValue - data) < 0.5) {    
    Serial.println("retrieving");
    Serial.println(temperatureValuetext);
    readValues[counter] = data;
    counter ++;
    isValid = true;
    if (counter > valuesForAverage) {
      float average = mediumValue(readValues);
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
