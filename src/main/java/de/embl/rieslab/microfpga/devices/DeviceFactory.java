package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.regint.RegisterInterface;

public class DeviceFactory {
	
	private final RegisterInterface regInt_;
	
	private int counterTTLs_, counterPWMs_, counterLasers_, 
		counterServos_, counterAIs_, counterCamera_;
	
	public DeviceFactory(RegisterInterface regint) {
		regInt_ = regint;
		
		counterTTLs_ = 0;
		counterPWMs_ = 0;
		counterLasers_ = 0;
		counterServos_ = 0;
		counterAIs_ = 0;
		counterCamera_ = 0;
	}

	public TTL getTTL() {
		if(getNumTTLLeft() > 0) {
			return new TTL(counterTTLs_++, regInt_);
		}
		return null;
	}
	
	public PWM getPWM() {
		if(getNumPWMLeft() > 0) {
			return new PWM(counterPWMs_++, regInt_);
		}
		return null;
	}
	
	public LaserTrigger getLaser() {
		if(getNumLaserLeft() > 0) {
			return new LaserTrigger(counterLasers_++, regInt_);
		}
		return null;
	}
	
	public Servo getServo() {
		if(getNumServoLeft() > 0) {
			return new Servo(counterServos_++, regInt_);
		}
		return null;
	}
	
	public AnalogInput getAI() {
		if(getNumAILeft() > 0) {
			return new AnalogInput(counterAIs_++, regInt_);
		}
		return null;
	}

	public CameraSync getCameraTrigger(){
		if(counterCamera_ < 1){
			counterCamera_++;
			return CameraSync.getInstance(regInt_);
		}
		return null;
	}

	public int getNumTTLLeft() {
		return Signal.NM_TTL -counterTTLs_;
	}

	public int getNumPWMLeft() {
		return Signal.NM_PWM -counterPWMs_;
	}

	public int getNumLaserLeft() {
		return Signal.NM_LASER -counterLasers_;
	}

	public int getNumServoLeft() {
		return Signal.NM_SERVO -counterServos_;
	}

	public int getNumAILeft() {
		return Signal.NM_AI -counterAIs_;
	}
}
