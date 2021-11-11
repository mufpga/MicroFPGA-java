package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.regint.RegisterInterface;

public class TTL extends Signal {
	
	public final static int ON = 1;
	public final static int OFF = 0; 
	
	protected TTL(int id, RegisterInterface regint) {
		super(id, regint, false);
	}

	@Override
	public int getMax() {
		return ON;
	}

	@Override
	public int getBaseAddress() {
		return Signal.ADDR_TTL;
	}

}
