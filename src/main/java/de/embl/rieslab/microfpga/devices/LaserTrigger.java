package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.MicroFPGAController;
import de.embl.rieslab.microfpga.regint.RegisterInterface;

import java.util.HashMap;

public class LaserTrigger {

	private final int id_;
	private final Mode mode_;
	private final Duration duration_;
	private final Sequence sequence_;

	public class Parameters {

		public final static String KEY_MODE = "Mode";
		public final static String KEY_DURATION = "Duration";
		public final static String KEY_SEQUENCE = "Sequence";

		private LaserTriggerMode mode_;
		private int duration_;
		private int sequence_;

		public Parameters(LaserTriggerMode mode, int duration, String sequence){
			setMode(mode);
			setDuration(duration);
			setSequence(sequence);
		}

		public void setMode(LaserTriggerMode mode){
			mode_ = mode;
		}

		public void setDuration(int duration){
			duration_ = duration;
		}

		public void setSequence(String sequence){
			int seq = formatSequence(sequence);

			if(seq != -1) sequence_ = seq;
		}

		public double getPulseMs(){return mode_ / 10.;}

		public double getPeriodMs(){return duration_ / 10.;}

		public double getExposureMs(){return sequence_ / 10.;}

		public double getDelayMs(){return delay_ / 100.;}

		public void setParametersMs(double pulse, double period, double exposure, double delay){
			setMode(pulse);
			setDuration(period);
			setExposureMs(exposure);
			setDelayMs(delay);
		}

		public HashMap<String, Double> getParametersMs(){
			HashMap map = new HashMap<String, Double>(4);

			map.put(KEY_MODE, getPulseMs());
			map.put(KEY_DURATION, getPeriodMs());
			map.put(KEY_SEQUENCE, getExposureMs());
			map.put(KEY_DELAY, getDelayMs());

			return map;
		}

		protected HashMap<String, Integer> getParameters(){
			HashMap map = new HashMap<String, Double>(4);

			map.put(KEY_MODE, mode_);
			map.put(KEY_DURATION, duration_);
			map.put(KEY_SEQUENCE, sequence_);
			map.put(KEY_DELAY, delay_);

			return map;
		}
	}
	
	protected LaserTrigger(int id, RegisterInterface regint) {
		id_ = id;

		mode_ = new Mode(id_, regint);
		duration_ = new Duration(id_, regint);
		sequence_ = new Sequence(id_, regint);
	}

	public boolean setMode(int value) {
		return mode_.setState(value);
	}
	
	public int getMode() {
		return mode_.getState();
	}
	
	public boolean setDuration(int value) {
		return duration_.setState(value);
	}
	
	public int getDuration() {
		return duration_.getState();
	}
	
	public boolean setSequence(int value) {
		return sequence_.setState(value);
	}
	
	public int getSequence() {
		return sequence_.getState();
	}
	
	/**
	 * Takes a binary string composed of 16 characters, either 0 or 1, and converts 
	 * it to integer. 
	 * 
	 * @param s Binary string composed of 0 or 1 and of length 16.
	 * @return Integer value, or -1 if the string is not a binary string or not of length 16.
	 */
	public static int formatSequence(String s) {
		if(!isBits(s) || s.length() != 16){
			return -1;
		}

		return Integer.parseInt(s, 2);
	}

	public static String getSequence(int i){
		if(i<0 || i>Sequence.MAX) return "";

		int value = i;
		StringBuilder sb = new StringBuilder();
		for(int j=15; j>=0; j--){
			if(value <  Math.pow(2, j)){
				value = value - (int) Math.pow(2, j);
				sb.append("1");
			} else {
				sb.append("0");
			}
		}

		return sb.toString();
	}
	
	public static boolean isBits(String s){	
		for(int i=0;i<s.length();i++){
			if(s.charAt(i) != '0' && s.charAt(i) != '1'){
				return false;
			}
		}
		return true;
	}

	public enum LaserTriggerMode{
		OFF(0), ON(1), RISING(2), FALLING(3), FOLLOWING(4);

		private final int value_;

		LaserTriggerMode(int i){
			value_ = i;
		}

		public int getValue(){
			return value_;
		}
	}

	public class Mode extends Signal{

		public static final int MAX = 4;

		protected Mode(int id, RegisterInterface regint) {
			super(id, Direction.OUTPUT, regint);
		}

		@Override
		public int getMax() {return MAX;}

		public boolean setTriggerMode(LaserTriggerMode mode) {
			return regInt_.write(getBaseAddress()+getID(), mode.getValue());
		}

		@Override
		public int getBaseAddress() {
			return MicroFPGAController.ADDR_MODE;
		}
	}
	
	public class Duration extends Signal{

		public static final int MAX = 65535;

		protected Duration(int id, RegisterInterface regint) {
			super(id, Direction.OUTPUT, regint);
		}

		@Override
		public int getMax() {
			return MAX;
		}

		@Override
		public int getBaseAddress() {
			return MicroFPGAController.ADDR_DURA;
		}
	}
	
	public class Sequence extends Signal{

		public static final int MAX = 65535;
		
		protected Sequence(int id, RegisterInterface regint) {
			super(id, Direction.OUTPUT, regint);
		}

		@Override
		public int getMax() {
			return MAX;
		}

		@Override
		public int getBaseAddress() {
			return MicroFPGAController.ADDR_SEQ;
		}
	}
}
