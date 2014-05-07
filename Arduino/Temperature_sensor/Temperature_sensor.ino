const int SENSOR = A0;
float baselineTemp;

const int LED1 = 2;
const int LED2 = 3;
const int LED3 = 4;

void setup()
{
  Serial.begin(9600);

  for (int pinNumber = 2; pinNumber<5; pinNumber++)
  {
    pinMode(pinNumber,OUTPUT);
    digitalWrite(pinNumber,LOW);
  }

  int sensorVal = analogRead(SENSOR);
  float voltage = (sensorVal / 1024.0) * 5.0;
  baselineTemp = (voltage - 0.5) * 100.0;
}

void displayTemperatureLights(float temperature)
{
  float difference = temperature - baselineTemp;
  
  digitalWrite(LED1,(difference>3));
  digitalWrite(LED2,(difference>6));
  digitalWrite(LED3,(difference>9));
}

void loop()
{
  int sensorVal = analogRead(SENSOR);
  float voltage = (sensorVal / 1024.0) * 5.0;
  float temperature = (voltage - 0.5) * 100.0;
  
  Serial.print("Sensor value: ");
  Serial.print(sensorVal);
  Serial.print(", sensor voltage: ");
  Serial.print(voltage);
  Serial.print(", sensor temperature: ");
  Serial.print(temperature);
  Serial.print("C\n");
  
  displayTemperatureLights(temperature);
  
  delay(1);
}
