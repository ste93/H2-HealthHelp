#include <OneWire.h>
#include <DallasTemperature.h>
#include <SoftwareSerial.h>
#include <U8g2lib.h>

//U8g2 Contructor
U8G2_SSD1306_128X32_UNIVISION_F_SW_I2C u8g2(U8G2_R0, /* clock=*/ 5, /* data=*/ 4, /* reset=*/ 16);
u8g2_uint_t offset;     // current offset for the scrolling text
u8g2_uint_t width;      // pixel width of the scrolling text (must be lesser than 128 unless U8G2_16BIT is defined

//sensor must be connected to 3V3 VCC
#define temperaturePin  14

//one wire for the temperature
OneWire oneWire(temperaturePin);
//Dallas Temperature code made for DS18B20
DallasTemperature sensors(&oneWire);

//ble constructor
SoftwareSerial BLEdevice (13, 15); // RX, TX

int readTemperaturesCounter;
bool isValid;
bool isAlreadyRetrieved;
float lastTemperatureValue;
char temperatureValuetext[4] = "";

void setup()
{
  isValid = false;
  readTemperaturesCounter = 0;
  lastTemperatureValue = 0;
  isAlreadyRetrieved = false;
  Serial.begin(9600); 
  setupBluetooth(9600); // RX, TX
  setupDisplay();
  sensors.begin();
}

void loop(){
  processAndSendTemperature();
  delay(1000);
}
