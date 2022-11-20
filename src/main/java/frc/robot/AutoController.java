package frc.robot;

// import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
// import me.wobblyyyy.pathfinder2.utils.DualJoystick;
// import com.revrobotics.CANSparkMax;

public class AutoController {

  boolean indexingMode;
  long reverseTime;

  public AutoController() {
    indexingMode = false;
    reverseTime = 0;
  }

  public void index(Intake in, Shooter sh, Sensors se, PS4Controller dualsense) {

    if (dualsense.getR2Button() && System.currentTimeMillis() - sh.lastLimed > 1000) {
      in.resetLock();
    } else if (dualsense.getR1Button()) {
      in.resetLock();
    } else if (dualsense.getL1Button()) {
      in.runIndex();
      in.resetLock();
    } else {

      if ((!se.frontStatus() && se.backStatus()) || indexingMode) {
        indexingMode = true;
        in.reverseIndexSpecial();

        if (!se.backStatus()) {

          indexingMode = false;
          reverseTime = 0;

        }

        in.resetLock();
      } else {
        in.stopIndex();
        in.lockMotor();
      }

    }

  }
}
