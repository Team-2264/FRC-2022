package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class Intake {

    Sensors sensors;
    WPI_TalonFX intakeMotor;
    // WPI_TalonSRX indexMotor;
    int ballsIn;
    long indexTime;

    public Intake() {
        intakeMotor = new WPI_TalonFX(Variables.intakeMotorPort);
        // indexMotor = new WPI_TalonSRX(Variables.indexMotorPort);
        ballsIn = 0;
    }

    public void intake() {
        // intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(3000));
        // Call indexing code if ball spotted
    }
    // Run motors
    public void runIntake(){
        intakeMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(3000));
    }

    public void runIndex() {
        // indexMotor.set(ControlMode.Velocity, convertToUnitsPer100ms(-4000));
    }

    public void stopIntake(){
        intakeMotor.set(ControlMode.Velocity, 0);
    }

    public void stopIndex(){
        // indexMotor.set(ControlMode.Velocity, 0);
    }



    // Indexing (make sure to... )

    public void index(Sensors se){

        // if(se.getBeamBroken()) {
        //     indexMotor.set(convertToUnitsPer100ms(200));
        //     indexTime = System.currentTimeMillis();
        //     if(System.currentTimeMillis() - indexTime > 2000) {
        //         indexMotor.set(convertToUnitsPer100ms(0));
        //     } 
        // } else{
        //     indexMotor.set(convertToUnitsPer100ms(200));
        // }

    }

    // if both beams open, run motors till second beam is broken. 
    // if beam two is occupied, run motors till beam one is broken
    // make sure to run a little more
    // pause intake when beam 1 broken

    // have a special "no-indexing" mode

    public void manualIndex(){
        // indexMotor.set(convertToUnitsPer100ms(200));
    }

    public void stopManualIndex(){
        // indexMotor.set(0);
    }

    // make a function that works with shooter. to move the index motor and clear bb2 and move ball from bb1 to bb2

    // Put the number of currrent balls on the smartdashboard
    

    private double convertToUnitsPer100ms(double rpm) {
        // This function converts RPM to the unit, called "unit," that the motors use.
        double unitsPerMinute = (rpm * 2048);
        double unitsPer100 = unitsPerMinute / 600;
        return unitsPer100;
    }

    private double convertToRPM(double input) {
        // This function converts the unit, called "unit," that the motors use into RPM.
        return ((int) input * 600)/2048;
    }

    

}
