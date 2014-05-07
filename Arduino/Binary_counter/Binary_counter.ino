int     switchState = 0;
int     value = 0;
boolean reset = true;

const int PUSH_SWITCH = 2;

const int LED_BOTTOM = 3;
const int LED_MIDDLE = 4;
const int LED_TOP = 5;

void setLEDCounterState()
{
  digitalWrite(LED_BOTTOM, value & 1);
  digitalWrite(LED_MIDDLE, value & 2);
  digitalWrite(LED_TOP,    value & 4);
}  

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
  pinMode(LED_BOTTOM,OUTPUT);
  pinMode(LED_MIDDLE,OUTPUT);
  pinMode(LED_TOP,OUTPUT);
  
  //
  // Set the LED's to their initial state.
  //
  
  setLEDCounterState();
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
    // When the switch state is LOW then we make sure that the toggle is in the RESET state
    //
    
    reset = true;
  }
  else
  {
    //
    // If the switch is in a RESET state (i.e. it has been pressed, and then released) then we can proceed, otherwise
    // we wait for the release.
    //
    
    if (reset)
    {
      //
      // The switch was clear, and then pressed.  The first thing we need to do is mark the switch as pressed.
      //
      
      reset = false;
      
      //
      // Increment the value and display the LED's
      //
      
      if (++value>7) value=0;
      setLEDCounterState();
    }
    
    delay(100);
  }
}
