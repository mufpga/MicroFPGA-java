package de.embl.rieslab.microfpga.devices;

import java.util.HashMap;

/**
 * Class holding the camera trigger module parameters and
 * allowing users to pass them in ms.
 *
 * Maximum pulse, period and exposure are 6553,5 ms, while
 * the maximum delay is 655,35 ms.
 */
public class CameraParameters {

    public final static String KEY_PULSE = "Pulse";
    public final static String KEY_PERIOD = "Period";
    public final static String KEY_EXPOSURE = "Exposure";
    public final static String KEY_DELAY = "Delay";

    private int pulse_;
    private int period_;
    private int exposure_;
    private int delay_;

    public CameraParameters(double pulse, double period, double exposure, double delay){
        setPulseMs(pulse);
        setPeriodMs(period);
        setExposureMs(exposure);
        setDelayMs(delay);
    }

    protected CameraParameters(int pulse, int period, int exposure, int delay){
        pulse_ = pulse;
        period_ = period;
        exposure_ = exposure;
        delay_ = delay;
    }

    public void setPulseMs(double pulse){
        pulse_ = (int) (pulse*10+0.5);

        if(pulse_ > CameraTrigger.Pulse.MAX) {
            pulse_ = CameraTrigger.Pulse.MAX;
        } else if(pulse_ < 0){
            pulse_ = 0;
        }
    }

    public void setPeriodMs(double period){
        period_ = (int) (period*10+0.5);

        if(period_ > CameraTrigger.Period.MAX) {
            period_ = CameraTrigger.Period.MAX;
        } else if(period_ < 0){
            period_ = 0;
        }
    }

    public void setExposureMs(double exposure){
        exposure_ = (int) (exposure*10+0.5);

        if(exposure_ > CameraTrigger.Exposure.MAX) {
            exposure_ = CameraTrigger.Exposure.MAX;
        } else if(exposure_ < 0){
            exposure_ = 0;
        }
    }

    public void setDelayMs(double delay){
        delay_ = (int) (delay*100+0.5);

        if(pulse_ > CameraTrigger.Exposure.MAX) {
            pulse_ = CameraTrigger.Exposure.MAX;
        } else if(pulse_ < 0){
            pulse_ = 0;
        }
    }

    public double getPulseMs(){return pulse_ / 10.;}

    public double getPeriodMs(){return period_ / 10.;}

    public double getExposureMs(){return exposure_ / 10.;}

    public double getDelayMs(){return delay_ / 100.;}

    public void setValuesMs(double pulse, double period, double exposure, double delay){
        setPulseMs(pulse);
        setPeriodMs(period);
        setExposureMs(exposure);
        setDelayMs(delay);
    }

    public HashMap<String, Double> getValuesMs(){
        HashMap map = new HashMap<String, Double>(4);

        map.put(KEY_PULSE, getPulseMs());
        map.put(KEY_PERIOD, getPeriodMs());
        map.put(KEY_EXPOSURE, getExposureMs());
        map.put(KEY_DELAY, getDelayMs());

        return map;
    }

    protected HashMap<String, Integer> getIntValues(){
        HashMap map = new HashMap<String, Double>(4);

        map.put(KEY_PULSE, pulse_);
        map.put(KEY_PERIOD, period_);
        map.put(KEY_EXPOSURE, exposure_);
        map.put(KEY_DELAY, delay_);

        return map;
    }

    @Override
    public String toString(){
        String s = "["+KEY_PULSE+": "+getPulseMs()+" ms, "
                +KEY_PERIOD+": "+getPeriodMs()+" ms, "
                +KEY_EXPOSURE+": "+getExposureMs()+" ms, "
                +KEY_DELAY+": "+getDelayMs()+" ms]";

        return s;
    }
}