package de.embl.rieslab.microfpga.devices;

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