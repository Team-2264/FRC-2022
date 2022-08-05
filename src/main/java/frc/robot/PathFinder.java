package frc.robot;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PathFinder {

    double stage;
    Timer timer;

    public PathFinder() {
        timer = new Timer();
    }

    public void runFull(frc.robot.Odometry od, DriveTrain dt, Shooter sh, Sensors se, Intake in, AutoController au,
            PS4Controller dualsense) {

        SmartDashboard.putNumber("STAGE", stage);

        sh.runIntake();

        if (od.getX() < .3 && stage == 0) {
            dt.drive(0.4, 0, 0);
            au.index(in, sh, se, dualsense);
        } else if (stage == 0) {
            dt.fullStop();
            stage = 1;
        }

        if (od.getX() > .3 && stage == 1) {
            dt.drive(-0.4, 0, 0);
            au.index(in, sh, se, dualsense);
        } else if (stage == 1) {
            dt.fullStop();
            stage = 2;
        }

        if (od.getX() < 2 && stage == 2) {
            sh.runIntakeFast();
            au.index(in, sh, se, dualsense);
            dt.drive(0.2, 0, 0);
        } else if (stage == 2) {
            sh.stopIntake();
            dt.fullStop();
            stage = 3;
        }

        if (od.getGyroAngle() < 180 && stage == 3) {
            dt.drive(0, 0, 0.2);
            timer.resetTimer();
            au.index(in, sh, se, dualsense);
        } else if (stage == 3 || stage == 3.5) {
            if (!timer.hasPassed(3000)) {
                sh.smartShoot(se.calcDistance(), se.getTX(), dt, in, dualsense, true);
                stage = 3.5;
            } else {
                sh.stopShoot();
                dt.fullStop();
                stage = 4;
            }
        }

        if (od.getGyroAngle() > 26 && stage == 4)

        {
            dt.drive(0, 0, -0.2);
        } else if (stage == 4) {
            dt.fullStop();
            stage = 5;
        }

        if (od.getX() < 4.5 && stage == 5) {
            if (od.getY() > -1.5) {
                dt.drive(0.4, 0.15, 0);
            } else {
                dt.drive(0.4, 0, 0);
            }
        } else if (stage == 5) {
            dt.fullStop();
            stage = 6;
        }

        if (od.getX() > 0 && stage == 6) {
            if (od.getY() < 0) {
                dt.drive(-0.3, -0.15, 0);
            } else if (od.getGyroAngle() < 130) {
                dt.drive(0, 0, -0.2);
            }
        } else if (stage == 6) {
            dt.fullStop();
            stage = 7;
        }

    }

    public void runTwoBall(frc.robot.Odometry od, DriveTrain dt, Shooter sh, Sensors se, Intake in, AutoController au,
            PS4Controller dualsense) {

        SmartDashboard.putNumber("STAGE", stage);

        sh.runIntake();

        if (od.getX() < .3 && stage == 0) {
            dt.drive(0.4, 0, 0);
            au.index(in, sh, se, dualsense);
        } else if (stage == 0) {
            dt.fullStop();
            stage = 1;
        }

        if (od.getX() > .3 && stage == 1) {
            dt.drive(-0.4, 0, 0);
            au.index(in, sh, se, dualsense);
        } else if (stage == 1) {
            dt.fullStop();
            stage = 2;
        }

        if (od.getX() < 2 && stage == 2) {
            sh.runIntake();
            au.index(in, sh, se, dualsense);
            dt.drive(0.2, 0, 0);
        } else if (stage == 2) {
            sh.stopIntake();
            dt.fullStop();
            stage = 3;
        }

        if (od.getGyroAngle() < 180 && stage == 3) {
            dt.drive(0, 0, 0.2);
            timer.resetTimer();
            au.index(in, sh, se, dualsense);
        } else if (stage == 3 || stage == 3.5) {

            if (!timer.hasPassed(4000)) {
                sh.smartShoot(se.calcDistance(), se.getTX(), dt, in, dualsense, true);
                stage = 3.5;
            } else if (timer.hasPassed(4000)) {
                sh.stopShoot();
                dt.fullStop();
                stage = 4;
            }
        }
    }

    public void runOneBall(frc.robot.Odometry od, DriveTrain dt, Shooter sh, Sensors se, Intake in, AutoController au,
            PS4Controller dualsense) {

        SmartDashboard.putNumber("STAGE", stage);

        sh.runIntake();

        if (od.getX() < .3 && stage == 0) {
            dt.drive(0.4, 0, 0);
            au.index(in, sh, se, dualsense);
        } else if (stage == 0) {
            dt.fullStop();
            stage = 1;
        }

        if (od.getX() > .3 && stage == 1) {
            dt.drive(-0.4, 0, 0);
            au.index(in, sh, se, dualsense);
        } else if (stage == 1) {
            dt.fullStop();
            stage = 3;
        }

        if (od.getGyroAngle() < 180 && stage == 3) {
            dt.drive(0, 0, 0.2);
            timer.resetTimer();
            au.index(in, sh, se, dualsense);
        } else if (stage == 3 || stage == 3.5) {
            if (!timer.hasPassed(3000)) {
                sh.smartShoot(se.calcDistance(), se.getTX(), dt, in, dualsense, true);
                stage = 3.5;
            } else {
                sh.stopShoot();
                dt.fullStop();
                stage = 4;
            }
        }
    }

}