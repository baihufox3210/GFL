package com.GFL.lib.hardware.config;

public class MotorConfig {
    public enum NeutralMode {BRAKE, COAST}
    public enum SensorSource {INTERNAL, ENCODER_PORT}
    public enum SensorMode {REMOTE, FUSED, SYNC}

    public Integer statorCurrentLimit = null;
    public Integer supplyCurrentLimit = null;

    public boolean inverted = false;
    
    public NeutralMode neutralMode = NeutralMode.BRAKE;

    public double voltageCompensation = 12.0;

    public double gearRatio = 1.0;
    
    public static class PIDConfig {
        public double kP = 0.0, kI = 0.0, kD = 0.0;
        public double kS = 0.0, kV = 0.0, kA = 0.0;
    }

    public static class MotionConfig {
        public double cruiseVelocity = 1.0;
        public double maxAcceleration = 1.0;
        public double allowedProfileError = 0.1;

        public boolean enableWrap = false;

        public double wrapMin, wrapMax;
    }

    public static class EncoderConfig {
        public SensorSource sensorSource = SensorSource.INTERNAL;
        public SensorMode sensorMode = SensorMode.REMOTE;

        public boolean inverted = false;
        
        public double zeroOffset = 0.0;

        public double positionConversionFactor = 1.0;
        public double velocityConversionFactor = 1.0;
    }

    public final PIDConfig pidConfig = new PIDConfig();
    public final MotionConfig motionConfig = new MotionConfig();
    public final EncoderConfig encoderConfig = new EncoderConfig();

    public MotorConfig setCurrentLimits(int statorLimit, int supplyLimit) {
        this.statorCurrentLimit = statorLimit;
        this.supplyCurrentLimit = supplyLimit;
        return this;
    }

    public MotorConfig setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    public MotorConfig setNeutralMode(NeutralMode neutralMode) {
        this.neutralMode = neutralMode;
        return this;
    }

    public MotorConfig setVoltageCompensation(double voltage) {
        this.voltageCompensation = voltage;
        return this;
    }

    public MotorConfig setGearRatio(double gearRatio) {
        this.gearRatio = gearRatio;
        return this;
    }

    public MotorConfig withKP(double kP) {
        this.pidConfig.kP = kP;
        return this;
    }

    public MotorConfig withKI(double kI) {
        this.pidConfig.kI = kI;
        return this;
    }

    public MotorConfig withKD(double kD) {
        this.pidConfig.kD = kD;
        return this;
    }

    public MotorConfig withKS(double kS) {
        this.pidConfig.kS = kS;
        return this;
    }

    public MotorConfig withKV(double kV) {
        this.pidConfig.kV = kV;
        return this;
    }

    public MotorConfig withKA(double kA) {
        this.pidConfig.kA = kA;
        return this;
    }

    public MotorConfig setMotionProfile(double cruiseVelocity, double maxAcceleration, double allowedProfileError) {
        this.motionConfig.cruiseVelocity = cruiseVelocity;
        this.motionConfig.maxAcceleration = maxAcceleration;

        this.motionConfig.allowedProfileError = allowedProfileError;
        
        return this;
    }

    public MotorConfig setContinuousInput(double min, double max) {
        this.motionConfig.enableWrap = true;

        this.motionConfig.wrapMin = min;
        this.motionConfig.wrapMax = max;
        
        return this;
    }

    public MotorConfig setEncoderInverted(boolean inverted) {
        this.encoderConfig.inverted = inverted;
        return this;
    }

    public MotorConfig setZeroOffset(double offset) {
        this.encoderConfig.zeroOffset = offset;
        return this;
    }

    public MotorConfig setSensorSource(SensorSource sensorSource) {
        this.encoderConfig.sensorSource = sensorSource;
        return this;
    }

    public MotorConfig setSensorMode(SensorMode sensorMode) {
        this.encoderConfig.sensorMode = sensorMode;
        return this;
    }

    public MotorConfig setConversion(double positionFactor, double velocityFactor) {
        this.encoderConfig.positionConversionFactor = positionFactor;
        this.encoderConfig.velocityConversionFactor = velocityFactor;
        
        return this;
    }
}