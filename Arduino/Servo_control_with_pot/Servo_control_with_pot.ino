#include <Servo.h>

Servo myServo;
int   potValue;
int   angle;

void setup()
{
  myServo.attach(9);
  Serial.begin(9600);
}

void loop()
{
  potValue = analogRead(A0);
  angle = map(potValue,0,1023,179,0);
  myServo.write(angle);
  
  delay(15);
}
  
