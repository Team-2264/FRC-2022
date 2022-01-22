package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.hal.util.*;
public class Pneumatics {
    DoubleSolenoid doubleSolenoid;
    Compressor compressor;
    
    public Pneumatics(int channelOne, int channelTwo) throws UncleanStatusException{
        doubleSolenoid = new DoubleSolenoid(1, PneumaticsModuleType.CTREPCM, channelOne, channelTwo);
    }
}
