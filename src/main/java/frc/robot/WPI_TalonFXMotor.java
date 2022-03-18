package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import me.wobblyyyy.pathfinder2.robot.components.AbstractMotor;

/**
 * Wrapper for {@link TalonFX}.
 *
 * @author Colin Robertson
 * @since 0.10.8
 */
public class WPI_TalonFXMotor extends AbstractMotor {
    private final WPI_TalonSRX talon;

    /**
     * Create a new {@code TalonFXMotor}.
     *
     * @param talon the {@link TalonFX} to use.
     */
    public WPI_TalonFXMotor(WPI_TalonSRX talon) {
        super(
                (power) -> talon.set(ControlMode.PercentOutput, power),
                talon::getMotorOutputPercent
        );

        this.talon = talon;
    }

    /**
     * Create a new {@code TalonFXMotor}.
     *
     * @param deviceNumber the Talon's device number.
     */
    public WPI_TalonFXMotor(int deviceNumber) {
        this(new WPI_TalonSRX(deviceNumber));
    }

    /**
     * Get the internal TalonFX.
     *
     * @return the internal TalonFX.
     */
    public WPI_TalonSRX getTalon() {
        return talon;
    }
}
