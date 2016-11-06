#! flask/bin/python
import RPi.GPIO as GPIO
from lib_nrf24 import NRF24
import time
import spidev

from flask import Flask, jsonify, make_response
import requests
import json

GPIO.setmode(GPIO.BCM)

pipes = [[0xE8, 0xE8, 0xF0, 0xF0, 0xE1],[0xF0, 0xF0, 0xF0, 0xF0, 0xE1]]

radio = NRF24(GPIO, spidev.SpiDev())
radio.begin(0, 17)

radio.setPayloadSize(32)
radio.setChannel(0x76)
radio.setDataRate(NRF24.BR_1MBPS)
radio.setPALevel(NRF24.PA_MIN)

radio.setAutoAck(True)
radio.enableDynamicPayloads()
radio.enableAckPayload()

radio.openReadingPipe(1, pipes[1])
radio.printDetails()
radio.startListening()

app = Flask(__name__)

def post_plant_data(message):
    url = 'http://localhost:8111/rest/sensors/writeData'
    for line in message.split(","):        
        data = {"id": line.split(":")[0],"value": line.split(":")[1]}
        headers = {'Content-Type': 'application/json'}
        response = requests.post(url,json=data)
        
    return
    
def loop():
    try :
        while True:
            while not radio.available(0):
                time.sleep(1/100)

            receivedMessage = []
            radio.read(receivedMessage, radio.getDynamicPayloadSize())
            #print("Received: {}".format(receivedMessage))
            string = ""

            for n in receivedMessage:
                # Stop if you see null
                if(n == 0):
                    break
                # Otherwise convert int to ascii char
                if (n >= 32 and n <= 126):
                    string += chr(n);
                    
            #print ("Message: {}".format(string))
            post_plant_data(string)
    finally:
        GPIO.cleanup()

@app.route('/api/v1/messages', methods=['GET'])
def get_messages():
    return jsonify({'message': "Test"})

@app.route('/')
def index():
    return "Hello, World!"

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    #app.run(debug=True)
    loop()

