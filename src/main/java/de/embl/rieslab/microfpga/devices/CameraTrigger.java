package de.embl.rieslab.microfpga.devices;

import de.embl.rieslab.microfpga.MicroFPGAController;
import de.embl.rieslab.microfpga.regint.RegisterInterface;

import java.util.HashMap;

public class CameraTrigger {

    public enum CameraTriggerMode{
        ACTIVE(1), PASSIVE(0);

        private final int val;

        CameraTriggerMode(int i){
            val = i;
        }

        public int getValue(){
            return val;
        }
    }

    /**
     * Class holding the camera trigger module parameters and
     * allowing users to pass them in ms.
     *
     * Maximum pulse, period and exposure are 6553,5 ms, while
     * the maximum delay is 655,35 ms.
     */
    public class Parameters {

        public final static String KEY_PULSE = "Pulse";
        public final static String KEY_PERIOD = "Period";
        public final static String KEY_EXPOSURE = "Exposure";
        public final static String KEY_DELAY = "Delay";

        private int pulse_;
        private int period_;
        private int exposure_;
        private int delay_;

        public Parameters(double pulse, double period, double exposure, double delay){
            setPulseMs(pulse);
            setPeriodMs(period);
            setExposureMs(exposure);
            setDelayMs(delay);
        }

        public void setPulseMs(double pulse){
            pulse_ = (int) (pulse*10+0.5);

            if(pulse_ > Pulse.MAX) {
                pulse_ = Pulse.MAX;
            } else if(pulse_ < 0){
                pulse_ = 0;
            }
        }

        public void setPeriodMs(double period){
            period_ = (int) (period*10+0.5);

            if(period_ > Period.MAX) {
                period_ = Period.MAX;
            } else if(period_ < 0){
                period_ = 0;
            }
        }

        public void setExposureMs(double exposure){
            exposure_ = (int) (exposure*10+0.5);

            if(exposure_ > Exposure.MAX) {
                exposure_ = Exposure.MAX;
            } else if(exposure_ < 0){
                exposure_ = 0;
            }
        }

        public void setDelayMs(double delay){
            delay_ = (int) (delay*100+0.5);

            if(pulse_ > Exposure.MAX) {
                pulse_ = Exposure.MAX;
            } else if(pulse_ < 0){
                pulse_ = 0;
            }
        }

        public double getPulseMs(){return pulse_ / 10.;}

        public double getPeriodMs(){return period_ / 10.;}

        public double getExposureMs(){return exposure_ / 10.;}

        public double getDelayMs(){return delay_ / 100.;}

        public void setParametersMs(double pulse, double period, double exposure, double delay){
            setPulseMs(pulse);
            setPeriodMs(period);
            setExposureMs(exposure);
            setDelayMs(delay);
        }

        public HashMap<String, Double> getParametersMs(){
            HashMap map = new HashMap<String, Double>(4);

            map.put(KEY_PULSE, getPulseMs());
            map.put(KEY_PERIOD, getPeriodMs());
            map.put(KEY_EXPOSURE, getExposureMs());
            map.put(KEY_DELAY, getDelayMs());

            return map;
        }

        protected HashMap<String, Integer> getParameters(){
            HashMap map = new HashMap<String, Double>(4);

            map.put(KEY_PULSE, pulse_);
            map.put(KEY_PERIOD, period_);
            map.put(KEY_EXPOSURE, exposure_);
            map.put(KEY_DELAY, delay_);

            return map;
        }
    }

    private static CameraTrigger cameraTrigger;
    private final Mode mode_;
    private final Start start_;
    private final Pulse pulse_;
    private final Period period_;
    private final Exposure exposure_;
    private final Delay delay_;

    private CameraTrigger(RegisterInterface regInt){
        mode_ = new Mode(regInt);
        start_ = new Start(regInt);
        pulse_ = new Pulse(regInt);
        period_ = new Period(regInt);
        exposure_ = new Exposure(regInt);
        delay_ = new Delay(regInt);
    }

    public static CameraTrigger getInstance(RegisterInterface regInt){
        if(cameraTrigger == null){
            cameraTrigger = new CameraTrigger(regInt);
        }
        return cameraTrigger;
    }

    public boolean setActiveTrigger(){
        return mode_.setTriggerMode(CameraTriggerMode.ACTIVE);
    }

    public boolean setPassiveTrigger(){
        return mode_.setTriggerMode(CameraTriggerMode.PASSIVE);
    }

    public boolean start(){
        return start_.start();
    }

    public boolean stop(){
        return start_.stop();
    }

    public boolean setParameters(Parameters params){
        HashMap<String, Integer> map = params.getParameters();

        boolean b = pulse_.setState(map.get(Parameters.KEY_PULSE));
        if(!b) return false;

        b = period_.setState(map.get(Parameters.KEY_PERIOD));
        if(!b) return false;

        b = exposure_.setState(map.get(Parameters.KEY_EXPOSURE));
        if(!b) return false;

        b = delay_.setState(map.get(Parameters.KEY_DELAY));

        return b;
    }

    public class Mode extends Signal{

        public static final int MAX = 1;

        protected Mode(RegisterInterface regInt) {
            super(0, regInt, false);
        }

        @Override
        public int getMax() {return MAX;}

        public boolean setTriggerMode(CameraTrigger.CameraTriggerMode mode) {
            return regInt_.write(getBaseAddress(), mode.getValue());
        }

        @Override
        public int getBaseAddress() {
            return MicroFPGAController.ADDR_ACTIVE_TRIGGER;
        }
    }

    public class Start extends Signal{

        public static final int MAX = 1;

        protected Start(RegisterInterface regInt) {
            super(0, regInt, false);
        }

        @Override
        public int getMax() {return MAX;}

        public boolean start() {
            return regInt_.write(getBaseAddress(), 1);
        }

        public boolean stop() {
            return regInt_.write(getBaseAddress(), 0);
        }

        @Override
        public int getBaseAddress() {
            return MicroFPGAController.ADDR_START_TRIGGER;
        }
    }

    public class Pulse extends Signal{

        public static final int MAX = 65535;

        protected Pulse(RegisterInterface regInt) {
            super(0, regInt, false);
        }

        @Override
        public int getMax() {
            return MAX;
        }

        @Override
        public int getBaseAddress() {
            return MicroFPGAController.ADDR_CAM_PULSE;
        }
    }

    public class Period extends Signal{

        public static final int MAX = 65535;

        protected Period(RegisterInterface regInt) {
            super(0, regInt, false);
        }

        @Override
        public int getMax() {
            return MAX;
        }

        @Override
        public int getBaseAddress() {
            return MicroFPGAController.ADDR_CAM_PERIOD;
        }
    }

    public class Exposure extends Signal{

        public static final int MAX = 65535;

        protected Exposure(RegisterInterface regInt) {
            super(0, regInt, false);
        }

        @Override
        public int getMax() {
            return MAX;
        }

        @Override
        public int getBaseAddress() {
            return MicroFPGAController.ADDR_CAM_EXPO;
        }
    }

    public class Delay extends Signal{

        public static final int MAX = 65535;

        protected Delay(RegisterInterface regInt) {
            super(0, regInt, false);
        }

        @Override
        public int getMax() {
            return MAX;
        }

        @Override
        public int getBaseAddress() {
            return MicroFPGAController.ADDR_LASER_DELAY;
        }
    }
}
