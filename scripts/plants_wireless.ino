#include <nRF24L01.h>
#include <printf.h>
#include <RF24.h>
#include <RF24_config.h>
#include <SPI.h>

#include <DallasTemperature.h>

#include <OneWire.h>

const int LIGHT_SENSOR_ID = 1;
const int TEMP_SENSOR_ID = 2;
const int MOISTURE_SENSOR_1_ID = 3;
const int MOISTURE_SENSOR_2_ID = 4;
const int MOISTURE_SENSOR_3_ID = 5;

/* Analog */
const int lightSensorPin = 0;
const int muxOutPin = 1;

/* Digital */
const int tempSensorPin = 2;

/* Mux */
const int muxPins[3] = {4,5,6};

// Setup a oneWire instance to communicate with any OneWire devices
// (not just Maxim/Dallas temperature ICs)
OneWire oneWire(tempSensorPin);

// Pass our oneWire reference to Dallas Temperature.
DallasTemperature sensors(&oneWire);

int lightLevel, temp, m1,m2,m3 = 0;

RF24 radio(9,10);

void setup() {
  radio.begin();
  radio.setPALevel(RF24_PA_MAX);
  radio.setChannel(0x76);
  radio.openWritingPipe(0xF0F0F0F0E1LL);
  radio.enableDynamicPayloads();
  radio.powerUp();

  // Turn off moisture sensor
  pinMode(3,OUTPUT);
  digitalWrite(3,LOW);
  sensors.begin();

  // Set up mux
  for (int i=0; i<3; i++)
  {
    pinMode(muxPins[i], OUTPUT);
    digitalWrite(muxPins[i], LOW);
  }
  pinMode(muxOutPin, INPUT);

}

void sendRadioMessage(String s){
  char msgChar[100];
  s.toCharArray(msgChar,100);
  radio.write(&msgChar,sizeof(msgChar));
}

void selectMuxPin(byte pin) {
  if(pin >7){
    return;
  }

  digitalWrite(muxPins[0], bitRead(pin,0));
  digitalWrite(muxPins[1], bitRead(pin,1));
  digitalWrite(muxPins[2], bitRead(pin,2));
}

void loop() {
  digitalWrite(3,HIGH);
  delay(500);
  lightLevel = analogRead(lightSensorPin);
  sensors.requestTemperatures();
  temp = sensors.getTempCByIndex(0);

  String muxMessage = "";
  for(byte pin=0;pin<8;pin++){
    selectMuxPin(pin);
    int pinValue = analogRead(muxOutPin);
    muxMessage += String(pin+5) + ":" + pinValue + ",";
  }

  digitalWrite(3,LOW);
  String lightMsg = String(LIGHT_SENSOR_ID) + ":" + lightLevel;
  String tempMsg = String(TEMP_SENSOR_ID) + ":" + temp;

  sendRadioMessage(muxMessage+lightMsg+","+tempMsg);
  delay(600000);

}