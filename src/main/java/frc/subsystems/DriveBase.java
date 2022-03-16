//DriveBase for the 2022 FRC Competition for team 291 (NPCA + Harborcreek)

package frc.subsystems;

//importing necessary classes to run CANSparkMax, Motor groups, and Differential drive train
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveBase{
  //creating CANSparkMax motors that are used to drive the robot
  PWMSparkMax leftMotor1, leftMotor2, rightMotor1, rightMotor2;
  
  //creating a group for motors (left and right)
  MotorControllerGroup leftMotors, rightMotors;

  //creating a drivetrain with the motor groups
  DifferentialDrive drivetrain;
  
  //constructor for drivebase that uses two motors on each side of the robot
  //lMotor1 is 1, lMotor2 is 4,, rMotor1 is 2, rMotor2 is 3
  public DriveBase(int lMotor1, int lMotor2, int rMotor1, int rMotor2){
    //initializing motors (LMotor1 ... RMotor2 are the port/deviceID of the SparkMax)
    leftMotor1 = new PWMSparkMax(lMotor1);
    leftMotor2 = new PWMSparkMax(lMotor2);
    rightMotor1 = new PWMSparkMax(rMotor1);
    rightMotor2 = new PWMSparkMax(rMotor2);

    //initializing motor groups (combining left motors with left and right with right)
    leftMotors = new MotorControllerGroup(leftMotor2, leftMotor1);
    rightMotors = new MotorControllerGroup(rightMotor1, rightMotor2);

    //initializing a differential drivetrain (forward and back, turn in place) from my two motor groups
    drivetrain = new DifferentialDrive(leftMotors, rightMotors);
  }

  //method that makes motor spin other way
  public void setReverses(){
    //not sure if this is needed but whatever
    leftMotor1.setInverted(false);
  }

  //making my differential drive be an arcade drive
  public void myArcadeDrive(double xValue, double yValue){
    drivetrain.arcadeDrive(xValue, yValue);
  }

  public void test(int id){
    if(id == 1){
      leftMotor1.set(.5);
    }else if(id == 2){
      rightMotor1.set(.5);
    }else if(id == 3){
      rightMotor2.set(.5);
    }else if(id == 4){
      leftMotor2.set(.5);
    }else{
      leftMotor1.set(0);
      leftMotor2.set(0);
      rightMotor1.set(0);
      rightMotor2.set(0);
    }
  }
  
  public void update(double speed){
    SmartDashboard.putNumber("Robot Speed: ", speed);
  }
}