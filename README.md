# <img src="https://github.com/siddharthdd/PotatoHammers/blob/main/app/src/main/res/drawable-v24/app_logo.png" alt="App Logo" width="60" height="60"/>
 Welcome to 'Leट K' Repository

This is source code for a **Home Automation App** which controls home appliances using Bluetooth module.

# Installing

You can Directly Install apk from [releases](https://github.com/siddharthdd/PotatoHammers/releases) or You can clone the repository in Android Studio or download and extract the zip and open the folder as project and build or test apk from there

##  Hardware Components

Used **Bluetooth Module (HC- 05)** and **Temperature Sensor** with **Arduino UNO** and 3 bulbs and 3 fans as a prototype.

## Hardware Setup

|Pin| Device|
|--|--|
|4 |Bulb 2|
|5 |Mode 1(Of Fan)|
|6 |Mode 2(Of Fan)|
|7|Fan 1|
|8|Fan 2|
|10|Mode 3(Of Fan)|
|11|Fan (Slider)|
|13|Bulb 1|
|A1| Temperature Sensor |

Load the Following Code into the Arduino

    #include <SoftwareSerial.h>
    char data;
    int val;
    #define bulb1 13
    #define bulb2 4
    #define fan 7
    #define fan1 8
    #define mode1 5
    #define mode2 6
    #define mode3 10
    #define slider 11
    #define temp A1
    void setup(){
    Serial.begin(9600);
    while (!Serial) {
    ;
    }
    pinMode(bulb1,OUTPUT);
    pinMode(bulb2,OUTPUT);
    pinMode(fan,OUTPUT);
    pinMode(fan1,OUTPUT);
    pinMode(mode1,OUTPUT);
    pinMode(mode2,OUTPUT);
    pinMode(mode3,OUTPUT);
    pinMode(fan1,OUTPUT);
    pinMode(slider,OUTPUT);
    }
    
    void loop() {
    val = analogRead(temp);
    float mv = ( val/1024.0)*5000;
    float cel = mv/10;
    
    if (Serial.available())
    {
    data= Serial.read();
    if(data == 'T'){
    Serial.print(cel);
    delay(500);
    }
    if(data=='B') //Bulb1
    {
    digitalWrite(bulb1,1);
    }
    if(data=='b')
    {
    digitalWrite(bulb1,0);
    }
    if(data=='z') //Bulb2
    {
    digitalWrite(bulb2,1);
    Serial.println("Bulb2 on");
    }
    else if(data=='a')
    
    {
    digitalWrite(bulb2,0);
    Serial.println("Bulb2 off");
    }
    if(data=='C') //Fan
    {
    digitalWrite(fan,1);
    Serial.println("Fan on");
    }
    if(data=='c')
    {
    digitalWrite(fan,0);
    Serial.println("Fan off");
    }
    if(data=='D') //mode1
    {
    analogWrite(mode2,50);
    Serial.println("mode1 on");
    }
    if(data=='d')
    {
    analogWrite(mode2,0);
    Serial.println("mode1 off");
    }
    if(data=='E') //mode2
    {
    analogWrite(mode2,150);
    
    Serial.println("mode2 on");
    }
    if(data=='e')
    {
    analogWrite(mode2,0);
    Serial.println("mode2 off");
    }
    if(data=='H') //mode3
    {
    analogWrite(mode2,255);
    Serial.println("mode3 on");
    }
    if(data=='h')
    {
    analogWrite(mode2,0);
    Serial.println("mode3 off");
    }
    if(data=='G')
    {
    int sliderval1 = Serial.parseInt();
    analogWrite(slider,sliderval1);
    Serial.println(sliderval1);
    }
    }
    }





##  Signal and its meaning

|ASCII Character | Signal to Arduino|  Output from Arduino |
|--|--|--|
|'T'|Tempreature Signal| Calculated Float Value to Bluetooth Outputstream |
|'B' or 'b' |Bulb 1 |Toggles the Bulb 1 (on for 'B', off for 'b') |
|'z' or 'a'| Bulb 2|Toggles the Bulb  2|
|'C' or 'c'|Fan| Toggles the Fan 1|
|'D' or 'd'| Fan (Mode 1)| Toggles the Fan (Mode 1)
|'E' or 'e'| Fan (Mode 2)| Toggles the Fan (Mode 2)
|'H' or 'h'| Fan (Mode 3)| Toggles the Fan (Mode 3)
|'G'|Fan (Slider) | Activate the Inputstream of Aurduino for float input
|Float Value of Slider|Fan (Slider)| Sets the Fan speed |

**Note: Values of Float Range From 0 to 255*

## Feel Free to Fork/Edit 

You are free edit the code, make your own modified version and/or use as it is.
