package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;

public class AutoController {

  boolean indexingMode;

  public AutoController() {
    indexingMode = false;
  }

  public void index(Joystick j, Intake in, Shooter sh, Sensors se, PS4Controller dualsense) {
    if (!indexingMode) {
      if (dualsense.getL2Button() || j.getRawButton(12) || (!se.frontStatus() && se.backStatus())) {
        if (j.getRawButton(12)) {
          sh.reverseIntake();
          in.reverseIndex();
        } else if (!se.frontStatus() && se.backStatus()) {
          indexingMode = true;
        } else if (se.backStatus() && se.frontStatus()) {
          in.runIndex();
          sh.runIntake();
        } else if (se.frontStatus() && !se.backStatus()) {
          in.stopIndex();
          sh.runIntake();
        } else {
          in.stopIndex();
          sh.runIntake();
        }

      } else {
        if (!j.getRawButton(7) && !j.getRawButton(8)) {
          sh.stopIntake();
        }
        if (!j.getRawButton(1) && !j.getRawButton(6)) {
          in.stopIndex();
        }
      }
    } else {
      in.runIndex();
      if (!se.backStatus()) {
        indexingMode = false;
      }
      if (j.getRawButton(3)) {
        sh.runIntake();
      } else {
        sh.stopIntake();
      }
    }
  }
}
