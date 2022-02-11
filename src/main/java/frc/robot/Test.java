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

        portsFX = new int[0];
        portsSRX = new int[0];

        fx = new WPI_TalonFX[portsFX.length];
        srx = new WPI_TalonSRX[portsSRX.length];

        for(int i = 0; i < portsFX.length; i++) {
            fx[i] = new WPI_TalonFX(portsFX[i]);
        }

        for(int i = 0; i < portsSRX.length; i++) {
            srx[i] = new WPI_TalonSRX(portsSRX[i]);
        }

    }

    public void smartdashboardInit() {
        for(int i = 0; i < fx.length; i++) {
            SmartDashboard.putNumber("Test FX" + portsFX[i], 1000);
            SmartDashboard.putNumber("Test FX Vel" + portsFX[i], 0);
        }

        for(int i = 0; i < srx.length; i++) {
            SmartDashboard.putNumber("Test SRX" + portsSRX[i], 1000);
            SmartDashboard.putNumber("Test SRX Vel" + portsFX[i], 0);
        }
    }

    public void callPeriodic(Joystick j) {
        for(int i = 0; i < fx.length; i++) {
            if(j.getRawButton(portsFX[i])) {
                fx[i].set(ControlMode.Velocity, SmartDashboard.getNumber("Test FX" + i, 1000));
            }
        }

        for(int i = 0; i < srx.length; i++) {
            if(j.getRawButton(portsSRX[i])) {
                srx[i].set(ControlMode.Velocity, SmartDashboard.getNumber("Test SRX" + i, 1000));
            }
        }

    }
}
