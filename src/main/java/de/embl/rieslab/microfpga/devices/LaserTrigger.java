package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.regint.RegisterInterface;

public class LaserTrigger {

	private final int id_;
	private final Mode mode_;
	private final Duration duration_;
	private final Sequence sequence_;

	public enum LaserTriggerMode{
		OFF(0), ON(1), RISING(2), FALLING(3), FOLLOWING(4);

		private final int value_;

		LaserTriggerMode(int i){
			value_ = i;
		}

		public int getValue(){
			return value_;
		}

		public static LaserTriggerMode getMode(int i){
			switch (i){
				case 1:
					return ON;
				case 2:
					return RISING;
				case 3:
					return FALLING;
				case 4:
					return FOLLOWING;
				default:
					return OFF;
			}
		}
	}

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

		public Parameters(LaserTriggerMode mode, int duration, int sequence){
			setMode(mode);
			setDuration(duration);
			setSequence(sequence);
		}

		public void setMode(LaserTriggerMode mode){
			mode_ = mode;
		}

		public void setDuration(int duration){
			if(duration >= 0 && duration <= Duration.MAX)
				duration_ = duration;
		}

		public void setSequence(String sequence){
			int seq = formatSequence(sequence);

			if(seq != -1) sequence_ = seq;
		}

		protected void setSequence(int sequence){
			if(sequence >= 0 && sequence <= Sequence.MAX)
				duration_ = sequence;
		}

		public LaserTriggerMode getMode(){return mode_;}

		public int getDuration(){return duration_;}

		public String getFormattedSequence(){return LaserTrigger.stringSequence(sequence_);}

		protected int getSequence(){return sequence_;}

		public void setValues(LaserTriggerMode mode, int duration, String sequence){
			setMode(mode);
			setDuration(duration);
			setSequence(sequence);
		}

		@Override
		public String toString(){
			String s = "["+KEY_MODE+": "+mode_.toString()+", "
					+KEY_DURATION+": "+duration_+" us, "
					+KEY_SEQUENCE+": "+getFormattedSequence()+"]";

			return s;
		}
	}
	
	protected LaserTrigger(int id, RegisterInterface regint) {
		id_ = id;

		mode_ = new Mode(id_, regint);
		duration_ = new Duration(id_, regint);
		sequence_ = new Sequence(id_, regint);
	}

	public boolean setParameters(Parameters p){
		boolean b = mode_.setTriggerMode(p.getMode());
		if(!b) return false;

		b = duration_.setState(p.getDuration());
		if(!b) return false;

		b = sequence_.setState(p.getSequence());

		return b;
	}

	public Parameters getParameters(){

		Parameters p = new Parameters(
				LaserTriggerMode.getMode(mode_.getState()),
				duration_.getState(),
				sequence_.getState()
		);

		return p;
	}

	public String getParametersPretty(){
		return getParameters().toString();
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

	public boolean setSequence(String s) {
		int value = formatSequence(s);

		if(value < 0) return false;

		return sequence_.setState(value);
	}

	public int getSequence() {
		return sequence_.getState();
	}

	public String getStringSequence() {
		return stringSequence(sequence_.getState());
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

	protected static String stringSequence(int i){
		if(i<0 || i>Sequence.MAX) return "";

		int value = i;
		StringBuilder sb = new StringBuilder();
		for(int j=15; j>=0; j--){
			if(value >= Math.pow(2, j)){
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

	public class Mode extends Signal{

		public static final int MAX = 4;

		protected Mode(int id, RegisterInterface regint) {
			super(id, regint, false);
		}

		@Override
		public int getMax() {return MAX;}

		public boolean setTriggerMode(LaserTriggerMode mode) {
			return regInt_.write(getBaseAddress()+getID(), mode.getValue());
		}

		@Override
		public int getBaseAddress() {
			return Signal.ADDR_MODE;
		}
	}
	
	public class Duration extends Signal{

		public static final int MAX = 65535;

		protected Duration(int id, RegisterInterface regint) {
			super(id, regint, false);
		}

		@Override
		public int getMax() {
			return MAX;
		}

		@Override
		public int getBaseAddress() {
			return Signal.ADDR_DURA;
		}
	}
	
	public class Sequence extends Signal{

		public static final int MAX = 65535;
		
		protected Sequence(int id, RegisterInterface regint) {
			super(id, regint, false);
		}

		@Override
		public int getMax() {
			return MAX;
		}

		@Override
		public int getBaseAddress() {
			return Signal.ADDR_SEQ;
		}
	}
}
