package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.regint.RegisterInterface;

public class PWM extends Signal{
	
	public static final int MAX = 255;

	protected PWM(int id, RegisterInterface regInt) {
		super(id, regInt, false);
	}

	@Override
	public int getMax() {
		return MAX;
	}

	@Override
	public int getBaseAddress() {
		return Signal.ADDR_PWM;
	}
}
