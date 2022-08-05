package frc.robot;

public class Timer {

    long time;
    double length;

    public Timer() {
        time = System.currentTimeMillis();
        length = 0;
    }

    public void resetTimer() {
        time = System.currentTimeMillis();
    }

    public void setTime(long input) {
        time = input;
    }

    public void delayTimerBegin(long input) {
        time = System.currentTimeMillis() + input;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public boolean hasPassed(long input) {
        return getCurrentTime() - time > input;
    }

    public void setTimer(double input) {
        length = input;
    }

    public boolean checkTimer() {
        return getCurrentTime() - time > length;
    }

}
