package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.regint.RegisterInterface;

public class Servo extends Signal{

	public static final int MAX = 65535;

	protected Servo(int id, RegisterInterface regInt) {
		super(id, regInt, false);
	}

	@Override
	public int getMax() {
		return MAX;
	}

	@Override
	public int getBaseAddress() {
		return Signal.ADDR_SERVO;
	}
}
