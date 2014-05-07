#include <Servo.h>

Servo myServo;
int   servoValue = 0;
int   increment = 1;

void setup()
{
  myServo.attach(9);
  Serial.begin(9600);

  myServo.write(servoValue);
}

void loop()
{
  servoValue += increment;

  myServo.write(servoValue);

  if (servoValue>=180)
  {
    increment = -1;
  }
  else if (servoValue<=0)
  {
    increment = 1;
  }
  
  delay(15);
}
  
