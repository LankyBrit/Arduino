int switchState = 0;



const int PUSH_SWITCH = 2;
const int LED_GREEN = 3;
const int LED_MIDDLE_RED = 4;
const int LED_TOP_RED = 5;

void setup()
{
  //
  // Setup pin modes as follows:
  //
  // 2 : Input from switch
  // 3 : Green LED
  // 4 : Middle red LED
  // 5 : Top red LED
  //
  
  pinMode(PUSH_SWITCH,INPUT);
  pinMode(LED_GREEN,OUTPUT);
  pinMode(LED_MIDDLE_RED,OUTPUT);
  pinMode(LED_TOP_RED,OUTPUT);
}

void loop()
{
  //
  // Read the switch state.
  //
  
  switchState = digitalRead(PUSH_SWITCH);
  
  if (switchState == LOW)
  {
    //
    // When the button is not pressed, we just light the green LED
    //
    
    digitalWrite(LED_GREEN,HIGH);
    digitalWrite(LED_MIDDLE_RED,LOW);
    digitalWrite(LED_TOP_RED,LOW);
  }
  else
  {
    //
    // When the button is pressed, blink the red LED's
    //
    
    digitalWrite(LED_GREEN,LOW);
    digitalWrite(LED_MIDDLE_RED,LOW);
    digitalWrite(LED_TOP_RED,HIGH);
    
    delay(250);
    
    digitalWrite(LED_MIDDLE_RED,HIGH);
    digitalWrite(LED_TOP_RED,LOW);
    
    delay(250);
  }
}
