package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;
import edu.wpi.first.wpilibj.PneumaticsControlModule;

public class Climbing {

    // private WPI_TalonFX leftClimbing;
    private WPI_TalonFX rightClimbing, leftClimbing;
    

    Compressor compressor;

    DoubleSolenoid doubleSolenoid;

    ClimbingSolenoid arms, ram;
    Variables varLib;

    public Climbing() {
        compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
        
        rightClimbing = new WPI_TalonFX(Variables.climbingMotorPortRight);
        leftClimbing = new WPI_TalonFX(Variables.climbingMotorPortLeft);

        arms = new ClimbingSolenoid(4, 5);
        ram = new ClimbingSolenoid(6, 7);

        compressor.enableDigital();;
    }

    public void runClimber() {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(100));
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(100));
    }

    public void reverseClimber() {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-100));
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-100));
    }

    public void runLeft() {
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(750));
    }

    public void runRight() {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(750));
    }

    public void reverseLeft() {
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-750));
    }

    public void reverseRight() {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(-750));
    }

    public void stopClimber() {
        rightClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(0));
        leftClimbing.set(ControlMode.Velocity, convertToUnitsPer100ms(0));
    }

    public void extendArms(){
        arms.extendSolenoid();
    }
    public void retractArms(){
        arms.retractSolenoid();
    }

    public void extendRam(){
        ram.extendSolenoid();
    }
    public void retractRam(){
        ram.retractSolenoid();
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