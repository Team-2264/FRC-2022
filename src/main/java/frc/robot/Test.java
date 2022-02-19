package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Test {
    
    private WPI_TalonFX[] fx;
    private WPI_TalonSRX[] srx;

    int[] portsFX;
    int[] portsSRX;

    public Test() {

        portsFX = new int[1];
        portsSRX = new int[0];

        portsFX[0] = 9;

        fx = new WPI_TalonFX[portsFX.length];
        srx = new WPI_TalonSRX[portsSRX.length];

        for(int i = 0; i < portsSRX.length; i++) {
            srx[i] = new WPI_TalonSRX(portsSRX[i]);
        }

        for(int i = 0; i < portsFX.length; i++) {
            fx[i] = new WPI_TalonFX(portsFX[i]);
        }

        for(int i = 0; i < portsSRX.length; i++) {
            srx[i].configFactoryDefault();
            srx[i].config_kP(0, 0);
            srx[i].config_kI(0, 0);
            srx[i].config_kD(0, 0);
            srx[i].config_kF(0, 0);
        }

        for(int i = 0; i < portsFX.length; i++) {
            fx[i].configFactoryDefault();
            fx[i].config_kP(0, 0);
            fx[i].config_kI(0, 0);
            fx[i].config_kD(0, 0);
            fx[i].config_kF(0,  0);
        }

    }

    public void smartdashboardInit() {
        for(int i = 0; i < fx.length; i++) {
            SmartDashboard.putNumber("Test FX" + portsFX[i], 1000);
            SmartDashboard.putNumber("Test FX Vel" + portsFX[i], 0);
        }

        for(int i = 0; i < srx.length; i++) {
            SmartDashboard.putNumber("Test SRX" + portsSRX[i], 1000);
            SmartDashboard.putNumber("Test SRX Vel" + portsSRX[i], 0);
        }
    }

    public void smartdashboardUpdate() {
        for(int i = 0; i < fx.length; i++) {
            SmartDashboard.putNumber("Test FX Vel" + portsFX[i], 0);
        }

        for(int i = 0; i < srx.length; i++) {
            SmartDashboard.putNumber("Test SRX Vel" + portsSRX[i], 0);
        }
    }

    public void callPeriodic(Joystick j) {

        if(j.getRawButton(4)) {
            fx[0].set(ControlMode.Velocity, 4000);
        } else if(j.getRawButton(5)) {
            fx[0].set(ControlMode.Velocity, -4000);
        } else {
            fx[0].set(ControlMode.Velocity, 0);
        }
        

        SmartDashboard.putBoolean("called", false);
        // for(int i = 0; i < fx.length; i++) {
        //     if(j.getRawButton(4)) {
        //         SmartDashboard.putBoolean("called", true);
        //         fx[i].set(ControlMode.Velocity, SmartDashboard.getNumber("Test FX" + portsFX[i], 1000));
        //     } else {
        //         fx[i].set(ControlMode.Velocity, 0);

        //     }
        //     SmartDashboard.putNumber("Test FX Vel" + portsFX[i], convertToRPM(fx[i].getSelectedSensorVelocity()));
        // }

        // for(int i = 0; i < srx.length; i++) {
        //     if(j.getRawButton(portsSRX[i])) {
        //         srx[i].set(ControlMode.Velocity, SmartDashboard.getNumber("Test SRX" + portsSRX[i], 1000));
        //     } else {
        //         srx[i].set(ControlMode.Velocity, 0);
        //     }
        //     SmartDashboard.putNumber("Test SRX Vel" + portsSRX[i], convertToRPM(srx[i].getSelectedSensorVelocity()));
        // }
    }


    public double convertToUnitsPer100ms(double rpm) {
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
