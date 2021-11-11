package de.embl.rieslab.microfpga.devices;

public class LaserParameters {

    public final static String KEY_MODE = "Mode";
    public final static String KEY_DURATION = "Duration";
    public final static String KEY_SEQUENCE = "Sequence";

    private LaserTriggerMode mode_;
    private int duration_;
    private int sequence_;

    public LaserParameters(LaserTriggerMode mode, int duration, String sequence){
        setMode(mode);
        setDuration(duration);
        setSequence(sequence);
    }

    public LaserParameters(LaserTriggerMode mode, int duration, int sequence){
        setMode(mode);
        setDuration(duration);
        setSequence(sequence);
    }

    public void setMode(LaserTriggerMode mode){
        mode_ = mode;
    }

    public void setDuration(int duration){
        if(duration >= 0 && duration <= LaserTrigger.Duration.MAX)
            duration_ = duration;
    }

    public void setSequence(String sequence){
        int seq = LaserTrigger.formatSequence(sequence);

        if(seq != -1) sequence_ = seq;
    }

    protected void setSequence(int sequence){
        if(sequence >= 0 && sequence <= LaserTrigger.Sequence.MAX)
            sequence_ = sequence;
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