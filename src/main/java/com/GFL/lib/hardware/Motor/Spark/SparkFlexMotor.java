package com.GFL.lib.hardware.Motor.Spark;

import com.GFL.lib.Factory.MotorFactory.MotorModel;
import com.GFL.lib.hardware.Encoder.Spark.SparkFlexAbsoluteEncoder;
import com.GFL.lib.hardware.Encoder.Spark.SparkFlexRelativeEncoder;
import com.GFL.lib.hardware.config.MotorConfig;
import com.GFL.lib.hardware.config.MotorConfig.NeutralMode;
import com.GFL.lib.hardware.config.MotorConfig.SensorType;
import com.GFL.lib.hardware.interfaces.GenericEncoder;
import com.GFL.lib.hardware.interfaces.GenericMotor;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.FeedForwardConfig;
import com.revrobotics.spark.config.MAXMotionConfig;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class SparkFlexMotor implements GenericMotor {
    private final MotorModel motorModel;
    private final MotorConfig motorConfig;

    private final SparkFlex motor;
    private final SparkClosedLoopController closedLoopController;

    private final GenericEncoder encoder;

    public SparkFlexMotor(int id, MotorModel motorModel, MotorConfig motorConfig) {
        this.motorModel = motorModel;
        this.motorConfig = motorConfig;

        motor = new SparkFlex(id, MotorType.kBrushless);
        closedLoopController = motor.getClosedLoopController();

        if(motorConfig.encoderConfig.sensorType == SensorType.INTERNAL) encoder = new SparkFlexRelativeEncoder(motor);
        else encoder = new SparkFlexAbsoluteEncoder(motor);
    }

    @Override
    public void configure() {
        SparkFlexConfig config = new SparkFlexConfig();
        MAXMotionConfig motionConfig = new MAXMotionConfig();
        
        if(motorConfig.statorCurrentLimit != null) config.smartCurrentLimit(motorConfig.statorCurrentLimit);
        else config.smartCurrentLimit(motorModel.defaultStatorCurrentLimit);

        if(motorConfig.supplyCurrentLimit != null) config.secondaryCurrentLimit(motorConfig.supplyCurrentLimit);
        else config.secondaryCurrentLimit(motorModel.defaultSupplyCurrentLimit);

        if(motorConfig.neutralMode == NeutralMode.COAST) config.idleMode(IdleMode.kCoast);
        else config.idleMode(IdleMode.kBrake);
        
        config.inverted(motorConfig.inverted);
        config.voltageCompensation(motorConfig.voltageCompensation);

        motionConfig.cruiseVelocity(motorConfig.motionConfig.cruiseVelocity);
        motionConfig.maxAcceleration(motorConfig.motionConfig.maxAcceleration);
        
        motionConfig.allowedProfileError(motorConfig.motionConfig.allowedProfileError);

        config.closedLoop.pid(
            motorConfig.pidConfig.kP,
            motorConfig.pidConfig.kI,
            motorConfig.pidConfig.kD
        );

        config.closedLoop.apply(
            new FeedForwardConfig()
                .kS(motorConfig.pidConfig.kS)
                .kV(motorConfig.pidConfig.kV)
                .kA(motorConfig.pidConfig.kA)
        );

        FeedbackSensor feedbackSensor;

        if(motorConfig.encoderConfig.sensorType == SensorType.INTERNAL) feedbackSensor = FeedbackSensor.kPrimaryEncoder;
        else feedbackSensor = FeedbackSensor.kAbsoluteEncoder;

        config.closedLoop.feedbackSensor(feedbackSensor);

        config.closedLoop.apply(motionConfig);

        config.encoder
            .inverted(motorConfig.encoderConfig.inverted)
            .positionConversionFactor(motorConfig.encoderConfig.positionConversionFactor)
            .velocityConversionFactor(motorConfig.encoderConfig.velocityConversionFactor);

        config.absoluteEncoder
            .inverted(motorConfig.encoderConfig.inverted)
            .zeroOffset(motorConfig.encoderConfig.zeroOffset)
            .positionConversionFactor(motorConfig.encoderConfig.positionConversionFactor)
            .velocityConversionFactor(motorConfig.encoderConfig.velocityConversionFactor);

        motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void set(double present) {
        motor.set(present);
    }

    @Override
    public void setVelocity(double velocity) {
        closedLoopController.setSetpoint(velocity, ControlType.kVelocity);
    }

    @Override
    public void setPosition(double position) {
        closedLoopController.setSetpoint(position, ControlType.kPosition);
    }

    @Override
    public void setMotionMagicVelocity(double velocity) {
        closedLoopController.setSetpoint(velocity, ControlType.kMAXMotionVelocityControl);
    }

    @Override
    public void setMotionMagicPosition(double position) {
        closedLoopController.setSetpoint(position, ControlType.kMAXMotionPositionControl);
    }

    @Override
    public void stop() {
        motor.stopMotor();
    }

    @Override
    public GenericEncoder getEncoder() {
        return encoder;
    }
}