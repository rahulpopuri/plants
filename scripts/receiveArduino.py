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

message = [
    {
        'id':1,
        'timestamp': 12356324,
        'message': u'Hello World!'
    }
]

def do_post(message):
    url = 'http://192.168.0.105:8111/rest/sensors/test'
    data = {"id": 1,"message": message}
    
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
            print("Received: {}".format(receivedMessage))
            string = ""

            for n in receivedMessage:
                if (n >= 32 and n <= 126):
                    string += chr(n)
            print ("Message: {}".format(string))
            do_post(string)
    finally:
        GPIO.cleanup()

@app.route('/api/v1/messages', methods=['GET'])
def get_messages():
    return jsonify({'message': message})

@app.route('/')
def index():
    return "Hello, World!"

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    #app.run(debug=True)
    loop()

