package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.regint.RegisterInterface;

public class AnalogInput extends Signal{
	
	protected AnalogInput(int id, RegisterInterface regint) {
		super(id, regint, true);
	}

	@Override
	public boolean isValueAllowed(int i) {
		return false;
	}

	@Override
	public int getMax() {
		return -1;
	}

	@Override
	public int getBaseAddress() {
		return Signal.ADDR_ANALOG_INPUT;
	}

}
