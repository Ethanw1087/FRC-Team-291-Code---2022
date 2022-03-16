package frc.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;


public class Shooter{
    Spark shooterLeft, shooterRight;
    MotorControllerGroup shooter;

    public Shooter(int shooterL, int shooterR){
        shooterLeft = new Spark(shooterL);
        shooterRight = new Spark(shooterR);

        //shooter = new MotorControllerGroup(shooterLeft, shooterRight);
    }

    public void setShooterSpeed(String state){
        if(state.equals("Stationary")){
            shooter.set(0);
        }else if(state.equals("Slow")){
            shooter.set(0.25);
        }else if(state.equals("Medium")){
            shooter.set(0.5);
        }else if(state.equals("Fast")){
            shooterLeft.set(0.75);
        }else if(state.equals("Max")){
            shooter.set(1);
        }
    }
}