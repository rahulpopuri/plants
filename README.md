# Plants

http://plants.bubblewrapstudios.ca/

The goal of this project is to allow me to monitor moisture levels of my plants - and enable them to notify me when they need to be watered.

#Hardware:

1. Raspberry Pi 3

2. Arduino Uno

3. Light Sensor

4. Temperature Sensor

5. 8 x Moisture sensor

6. 1 x Analog multiplexer



#Software:

1. Server: Java Spring Boot server, running on the Raspberry Pi

2. Database: MySQL

3. Front End: JQuery


#Current State

I've hooked up my Oregano and Basil plants, as well as light and temperature sensors to the Arduino. The Arduino sends sensor values to the Pi every 10 minutes - these values are stored in MySQL tables. If the moisture levels fall below a predefined threshold (currently 40%) the server will notify me via text message.

*Sample text :*
Oregano needs to be watered. Current moisture level = 620, threshold = 600 

(note that raw sensor values go from 0 to 1024, 0 being very wet and 1024 being perfectly dry)

The website(mentioned above) shows a graph of the last 5 days of sensor values, along with a table indicating the most recent value of each sensor.


#Future ideas:

1. Convert sensor data into more meaningful output (currently converted in the front end) 

2. Hook up the rest of the plants (will need a multiplexer as the Arduino is limited to 5 analog inputs)

3. Use Pushbullet instead of text messages (each plant can have its own stream).

4. Figure out the mechanics in getting the plants to water themselves (without electrocuting me) .

5. Better website design








