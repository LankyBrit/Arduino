#include <Servo.h>

const int TRIGGER = 12;
const int ECHO    = 13;
const int SERVO   = 3;

const int MIN_ANGLE = 20;
const int MAX_ANGLE = 160;
const int MAX_DISTANCE = 10000;
const int PINGS = 1;

Servo servo;

void setup()
{
  pinMode(TRIGGER,OUTPUT);
  pinMode(ECHO,INPUT);
  pinMode(SERVO,OUTPUT);

  servo.attach(SERVO);
  servo.write(MIN_ANGLE);
  
  Serial.begin(19200);
}

unsigned long pingDistance()
{
  long duration, distance;
  
  digitalWrite(TRIGGER,LOW);
  delayMicroseconds(2);
  digitalWrite(TRIGGER,HIGH);
  delay(10);
  digitalWrite(TRIGGER,LOW);
  duration = pulseIn(ECHO, HIGH);
    
  distance = duration / 5.82f;
  if (distance>MAX_DISTANCE) distance = -1;
  
  return distance;
}

unsigned long averageDistance(unsigned int iterations)
{
  unsigned long total = 0;
  int readings = 0;
  
  for (int i=0; i<PINGS; i++)
  {
    unsigned long pingDist = pingDistance();
    if (pingDist>0)
    {
      total += pingDist; readings++;
    };
  }
  
  if (readings>0)
  {
    return (unsigned long) (total / readings);
  }
  else
  {
    return -1;
  }
}

unsigned long turnAndPing(int angle)
{  
  servo.write(angle);
  delay(15);
  return averageDistance(PINGS);
}

void sendDistance(int angle, unsigned long distance)
{
  unsigned long sendDistance = distance;
  
  if (sendDistance>0)
  {
    Serial.print(angle);
    Serial.print(",");
    Serial.print(sendDistance);
    Serial.write(0);
    Serial.flush();
  }
}

void loop()
{
  int angle=90;
  
  servo.write(MAX_ANGLE);
  delay(100);

  loop:
  {
    for (angle=MAX_ANGLE; angle>=MIN_ANGLE; angle--)
    {
      sendDistance(angle,turnAndPing(angle));
    }
  
    for (angle=MIN_ANGLE+1; angle<MAX_ANGLE; angle++)
    {
      sendDistance(angle,turnAndPing(angle));
    }
  }
  
  goto loop;
}
