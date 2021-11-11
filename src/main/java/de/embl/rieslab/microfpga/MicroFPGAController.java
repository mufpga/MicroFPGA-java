package de.embl.rieslab.microfpga;

import java.util.ArrayList;

import de.embl.rieslab.microfpga.devices.*;
import de.embl.rieslab.microfpga.regint.RegisterInterface;

public class MicroFPGAController {

	private ArrayList<TTL> ttls_;
	private ArrayList<PWM> pwms_;
	private ArrayList<LaserTrigger> lasers_;
	private ArrayList<Servo> servos_;
	private ArrayList<AnalogInput> ais_;
	private CameraTrigger cam_;
	
	private boolean connected_;
	
	private final RegisterInterface regint_;
	private final int id_;
	private final int version_;
	
	public MicroFPGAController(int nLasers, int nTTLs, int nServos, int nPWMs,
							   int nAIs, boolean useCamera) throws Exception {

		regint_ = new RegisterInterface();

		// attempts to connect to the interface
		connected_ = regint_.connect();
		
		if(connected_){
			version_  = regint_.read(Signal.ADDR_VERSION);
			id_ = regint_.read(Signal.ADDR_ID);
			
			if(Signal.CURRENT_VERSION == version_ &&
					( (id_ == Signal.ID_AU) ||(id_ == Signal.ID_AUP) || (id_ == Signal.ID_CU) ) ) {
				DeviceFactory factory = new DeviceFactory(regint_);
				
				ttls_ = new ArrayList<TTL>();
				pwms_ = new ArrayList<PWM>();
				lasers_ = new ArrayList<LaserTrigger>();
				servos_ = new ArrayList<Servo>();
				ais_ = new ArrayList<AnalogInput>();
		
				for(int i=0;i<nTTLs;i++) {
					ttls_.add(factory.getTTL());
				}
				for(int i=0;i<nPWMs;i++) {
					pwms_.add(factory.getPWM());
				}
				for(int i=0;i<nLasers;i++) {
					lasers_.add(factory.getLaser());
				}
				for(int i=0;i<nServos;i++) {
					servos_.add(factory.getServo());
				}

				if ((id_ == Signal.ID_AU) || (id_ == Signal.ID_AUP)) {
					for (int i = 0; i < nAIs; i++) {
						ais_.add(factory.getAI());
					}
				}

				if(useCamera){
					cam_ = factory.getCameraTrigger();
				} else {
					factory.getCameraTrigger().setPassiveTrigger();
				}

			} else {
				regint_.disconnect();
				
				if(Signal.CURRENT_VERSION != version_) {
					throw new Exception("Incorrect firmware version ("+version_+"), expected version 2.");
				}
				if( (id_ != Signal.ID_AU) && (id_ != Signal.ID_AUP) && (id_ != Signal.ID_CU) ) {
					throw new Exception("Unknown device id ("+id_+"), expected version 49 (Cu), 79 (Au) or 80 (Au+).");
				}
			}
		} else {
			throw new Exception("Could not find device or could not connect.");
		}
	}

	public void disconnect() {
		connected_ = !regint_.disconnect();
	}
	
	public boolean isConnected() {
		return connected_;
	}
	
	public int getNumberTTLs() {
		return ttls_.size();
	}
	public int getNumberPWMs() {
		return pwms_.size();
	}
	public int getNumberLasers() {
		return lasers_.size();
	}
	public int getNumberServos() {
		return servos_.size();
	}
	public int getNumberAIs() {
		return ais_.size();
	}
	
	public boolean setTTLState(int channel, boolean state) {
		if(connected_ && channel >= 0 && channel < getNumberTTLs()) {
			return ttls_.get(channel).setState( state ? TTL.ON : TTL.OFF );
		}
		return false;
	}
	
	public boolean getTTLState(int channel) {
		if(connected_ && channel >= 0 && channel < getNumberTTLs()) {
			return ttls_.get(channel).getState() == TTL.ON;
		}
		return false;
	}
	
