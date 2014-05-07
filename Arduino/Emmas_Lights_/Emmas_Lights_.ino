const int BLUE=11;
const int RED=7;
const int WHITE=3;
const int DELAY=50;

void setup()
{
  pinMode (BLUE, OUTPUT);
  pinMode (RED, OUTPUT);
  pinMode (WHITE, OUTPUT);
}

void switchOnALight (int theLightWeWant)
{ 
  digitalWrite (BLUE, theLightWeWant==BLUE );
  digitalWrite (RED, theLightWeWant==RED );
  digitalWrite (WHITE, theLightWeWant==WHITE );
  delay (DELAY);
}

void loop()
{
  switchOnALight (BLUE);
  switchOnALight (RED);
  switchOnALight (WHITE);
  switchOnALight (RED);
}
