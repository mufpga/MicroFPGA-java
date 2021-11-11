package de.embl.rieslab.microfpga.devices;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CameraParametersTest {

    public final static double eps = 0.000001;

    @Test
    public void testDoubleValues(){

        /*
        pulse, period and exposure go in steps of 0.1 ms,
        while delay goes in steps of 0.01 ms.
         */
        final double pulse = 1.5;
        final double period = 45.8;
        final double exposure = 30.0;
        final double delay = 1.64;

        CameraParameters p = new CameraParameters(
                pulse, period, exposure, delay
        );

        assertEquals(pulse, p.getPulseMs(), eps);
        assertEquals(period, p.getPeriodMs(), eps);
        assertEquals(exposure, p.getExposureMs(), eps);
        assertEquals(delay, p.getDelayMs(), eps);

        // if the values have the wrong precision, they
        // are thresholded to the nearest step
        p.setPulseMs(pulse + 0.01);
        p.setPeriodMs(period - 0.02);
        p.setExposureMs(exposure + 0.07);
        p.setDelayMs(delay - 0.008);

        assertEquals(pulse, p.getPulseMs(), eps);
        assertEquals(period, p.getPeriodMs(), eps);
        assertEquals(exposure+0.1, p.getExposureMs(), eps);
        assertEquals(delay-0.01, p.getDelayMs(), eps);
    }

    @Test
    public void testIntValues(){
        /*
        pulse, period and exposure go in steps of 0.1 ms,
        while delay goes in steps of 0.01 ms.
         */
        final int pulse = 15;
        final int period = 458;
        final int exposure = 300;
        final int delay = 164;

        // use the int constructor
        CameraParameters p = new CameraParameters(
                pulse, period, exposure, delay
        );

        HashMap<String, Integer> vals = p.getIntValues();

        assertEquals(pulse, vals.get(CameraParameters.KEY_PULSE), eps);
        assertEquals(period, vals.get(CameraParameters.KEY_PERIOD), eps);
        assertEquals(exposure, vals.get(CameraParameters.KEY_EXPOSURE), eps);
        assertEquals(delay, vals.get(CameraParameters.KEY_DELAY), eps);
    }
}
