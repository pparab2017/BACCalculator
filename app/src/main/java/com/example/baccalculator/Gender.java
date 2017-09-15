package com.example.baccalculator;

/**
 * Created by pushparajparab on 9/1/16.
 */
public class Gender {

    public boolean isSwitch_Val() {
        return switch_Val;
    }

    private boolean switch_Val ;
    public Gender(boolean switchVal)
    {
        this.switch_Val = switchVal;
    }
    private double rValueForFemale = 0.55;
    private double rValueForMale = 0.68;

    public double GetRValue(Gender g)
    {
        if(g.isSwitch_Val())
        return rValueForMale;
        else return rValueForFemale;
    }
}
