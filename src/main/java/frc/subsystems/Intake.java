//creating a package lullllllll ://///
package frc.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.Spark;

//importing smart dashboard
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Intake{
    //speed of motor and name of intake motor
    PWMSparkMax intakeMotor;
    Spark beltMotorBottom, beltMotorTop; 
    double intakeMotorSpeed, bottomMotorSpeed, topMotorSpeed;
    String intakeStateSD;
    String intakeState, bottomMotorState, topMotorState;

    //constructing Intake
    public Intake(int iMotor, int bMotorB, int bMotorT){
        intakeMotor = new PWMSparkMax(iMotor);
        intakeMotor.setInverted(true);

        beltMotorBottom = new Spark(bMotorB);
        beltMotorBottom.setInverted(true);
        beltMotorTop = new Spark(bMotorT);
    }

    //basically, Robot says Stationary, it doesn't move
    //Robot says Intaking, motor turns
    public void setIntakeState(String state){
        if(state.equals("Stationary")){
            intakeMotorSpeed = 0;
            intakeMotor.set(intakeMotorSpeed);
            intakeStateSD = "Stationary";
        }else if(state.equals("Intaking")){
            intakeMotorSpeed = 0.8;
            intakeMotor.set(intakeMotorSpeed);
            intakeStateSD = "Intaking";
        }else if(state.equals("Reverse")){
            intakeMotor.set(-.8);
        }
    }

    //bottom belt motor
    public void setBeltStateBottom(String state){
        if(state.equals("Stationary")){
            bottomMotorSpeed = 0;
            beltMotorBottom.set(bottomMotorSpeed);
        }else if(state.equals("Bottom Motor")){
            bottomMotorSpeed = 0.8;
            beltMotorBottom.set(bottomMotorSpeed);
        }else if(state.equals("Reverse")){
            beltMotorBottom.set(-.8);
        }
    }

    //top belt motor
    public void setBeltStateTop(String state){
        if(state.equals("Stationary")){
            topMotorSpeed = 0;
            beltMotorTop.set(topMotorSpeed);
        }else if(state.equals("Top Motor")){
            topMotorSpeed = 0.8;
            beltMotorTop.set(topMotorSpeed);
        }else if(state.equals("Reverse")){
            beltMotorTop.set(-.8);
        }
    }
}