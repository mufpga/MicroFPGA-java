package de.embl.rieslab.microfpga.devices;

import java.util.HashMap;

/**
 * Class holding the camera trigger module parameters and
 * allowing users to pass them in ms.
 *
 * Maximum delay and readout are 65,535 ms, while
 * the maximum pulse and exposure are 1048,575 ms.
 */
public class CameraParameters {

    public final static String KEY_PULSE = "Pulse";
    public final static String KEY_READOUT = "Read-out";
    public final static String KEY_EXPOSURE = "Exposure";
    public final static String KEY_DELAY = "Delay";

    private int pulse_;
    private int readout_;
    private int exposure_;
    private int delay_;

    public CameraParameters(double pulse, double delay, double exposure, double readout){
        setPulseMs(pulse);
        setReadoutMs(readout);
        setExposureMs(exposure);
        setDelayMs(delay);
    }

    protected CameraParameters(int pulse, int delay, int exposure, int readout){
        pulse_ = pulse;
        readout_ = readout;
        exposure_ = exposure;
        delay_ = delay;
    }

    public void setPulseMs(double pulseMs){
        pulse_ = (int) (pulseMs*1000+0.5);

        if(pulse_ > CameraTrigger.Pulse.MAX) {
            pulse_ = CameraTrigger.Pulse.MAX;
        } else if(pulse_ < 0){
            pulse_ = 0;
        }
    }

    public void setReadoutMs(double readoutMs){
        readout_ = (int) (readoutMs*1000+0.5);

        if(readout_ > CameraTrigger.Readout.MAX) {
            readout_ = CameraTrigger.Readout.MAX;
        } else if(readout_ < 0){
            readout_ = 0;
        }
    }

    public void setExposureMs(double exposureMs){
        exposure_ = (int) (exposureMs*1000+0.5);

        if(exposure_ > CameraTrigger.Exposure.MAX) {
            exposure_ = CameraTrigger.Exposure.MAX;
        } else if(exposure_ < 0){
            exposure_ = 0;
        }
    }

    public void setDelayMs(double delayMs){
        delay_ = (int) (delayMs*1000+0.5);

        if(pulse_ > CameraTrigger.Exposure.MAX) {
            pulse_ = CameraTrigger.Exposure.MAX;
        } else if(pulse_ < 0){
            pulse_ = 0;
        }
    }

    public double getPulseMs(){return pulse_ / 1000.;}

    public double getReadoutMs(){return readout_ / 1000.;}

    public double getExposureMs(){return exposure_ / 1000.;}

    public double getDelayMs(){return delay_ / 1000.;}

    public void setValuesMs(double pulse, double delay, double exposure, double readout){
        setPulseMs(pulse);
        setReadoutMs(readout);
        setExposureMs(exposure);
        setDelayMs(delay);
    }

    public HashMap<String, Double> getValuesMs(){
        HashMap map = new HashMap<String, Double>(4);

        map.put(KEY_PULSE, getPulseMs());
        map.put(KEY_READOUT, getReadoutMs());
        map.put(KEY_EXPOSURE, getExposureMs());
        map.put(KEY_DELAY, getDelayMs());

        return map;
    }

    protected HashMap<String, Integer> getIntValues(){
        HashMap map = new HashMap<String, Double>(4);

        map.put(KEY_PULSE, pulse_);
        map.put(KEY_READOUT, readout_);
        map.put(KEY_EXPOSURE, exposure_);
        map.put(KEY_DELAY, delay_);

        return map;
    }

    @Override
    public String toString(){
        String s = "["+KEY_PULSE+": "+getPulseMs()+" ms, "
                + KEY_DELAY +": "+ getDelayMs()+" ms, "
                +KEY_EXPOSURE+": "+getExposureMs()+" ms, "
                +KEY_READOUT+": "+getReadoutMs()+" ms]";

        return s;
    }
}