package com.graphhopper.GPXUtil;

public class GPXTraining {

    private double wi_alpha =0.05;

    public double TrainWeighting(double CurrentWeighting, int CurrentTrainTime, int lastTrainTime ){
        double EdgeWeighting = 0;

        EdgeWeighting = wi_alpha  + Math.pow((1 - wi_alpha), CurrentTrainTime - lastTrainTime + 1) * CurrentWeighting;

        return EdgeWeighting;
    }
}