	public boolean setPWMState(int channel, int state) {
		if(connected_ && channel >= 0 && channel < getNumberPWMs()) {
			return pwms_.get(channel).setState( state );
		}
		return false;
	}
	
	public int getPWMState(int channel) {
		if(connected_ && channel >= 0 && channel < getNumberPWMs()) {
			return pwms_.get(channel).getState();
		}
		return -1;
	}
	
	public boolean setServoState(int channel, int state) {
		if(connected_ && channel >= 0 && channel < getNumberServos()) {
			return servos_.get(channel).setState( state );
		}
		return false;
	}

	public int getServoState(int channel) {
		if(connected_ && channel >= 0 && channel < getNumberServos()) {
			return servos_.get(channel).getState();
		}
		return -1;
	}

	public int getAnalogInputState(int channel){
		if(connected_ && channel >= 0 && channel < getNumberAIs()) {
			return ais_.get(channel).getState();
		}
		return -1;
	}

	public boolean setLaserParameters(int channel, LaserParameters p) {
		if (connected_ && channel >= 0 && channel < getNumberLasers()) {
			lasers_.get(channel).setParameters(p);
		}
		return false;
	}

	public String getLaserParametersPretty(int channel){
		if(connected_ && channel >= 0 && channel < getNumberLasers()) {
			return lasers_.get(channel).getParametersPretty();
		}
		return "Not connected, or wrong channel number.";
	}

	public LaserParameters getLaserParameters(int channel){
		if(connected_ && channel >= 0 && channel < getNumberLasers()) {
			return lasers_.get(channel).getParameters();
		}
		return null;
	}

	public boolean setLaserModeState(int channel, int state) {
		if(connected_ && channel >= 0 && channel < getNumberLasers()) {
			return lasers_.get(channel).setMode(state);
		}
		return false;
	}
	
	public int getLaserModeState(int channel) {
		if(connected_ && channel >= 0 && channel < getNumberLasers()) {
			return lasers_.get(channel).getMode();
		}
		return -1;
	}
	
	public boolean setLaserDurationState(int channel, int state) {
		if(connected_ && channel >= 0 && channel < getNumberLasers()) {
			return lasers_.get(channel).setDuration(state);
		}
		return false;
	}
	
	public int getLaserDurationState(int channel) {
		if (connected_ && channel >= 0 && channel < getNumberLasers()) {
			return lasers_.get(channel).getDuration();
		}
		return -1;
	}
	
	public boolean setLaserSequenceState(int channel, int state) {
		if(connected_ && channel >= 0 && channel < getNumberLasers()) {
			return lasers_.get(channel).setSequence(state);
		}
		return false;
	}
	
	public int getLaserSequenceState(int channel) {
		if(connected_ && channel >= 0 && channel < getNumberLasers()) {
			return lasers_.get(channel).getSequence();
		}
		return -1;
	}

	public boolean setActiveTrigger(){
		if(connected_ && cam_ != null) return cam_.setActiveTrigger();
		else return false;
	}

	public boolean setPassiveTrigger(){
		if(connected_ && cam_ != null) return cam_.setPassiveTrigger();
		else return false;
	}

	public boolean startCamera(){
		if(connected_ && cam_ != null) return cam_.start();
		else return false;
	}

	public boolean stopCamera(){
		if(connected_ && cam_ != null) return cam_.stop();
		else return false;
	}

	public boolean setCameraTriggerParameters(CameraParameters p){
		if(connected_ && cam_ != null) return cam_.setParameters(p);
		else return false;
	}

	public CameraParameters getCameraTriggerParameters(){
		if(connected_ && cam_ != null) return cam_.getParameters();
		else return null;
	}
	public String getCameraTriggerParametersPretty(){
		if(!connected_) return "Not connected.";

		if(cam_ != null) return cam_.getParametersPretty();
		else return "Camera not instantiated.";
	}

	public String getID() {
		if(connected_) {
			switch(id_){
				case Signal.ID_AU:
					return "Au";
				case Signal.ID_CU:
					return "Cu";
				case Signal.ID_AUP:
					return "Au+";
			}
			return "Unknown device";
		}
		return "Not connected";
	}
	
}
