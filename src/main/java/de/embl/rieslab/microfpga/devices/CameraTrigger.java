package de.embl.rieslab.microfpga.devices;

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

        public static CameraTriggerMode getMode(int i){
            switch (i){
                case 1:
                    return ACTIVE;
                default:
                    return PASSIVE;
            }
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

    public boolean isActiveTrigger() {return mode_.getTriggerMode() == CameraTriggerMode.ACTIVE.getValue();}

    public boolean start(){
        return start_.start();
    }

    public boolean stop(){
        return start_.stop();
    }

    public boolean setParameters(CameraParameters params){
        HashMap<String, Integer> map = params.getIntValues();

        boolean b = pulse_.setState(map.get(CameraParameters.KEY_PULSE));
        if(!b) return false;

        b = period_.setState(map.get(CameraParameters.KEY_PERIOD));
        if(!b) return false;

        b = exposure_.setState(map.get(CameraParameters.KEY_EXPOSURE));
        if(!b) return false;

        b = delay_.setState(map.get(CameraParameters.KEY_DELAY));

        return b;
    }

    public CameraParameters getParameters(){

        CameraParameters p = new CameraParameters(
                pulse_.getState(),
                period_.getState(),
                exposure_.getState(),
                delay_.getState()
        );

        return p;
    }

    public String getParametersPretty() {
        return getParameters().toString();
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

        public int getTriggerMode(){
            return regInt_.read(getBaseAddress());
        }

        @Override
        public int getBaseAddress() {
            return Signal.ADDR_ACTIVE_TRIGGER;
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
            return Signal.ADDR_START_TRIGGER;
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
            return Signal.ADDR_CAM_PULSE;
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
            return Signal.ADDR_CAM_PERIOD;
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
            return Signal.ADDR_CAM_EXPO;
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
            return Signal.ADDR_LASER_DELAY;
        }
    }
}
