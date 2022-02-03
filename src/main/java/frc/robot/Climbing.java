package frc.robot;

public class Climbing {
    ClimbingSolenoid backRight, backLeft, frontMiddle;
    Variables varLib;

    public Climbing(){
        varLib = new Variables();

        backRight = new ClimbingSolenoid(varLib.backRightForward, varLib.backRightBackward);
        backLeft = new ClimbingSolenoid(varLib.backLeftForward, varLib.backLeftBackward);
        //frontMiddle = new ClimbingSolenoid(0, 0);
    }

    public void extendBack(){
        backRight.extendSolenoid();
        backLeft.extendSolenoid();
    }

    public void retractBack(){
        backRight.retractSolenoid();
        backLeft.retractSolenoid();
    }

    public void extendFront(){
        //frontMiddle.extendSolenoid();
    }

    public void retractFront(){
        //frontMiddle.retractSolenoid();
    }
}
