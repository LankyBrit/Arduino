const int maxVoltage = 1000;
const int minVoltage = 50;

const int maxPWM = 128;
const int minPWM = 8;

void setup()
{
  pinMode(3,OUTPUT);
  Serial.begin(9600);
}

void loop()
{
  int v0 = analogRead(A0);
  
  Serial.print("V0 = ");
  Serial.print(v0);

  int absVoltage = v0 - minVoltage;
  int pwmValue = (((maxVoltage - absVoltage) / (float) maxVoltage) * (maxPWM-minPWM)) - minPWM;
  
  if (pwmValue<0) pwmValue=0;
  
  Serial.print(", PWM value = ");
  Serial.print(pwmValue);
  Serial.println();
  
  analogWrite(3,pwmValue);
  
  delay(10);
}
