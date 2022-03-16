// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/** Resources
*  https://docs.wpilib.org >>>>> Zero to Robot
*  https://first.wpi.edu/wpilib/allwpilib/docs/release/java/index.html >>>>> API
*  https://frc-pdr.readthedocs.io/en/latest/ >>>>> program stuff
*/

//Code for the 2022 FRC Competition for team 291 (NPCA + Harborcreek)
//Lead Programmer: Ethan Wang :D

//imagine being a package
package frc.robot;

//importing the package...? that I made
//DriveBase contains basically everything drive code related
import frc.subsystems.DriveBase;
import frc.subsystems.Intake;
import frc.subsystems.Shooter;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
//import edu.wpi.first.wpilibj.motorcontrol.Talon;

//necessary imports
//TimedRobot is idk
//Joystick is so you can hook up a joystick and make robot go brrr
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import com.revrobotics.CANSparkMax;
//import com.revrobotics.CANSparkMaxLowLevel;

//used to pause 
import java.lang.Thread;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

//initializing stuf
public class Robot extends TimedRobot {
  //initializing my drive base. Device ID following left motor1, left 2, r1, r2
  DriveBase driveBase = new DriveBase(1, 4, 2, 3);

  //CANSparkMax intakeMo = new CANSparkMax(5,CANSparkMaxLowLevel.MotorType.kBrushless);
  //initializing my intake. Device ID for intake motor
  Intake intake = new Intake(5, 6, 7);

  Spark shooter = new Spark(8);
  Spark shooter2 = new Spark(9);
  //Shooter shooter = new Shooter(8,9);

  UsbCamera cameraFront;
  UsbCamera cameraBack;
  NetworkTableEntry cameraSelection;

  double startTime;

  String intakeState = "Stationary";
  String beltStateBottom = "Stationary";
  String beltStateTop = "Stationary";
  String shooterState = "Stationary";
  
  boolean climbing = false;
  //initializing joystick
  //Two joysticks: controller2 is for forward backwards, controller1 is turn left turn right i think :/
  Joystick controller1 = new Joystick(0);
  Joystick controller2 = new Joystick(1);

  //private final Timer timer = new Timer();


  //variables for toggling swap front of robot
  //pausedForFToB pausedforBToF means, if false, it has not paused yet for front to back
  boolean pausedForFToB = false;
  boolean pausedforBToF = false;

  boolean buttonPressed = false;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    driveBase.setReverses();
    
    shooter2.setInverted(true);
    cameraFront = CameraServer.startAutomaticCapture(0);
    cameraBack = CameraServer.startAutomaticCapture(1);

    cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
  }

  //updating SmartDashboard
  @Override
  public void robotPeriodic() {
    //intake.update();
    driveBase.update(controller1.getY());
    
  }

  @Override
  public void autonomousInit() {
    startTime = Timer.getFPGATimestamp();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    double time = Timer.getFPGATimestamp();
    
    if(time - startTime < 1){
      intake.setIntakeState("Intaking");
      intake.setBeltStateBottom("Bottom Motor");
      intake.setBeltStateTop("Top Motor");
      shooter.set(1);
      shooter2.set(1);
    }else if(time - startTime < 3.5){
      intake.setBeltStateTop("Stationary");

      driveBase.myArcadeDrive(0, .5);
    }else if(time - startTime < 5){
      
      driveBase.myArcadeDrive(0, -.5);
    }else if(time - startTime < 6.5){
      driveBase.myArcadeDrive(0,0);
      
      shooter.set(1);
      shooter2.set(1);
      intake.setBeltStateTop("Top Motor");
    }else if(time - startTime < 8){
      driveBase.myArcadeDrive(0, .5);
    }else{
      intake.setIntakeState("Stationary");
      driveBase.myArcadeDrive(0,0);
      intake.setBeltStateBottom("Stationary");
      intake.setBeltStateTop("Stationary");
      shooter.set(0);
      shooter2.set(0);
    }
    
    /*
    if(time - startTime < 2){
      driveBase.myArcadeDrive(0, .55);
      intake.setIntakeState("Intaking");
      intake.setBeltStateBottom("Bottom Motor");
      //intake.setBeltStateTop("Top Motor");
    }else if(time - startTime < 2.1){
      driveBase.myArcadeDrive(0,0);

      
    }
    */
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    //DRIVE
    //driving code is driveBase.myArcadeDrive(x co-ord, y co-ord)
    //when you hold a button down, the front and back of the robot flip (no need to back up)
    //try and catch with Thread.sleep pauses the robot (no control whatsoever oof)
    //so that motors don't clockwise to counterclockwise instantly (  hurts motor :(  )
    
    
    if(controller1.getRawButton(6)){
      //driveBase.myArcadeDrive(0, controller1.getY());
      if(controller1.getRawButton(2)){
        if(pausedForFToB == false){
          try{
            //pause for .5 seconds 
            Thread.sleep(250);
          }catch(InterruptedException ex){
            Thread.currentThread().interrupt();
          }
          pausedForFToB = true;
        }
        pausedforBToF = false;
        driveBase.myArcadeDrive(0, controller1.getY());
      }else{
        if(pausedforBToF == false){
          try{
            Thread.sleep(250);
          }catch(InterruptedException ex){
            Thread.currentThread().interrupt();
          }
          pausedforBToF = true;
        }
        pausedForFToB = false;
        driveBase.myArcadeDrive(0, -controller1.getY());
      }
    }else{
      if(controller1.getRawButton(2)){
        if(pausedForFToB == false){
          try{
            //pause for .5 seconds 
            Thread.sleep(250);
          }catch(InterruptedException ex){
            Thread.currentThread().interrupt();
          }
          pausedForFToB = true;
        }
        pausedforBToF = false;
        driveBase.myArcadeDrive(controller1.getX()*1/2, controller1.getY());
      }else{
        if(pausedforBToF == false){
          try{
            Thread.sleep(250);
          }catch(InterruptedException ex){
            Thread.currentThread().interrupt();
          }
          pausedforBToF = true;
        }
        pausedForFToB = false;
        driveBase.myArcadeDrive(controller1.getX()*1/2, -controller1.getY());
      }
    }
    

    
    //INTAKE AND SHOOT
    if(controller2.getRawButton(1)){
      intakeState = "Intaking";
      beltStateBottom = "Bottom Motor";
      intake.setIntakeState(intakeState);
      intake.setBeltStateBottom(beltStateBottom);
    }else{
      intake.setIntakeState(intakeState);
      intake.setBeltStateBottom(beltStateBottom);
    }

    if(controller2.getRawButton(2)){
      beltStateTop = "Reverse";
      beltStateBottom = "Reverse";
      intakeState = "Reverse";
      intake.setBeltStateTop(beltStateTop);
      intake.setBeltStateBottom(beltStateBottom);
      intake.setIntakeState(intakeState);
    }else{
      intake.setBeltStateTop(beltStateTop);
      intake.setBeltStateBottom(beltStateBottom);
      intake.setIntakeState(intakeState);
    }

    if(controller2.getRawButton(3)){
      shooterState = "Shooting";
      shooter.set(1);
      shooter2.set(1);

      beltStateTop = "Top Motor";
      intake.setBeltStateTop(beltStateTop);
    }else{
      if(shooterState.equals("Shooting")){
        shooter.set(1);
        shooter2.set(1);
      }else if(shooterState.equals("Stationary")){
        shooter.set(0);
        shooter2.set(0);
      }
      
      intake.setBeltStateTop(beltStateTop);
    }

    if(controller2.getRawButton(4)){
      beltStateBottom = "Bottom Motor";
      intake.setBeltStateBottom(beltStateBottom);
    }else{
      intake.setBeltStateBottom(beltStateBottom);
    }

    if(controller2.getRawButton(5)){
      beltStateTop = "Top Motor";
      intake.setBeltStateTop(beltStateTop);
    }else{
      intake.setBeltStateTop(beltStateTop);
    }



    //STATIONARY
    if(!controller2.getRawButton(1)){
      intakeState = "Stationary";
      intake.setIntakeState(intakeState);
    }

    if(!controller2.getRawButton(1) && !controller2.getRawButton(2) && !controller2.getRawButton(4)){
      beltStateBottom = "Stationary";
      intake.setBeltStateBottom(beltStateBottom);
    }

    if(!controller2.getRawButton(2) && !controller2.getRawButton(3) && !controller2.getRawButton(5)){
      beltStateTop = "Stationary";
      intake.setBeltStateTop(beltStateTop);
    }
    
    if(!controller2.getRawButton(3)){
      shooterState = "Stationary";
      if(shooterState.equals("Stationary")){
        shooter.set(0);
        shooter2.set(0);
      }
    }



    //CAMERA
    if(controller1.getRawButtonPressed(1)){
      System.out.println("Setting Back Camera");
      cameraSelection.setString(cameraBack.getName());

    }else if(controller1.getRawButtonReleased(1)){
      System.out.println("Setting Front Camera");
      cameraSelection.setString(cameraFront.getName());
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}