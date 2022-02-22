package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Climbing {

    // private WPI_TalonFX leftClimbing;
    private WPI_TalonFX rightClimbing, leftClimbing;

    public Climbing() {
        rightClimbing = new WPI_TalonFX(Variables.climbingMotorPortRight);
        leftClimbing = new WPI_TalonFX(Variables.climbingMotorPortLeft);
    }

    public void runClimber() {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(100));
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(100));
    }

    public void stopClimber() {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(0));
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(0));
    }
    // suck my dick

    public double convertToUnitsPer100ms(double rpm) {
        // This function converts RPM to the unit, called "unit," that the motors use.
        double unitsPerMinute = (rpm * 2048);
        double unitsPer100 = unitsPerMinute / 600;
        return unitsPer100;
    }

    private double convertToRPM(double input) {
        // This function converts the unit, called "unit," that the motors use into RPM.
        return ((int) input * 600) / 2048;
    }

}
// it felt great ;)