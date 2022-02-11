package de.embl.rieslab.microfpga.devices;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CameraParametersTest {

    public final static double eps = 0.000001;

    @Test
    public void testDoubleValues(){

        final double pulse = 1.5;
        final double readout = 2.8;
        final double exposure = 30.0;
        final double delay = 1.64;

        CameraParameters p = new CameraParameters(
                pulse, delay, exposure, readout
        );

        assertEquals(pulse, p.getPulseMs(), eps);
        assertEquals(delay, p.getDelayMs(), eps);
        assertEquals(exposure, p.getExposureMs(), eps);
        assertEquals(readout, p.getReadoutMs(), eps);

        // if the values have the wrong precision (last digit is smaller than a
        // micro-second), they are thresholded to the nearest step (nearest us value)
        p.setPulseMs(pulse + 0.0001);
        p.setDelayMs(delay - 0.0008);
        p.setExposureMs(exposure + 0.0007);
        p.setReadoutMs(readout - 0.0002);

        assertEquals(pulse, p.getPulseMs(), eps);
        assertEquals(delay-0.001, p.getDelayMs(), eps);
        assertEquals(exposure+0.001, p.getExposureMs(), eps);
        assertEquals(readout, p.getReadoutMs(), eps);
    }

    @Test
    public void testIntValues(){
        /*
        pulse, readout and exposure go in steps of 0.1 ms,
        while delay goes in steps of 0.01 ms.
         */
        final int pulse = 15;
        final int delay = 164;
        final int exposure = 300;
        final int readout = 158;

        // use the int constructor
        CameraParameters p = new CameraParameters(
                pulse, delay, exposure, readout
        );

        HashMap<String, Integer> vals = p.getIntValues();

        assertEquals(pulse, vals.get(CameraParameters.KEY_PULSE), eps);
        assertEquals(delay, vals.get(CameraParameters.KEY_DELAY), eps);
        assertEquals(exposure, vals.get(CameraParameters.KEY_EXPOSURE), eps);
        assertEquals(readout, vals.get(CameraParameters.KEY_READOUT), eps);
    }
}
