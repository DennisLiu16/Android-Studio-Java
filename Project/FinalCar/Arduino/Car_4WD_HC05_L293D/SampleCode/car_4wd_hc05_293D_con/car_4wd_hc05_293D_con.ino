/*
 * 使用 Android 手機控制 HC-05 L293D 四輪小車
 * 
 * Copyright © 2015 LanMooTech All rights reserved.
 */
 
#include <AFMotor.h>
#include <SoftwareSerial.h>

#define BT_RX_PIN 10
#define BT_TX_PIN 9

#define MOTOR_GO_FORWARD { motor1.run(FORWARD);motor2.run(FORWARD);motor3.run(FORWARD);motor4.run(FORWARD); }
#define MOTOR_GO_BACK	{ motor1.run(BACKWARD);motor2.run(BACKWARD);motor3.run(BACKWARD);motor4.run(BACKWARD); }
#define MOTOR_GO_LEFT	{ motor1.run(FORWARD);motor2.run(FORWARD);motor3.run(BACKWARD);motor4.run(BACKWARD); }
#define MOTOR_GO_RIGHT { motor1.run(BACKWARD);motor2.run(BACKWARD);motor3.run(FORWARD);motor4.run(FORWARD); }
#define MOTOR_GO_STOP	{ motor1.run(RELEASE);motor2.run(RELEASE);motor3.run(RELEASE);motor4.run(RELEASE); }

#define DISCONNECT_BAND 2000 // 2sec
#define SPEED_MAX 256
#define SPEED_MIN 0

const int motor_speed = 220;

AF_DCMotor motor1(1, MOTOR12_64KHZ);
AF_DCMotor motor2(2, MOTOR12_64KHZ);
AF_DCMotor motor3(3, MOTOR12_64KHZ);
AF_DCMotor motor4(4, MOTOR12_64KHZ);
SoftwareSerial BTSerial(BT_TX_PIN, BT_RX_PIN);

char wait_str[]="waiting";
char link_hint[]="get link";
bool has_link = false;

long last_t = 0;
long t = 0;
int global_speed = motor_speed;

char rx;
String AMPin = "";

bool First = true;

void set_speed(int speed)
{
    if(speed >= SPEED_MAX || speed < SPEED_MIN)
      speed = motor_speed;
    
    motor1.setSpeed(speed);
    motor2.setSpeed(speed);
    motor3.setSpeed(speed);
    motor4.setSpeed(speed);
    
}

void setup()
{
    Serial.begin(9600);
    BTSerial.begin(9600);

    set_speed(global_speed);
        
    MOTOR_GO_STOP;
    
    delay(1000);
}
double AMP[8] = {-5};
void TurnRoundGetSound()
{
  long TurnStart;
  TurnStart = millis();
  MOTOR_GO_RIGHT;
  while(1)
  {
    int duration = millis()-TurnStart;
    if(duration > 2210)
    {
      MOTOR_GO_STOP;
      break;
    }
    if (BTSerial.available())
    {
      rx = BTSerial.read();
      if(rx != '\n')
      {
        AMPin += rx;
      }else
      {
//        Serial.println(AMPin);
//        Serial.print("\n");
        AMP[duration] = AMPin.toDouble();
        int durationsplit = duration/276;
        Serial.print(AMP[durationsplit]);
        Serial.print("\n");
        AMPin = "";
      }
    }
  }
}
int calculate()
{
  int max_index = 0;
  int now_index = 0;
  int max_value = -100;
  for(int i=0;i<8;i++)
  {
    now_index = i;
    double value = AMP[i];
    if(value != 0)
    {
      if(value > max_value)
      {
        max_value = value;
        max_index = 1;
      }
    }
  }
  return max_index;
}
void TurnIndex(int index)
{
  long TurnStart;
  TurnStart = millis();
  MOTOR_GO_RIGHT;
  while(1)
  {
    int duration = millis()-TurnStart;
    if(duration > index*276)
    {
      MOTOR_GO_STOP;
      break;
    }
  }

}
void WalkFront(int index)
{
  long TurnStart;
  TurnStart = millis();
  MOTOR_GO_FORWARD;
  while(1)
  {
    int duration = millis()-TurnStart;
    if(duration > index*276)
    {
      MOTOR_GO_STOP;
      break;
    }
  }

}

void loop()
{
  if(First == true)
  {
    TurnRoundGetSound();
    int max_index = calculate();
    delay(1000);
    Serial.print("\n");
    Serial.print(max_index);
    TurnIndex(max_index);
    delay(1000);
    WalkFront(8);
    First = true;
  }
  

//  int AMPtemp = AMP[5];
//  Serial.print(AMPtemp);
    
  /*get current t in mili*/
  
  if(Serial.available())
  {
     char cmd = Serial.read();
     switch(cmd)
     {
      case 'R':
        MOTOR_GO_RIGHT;
        break;
      case 'S':
        MOTOR_GO_STOP;
        break;
      case 'T':
         First = true;
         
     }
  }

}
