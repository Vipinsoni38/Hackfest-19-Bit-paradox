//nodemcu-code

#include<SoftwareSerial.h>
SoftwareSerial arduino(12,14);  //rx, tx // d6, d5
char itemID[12];
int count=0;
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  arduino.begin(9600);
}

void loop() {
/*  // put your main code here, to run repeatedly:
  Serial.println(count);
    if(arduino.available() && char(arduino.read())=='0')
    
    if(arduino.available() && char(arduino.read())=='8')
    if(arduino.available()&& char(arduino.read())=='0')
    if(arduino.available()&& char(arduino.read())=='0')
    {
      Serial.println(count);
        while(arduino.available() && count<8)
      {
        itemID[count] = char(arduino.read());
        count++;
      }
      Serial.println(String(itemID));
      count=0;

    }
    else {itemID[0]=0; count++;}
    
    delay(500); 
    count=0;
    
    if(arduino.available() && arduino.read()==int("_"))
    {
      while(arduino.available()=-=0p9ol8ki c && count<12)
      {
        itemID[count] = arduino.read();
        count++;
      }
      //itemID = arduino.readString();
      Serial.println(itemID);
    }*/
  //Serial.println(count);
  //Serial.println("..................");
  if(arduino.available())
    {
        while(arduino.available() && count<13)
      {
        itemID[count] = char(arduino.read());
        count++;
      }
      Serial.println(itemID);
      //sendToFirebase(purify(itemID));
      count=0;
    }
    delay(500); 
    count=0;
}
