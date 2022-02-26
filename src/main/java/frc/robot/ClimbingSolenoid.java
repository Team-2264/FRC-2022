package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;


public class ClimbingSolenoid{
    DoubleSolenoid doubleSolenoid;
    
    public ClimbingSolenoid(int forwardChannel, int reverseChannel){
        doubleSolenoid = new DoubleSolenoid(15, PneumaticsModuleType.CTREPCM, forwardChannel, reverseChannel);
        doubleSolenoid.set(Value.kOff);
    }

    public void extendSolenoid(){
        doubleSolenoid.set(Value.kForward);
    }
    public void retractSolenoid(){
        doubleSolenoid.set(Value.kReverse);
    }

    public void disableSolenoid() {
        doubleSolenoid.set(Value.kOff);
    }
}