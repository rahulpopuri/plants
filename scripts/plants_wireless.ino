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
const int m1Pin = 1;
const int m2Pin = 2;
const int m3Pin = 3;

/* Digital */
const int tempSensorPin = 2;

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
  
}

void sendRadioMessage(String s){
  char msgChar[100];
  s.toCharArray(msgChar,100);
  radio.write(&msgChar,sizeof(msgChar));
}

void loop() {
  digitalWrite(3,HIGH);
  delay(500);
  lightLevel = analogRead(lightSensorPin);
  sensors.requestTemperatures();
  temp = sensors.getTempCByIndex(0);

  m1 = analogRead(m1Pin);
  m2 = analogRead(m2Pin);
  m3 = analogRead(m3Pin);

  digitalWrite(3,LOW);
  String lightMsg = String(LIGHT_SENSOR_ID) + ":" + lightLevel;
  String tempMsg = String(TEMP_SENSOR_ID) + ":" + temp;
  String m1Msg = String(MOISTURE_SENSOR_1_ID) + ":" + m1;
  String m2Msg = String(MOISTURE_SENSOR_2_ID) + ":" + m2;
  String m3Msg = String(MOISTURE_SENSOR_3_ID) + ":" + m3;
  
  sendRadioMessage(m1Msg+","+m2Msg+","+m3Msg+","+lightMsg+","+tempMsg);
  delay(600000);
  
}
