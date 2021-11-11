package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.regint.RegisterInterface;

public abstract class Signal {

	private final boolean readOnly_;
	private final int id_;
	protected final RegisterInterface regInt_;
	
	protected Signal(int id, RegisterInterface regint, boolean readOnly) {
		id_ = id;
		readOnly_ = readOnly;
		regInt_ = regint;
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
