package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.hal.util.*;
public class ClimbingSolenoid{
    DoubleSolenoid doubleSolenoid;
    Compressor compressor;
    
    public ClimbingSolenoid(int forwardChannel, int reverseChannel) throws UncleanStatusException{
        doubleSolenoid = new DoubleSolenoid(1, PneumaticsModuleType.CTREPCM, forwardChannel, reverseChannel);
        doubleSolenoid.set(Value.kOff);
    }

    public void extendSolenoid(){
        doubleSolenoid.set(Value.kForward);
    }
    public void retractSolenoid(){
        doubleSolenoid.set(Value.kReverse);
    }
}
