package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

public class AutoController {

    boolean indexingMode;

    public AutoController() {
      indexingMode = false;
    }


    public void index(Joystick j, Intake in, Shooter sh, Sensors se) {
      if(!indexingMode) {
        if(j.getRawButton(2) || j.getRawButton(12) || (!se.frontStatus() && se.backStatus())) {
            if(j.getRawButton(12)) {
              sh.reverseIntake();
              in.reverseIndex();
            } else if(!se.frontStatus() && se.backStatus()) {
              indexingMode = true;
            } else if(se.backStatus() && se.frontStatus()) {
              in.runIndex();
              sh.runIntake();
            } else if(se.frontStatus() && !se.backStatus()) {
              in.stopIndex();
              sh.runIntake();
            } else {
              in.stopIndex();
              sh.runIntake();
            }
            
            SmartDashboard.putBoolean("Intake", true);
          } else {
            if(!j.getRawButton(7)) {
              sh.stopIntake();
            }
            if(!j.getRawButton(1)) {
              in.stopIndex();
            }
            SmartDashboard.putBoolean("Intake", false);
          }
        } else {
          in.runIndex();
          sh.stopIntake();
          if(!se.backStatus()) {
            indexingMode = false;
          }
        }
    } 
}
