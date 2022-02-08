package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Intake {
    
    WPI_TalonSRX intakeMotor;
    WPI_TalonSRX indexMotor;

    public Intake() {
        intakeMotor = new WPI_TalonSRX(Variables.intakeMotorPort);
        indexMotor = new WPI_TalonSRX(Variables.indexMotorPort);
    }

    // Run motors


    // Indexing (make sure to )

    // if both beams open, run motors till second beam is broken. 
    // if beam two is occupied, run motors till beam one is broken
    // make sure to run a little more
    // pause intake when beam 1 broken

    // have a special "no-indexing" mode

    // make a function that works with shooter. to move the index motor and clear bb2 and move ball from bb1 to bb2


    // Put the number of currrent balls on the smartdashboard

}
