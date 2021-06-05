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

int last_t = 0;
int t = 0;
int global_speed = motor_speed;


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

void loop()
{


  /*get current t in mili*/
  t = millis();
  
  if (BTSerial.available())
  {
    if(!has_link)
    {
      /*show link hint*/
      has_link = true;
      Serial.println(link_hint);
    }
    char command = (char)BTSerial.read();
  
    Serial.println(command);

    switch (command)
    {
      case 'F':
        MOTOR_GO_FORWARD;
        break;
      case 'B':
        MOTOR_GO_BACK;
        break;
      case 'L':
        MOTOR_GO_LEFT;
        break;
      case 'R':
        MOTOR_GO_RIGHT;
        break;
      case 'S':
        MOTOR_GO_STOP;
        break;

      /*change*/
    }

    last_t = t;
  }
  
  if(t - last_t >= DISCONNECT_BAND)
  {
    Serial.println(wait_str);
    has_link = false;
    last_t = t;
  }
}
