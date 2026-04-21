package com.GFL.lib.hardware.Motor.Talon;

import static edu.wpi.first.units.Units.Amps;

import com.GFL.lib.Factory.MotorFactory.MotorModel;
import com.GFL.lib.hardware.Encoder.CANCoder.CANCoderEncoder;
import com.GFL.lib.hardware.Encoder.Talon.TalonFXRelativeEncoder;
import com.GFL.lib.hardware.config.MotorConfig;
import com.GFL.lib.hardware.config.MotorConfig.SensorSource;
import com.GFL.lib.hardware.interfaces.GenericEncoder;
import com.GFL.lib.hardware.interfaces.GenericMotor;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.DriverStation;

public class TalonFXMotor implements GenericMotor {
    private final MotorModel motorModel;
    private final MotorConfig motorConfig;

    private final TalonFX motor;
    
    private final VelocityVoltage velocityControl;
    private final PositionVoltage positionControl;

    private final MotionMagicVoltage motionMagicPositionControl;
    private final MotionMagicVelocityVoltage motionMagicVelocityVoltageControl;

    private final NeutralOut neutralOut;

    private CANcoder remoteEncoder;
    private final GenericEncoder encoder;

    public TalonFXMotor(int id, MotorModel motorModel, MotorConfig motorConfig) {
        this.motorModel = motorModel;
        this.motorConfig = motorConfig;

        motor = new TalonFX(id);

        velocityControl = new VelocityVoltage(0);
        positionControl = new PositionVoltage(0);

        motionMagicPositionControl = new MotionMagicVoltage(0);
        motionMagicVelocityVoltageControl = new MotionMagicVelocityVoltage(0);

        neutralOut = new NeutralOut();

        encoder = createEncoder();
    }

    private GenericEncoder createEncoder() {
        if(motorConfig.encoderConfig.sensorSource == SensorSource.REMOTE) {
            int remoteId = motorConfig.encoderConfig.remoteSensorID;

            if(remoteId == -1) DriverStation.reportError("Remote sensor ID must be specified for remote encoder", false);
            else {
                remoteEncoder = new CANcoder(motorConfig.encoderConfig.remoteSensorID);
                return new CANCoderEncoder(remoteEncoder);
            }
        }
        return new TalonFXRelativeEncoder(motor);
    }

    @Override
    public void configure() {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.CurrentLimits
            .withStatorCurrentLimit(Amps.of((motorConfig.statorCurrentLimit != null) ? motorConfig.statorCurrentLimit : motorModel.defaultStatorCurrentLimit))
            .withSupplyCurrentLimit(Amps.of((motorConfig.supplyCurrentLimit != null) ? motorConfig.supplyCurrentLimit : motorModel.defaultSupplyCurrentLimit));

        if(motorConfig.neutralMode == MotorConfig.NeutralMode.COAST) config.MotorOutput.withNeutralMode(NeutralModeValue.Coast);
        else config.MotorOutput.withNeutralMode(NeutralModeValue.Brake);

        if(motorConfig.inverted) config.MotorOutput.withInverted(InvertedValue.Clockwise_Positive);
        else config.MotorOutput.withInverted(InvertedValue.CounterClockwise_Positive);

        config.withSlot0(
            new Slot0Configs()
                .withKP(motorConfig.pidConfig.kP).withKI(motorConfig.pidConfig.kI).withKD(motorConfig.pidConfig.kD)
                .withKS(motorConfig.pidConfig.kS).withKV(motorConfig.pidConfig.kV).withKA(motorConfig.pidConfig.kA)
                .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign)
        );

        config.withMotionMagic(
            new MotionMagicConfigs()
                .withMotionMagicCruiseVelocity(motorConfig.motionConfig.cruiseVelocity)
                .withMotionMagicAcceleration(motorConfig.motionConfig.maxAcceleration)
        );

        if(motorConfig.motionConfig.enableWrap) {
            config.SoftwareLimitSwitch
                .withReverseSoftLimitEnable(true)
                .withForwardSoftLimitEnable(true)
                .withReverseSoftLimitThreshold(motorConfig.motionConfig.wrapMin)
                .withForwardSoftLimitThreshold(motorConfig.motionConfig.wrapMax);

        }

        config.Feedback
            .withRotorToSensorRatio(motorConfig.encoderConfig.rotorToSensorRatio)
            .withSensorToMechanismRatio(motorConfig.encoderConfig.sensorToMechanismRatio);

        if(remoteEncoder != null) {
            if(motorConfig.encoderConfig.sensorMode == MotorConfig.SensorMode.REMOTE) config.Feedback.withRemoteCANcoder(remoteEncoder);
            else if(motorConfig.encoderConfig.sensorMode == MotorConfig.SensorMode.FUSED) config.Feedback.withFusedCANcoder(remoteEncoder);
            else if(motorConfig.encoderConfig.sensorMode == MotorConfig.SensorMode.SYNC) config.Feedback.withSyncCANcoder(remoteEncoder);
        }

        motor.getConfigurator().apply(config);
    }

    @Override
    public void set(double present) {
        motor.setControl(new DutyCycleOut(present));
    }

    @Override
    public void setVelocity(double velocity) {
        motor.setControl(velocityControl.withVelocity(velocity));
    }

    @Override
    public void setPosition(double position) {
        motor.setControl(positionControl.withPosition(position));
    }

    @Override
    public void setMotionMagicVelocity(double velocity) {
        motor.setControl(motionMagicVelocityVoltageControl.withVelocity(velocity));
    }

    @Override
    public void setMotionMagicPosition(double position) {
        motor.setControl(motionMagicPositionControl.withPosition(position));
    }

    @Override
    public void stop() {
        motor.setControl(neutralOut);
    }

    @Override
    public GenericEncoder getEncoder() {
        return encoder;
    }
}