#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <ArduinoJson.h>
#include <SoftwareSerial.h>

//
#define FIREBASE_HOST "example.firebaseio.com"
#define FIREBASE_AUTH "token_or_secret"
#define WIFI_SSID "SSID"
#define WIFI_PASSWORD "PASSWORD"
SoftwareSerial arduino(D6, D5);
char itemID[12];
int count = 0;
char thisCartID[] = "CART0001";
String items = "[";

//SoftwareSerial arduino();


/*
 * this code will be uploaded in microcontroller of each WayCart.
 * So this code must contain a unique-ID for its respective WayCart.
 */
void setup() {
  Serial.begin(9600);
  
  //serial comm with arduino
  arduino.begin(9600);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  //firebase begin
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  //JSON data-frame creation
  const size_t capacity = JSON_ARRAY_SIZE(20) + JSON_OBJECT_SIZE(1);
  DynamicJsonBuffer itemsInCart(capacity);
  JsonObject& root = itemsInCart.createObject();
  JsonArray& items = root.createNestedArray("items");  
}

void loop() 
{
  
  if arduino.available()
  {
    //store ID string in a variable
    count = 0;
    while(arduino.available() && count<12)
    {
      itemID[count] = arduino.read();
      count++;
      delay(5);  
    }
    //items = items + itemID;         //items.add(itemID);
    
    //Upload to Firebase
    String s="";
    for(int i=0;i<itemID.length();i++)
    {
      s+=itemID[i];
    }
    //int ind=items.indexOf(',');
    if( items.length()==1)
    items+=s;
    else
    items=items+","+s;
    
    
    Firebase.pushString("/carts/"+thisCartID, items+"]");
  } 
}

//char json[] = "[1,2,3]";
//JsonArray& array2 = jsonBuffer.parseArray(json);
