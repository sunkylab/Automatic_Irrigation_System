package com.banque.irrigationsystem.shared.integrations;

public class SensorActivityImpleme implements SensorActivity {

    @Override
    public boolean canLandBeIrrigated() {
        return false;
    }

    @Override
    public boolean irrigateLand() {
        //supply water
        return false;
    }
}
