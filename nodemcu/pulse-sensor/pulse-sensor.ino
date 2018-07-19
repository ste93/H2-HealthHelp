#include <Ticker.h>

int pulsePin = 0;          
int fadeRate = 0;  
volatile int BPM;                  
volatile int Signal;                
volatile int IBI = 600;             
volatile boolean Pulse = false; 
volatile boolean QS = false;   volatile int rate[10];                  
volatile unsigned long sampleCounter = 0;   
volatile unsigned long lastBeatTime = 0;      
int lastBeatValue = 0;
volatile int P =512;                
volatile int T = 512;               
volatile int thresh = 512;       
volatile int amp = 100;          
volatile boolean firstBeat = true;       
volatile boolean secondBeat = false;
String command = ""; // Stores response of bluetooth device
int counter;
bool isValid;
bool isAlreadyRetrieved;

Ticker flipper;
   
void sendDataToProcessing(char symbol, int data ){
  char text[3] = "";
  //here is the control for the variation of bpm and maybe the control for the out of range
  //also can be controlled by checking if the finger is still there by another button
  if (abs(lastBeatValue - data) < 3) {	  
	  dtostrf(data, 3, 0, text);
    counter ++;
    isValid = true;
    if (counter > 6) {
  	  drawTextCentered (text);
  	  sendDataOverBLE(text);  
      delay(1000);
      drawTextCentered ("STOP");
      isAlreadyRetrieved = true;
    }
  }
  if (!isValid) {
    counter = 0;
  }
  lastBeatValue = data;
  isValid = false;
}


void setup(){ 
  isValid = false;
  counter = 0;
  isAlreadyRetrieved = false;
  Serial.begin(9600);  
  interruptSetup();
  setupBluetooth(9600); // RX, TX
  setupDisplay();
}  

void loop(){
  if (!isAlreadyRetrieved) {
    if (QS == true){
      sendDataToProcessing('B',BPM);
      QS = false;
    }
    delay(100);
  } 
}
