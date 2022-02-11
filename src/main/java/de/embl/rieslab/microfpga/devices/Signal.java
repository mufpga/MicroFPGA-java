package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.regint.RegisterInterface;

public abstract class Signal {

	public final static int NM_TTL = 4;
	public final static int NM_PWM = 5;
	public final static int NM_LASER = 8;
	public final static int NM_SERVO = 7;
	public final static int NM_AI = 8;

	public final static int ADDR_MODE = 0;
	public static final int ADDR_DURA = ADDR_MODE+ NM_LASER;
	public static final int ADDR_SEQ = ADDR_DURA+ NM_LASER;
	public static final int ADDR_TTL = ADDR_SEQ+ NM_LASER;
	public static final int ADDR_SERVO = ADDR_TTL+ NM_TTL;
	public static final int ADDR_PWM = ADDR_SERVO+ NM_SERVO;

	public static final int ADDR_ACTIVE_SYNC = ADDR_PWM + NM_PWM;
	public static final int ADDR_START_TRIGGER = ADDR_ACTIVE_SYNC + 1;
	public static final int ADDR_CAM_PULSE = ADDR_START_TRIGGER + 1;
	public static final int ADDR_CAM_READOUT = ADDR_CAM_PULSE + 1;
	public static final int ADDR_CAM_EXPO = ADDR_CAM_READOUT + 1;
	public static final int ADDR_LASER_DELAY = ADDR_CAM_EXPO + 1;

	public static final int ADDR_ANALOG_INPUT = ADDR_LASER_DELAY + 1;

	public static final int ADDR_VERSION = 200;
	public static final int ADDR_ID = 201;

	public static final int ERROR_UNKNOWN_COMMAND = 11206655;

	public static final int ID_AU = 79;
	public static final int ID_AUP = 80;
	public static final int ID_CU = 29;
	public static final int ID_MOJO = 12;
	public static final int CURRENT_VERSION = 3;

	private final boolean readOnly_;
	private final int id_;
	protected final RegisterInterface regInt_;
	
	protected Signal(int id, RegisterInterface regInt, boolean readOnly) {
		id_ = id;
		readOnly_ = readOnly;
		regInt_ = regInt;
	}
	
	public boolean isReadOnly() {
		return readOnly_;
	}
	
	public int getID() {
		return id_;
	}
	
	public boolean setState(int state) {
		if(!isReadOnly() && isValueAllowed(state)) {
			return regInt_.write(getBaseAddress()+getID(), state);
		}
		return false;
	}
	
	public int getState() {
		return regInt_.read(getBaseAddress() + getID());
	}
	
	public abstract int getBaseAddress();

	public boolean isValueAllowed(int value){
		return ( value >= 0 && value <= getMax() );
	}

	public abstract int getMax();
}
