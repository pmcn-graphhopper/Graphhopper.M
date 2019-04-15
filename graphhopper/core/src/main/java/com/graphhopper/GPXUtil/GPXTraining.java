package com.graphhopper.GPXUtil;

/**
 *Train weight
 *
 author Yu-Hsiang Lin
 **/


public class GPXTraining {

    public double TrainWeighting(double CurrentWeighting, int CurrentTrainTime, int lastTrainTime ){
        double EdgeWeighting = 0;
        double wi_alpha =0.05;

        EdgeWeighting = wi_alpha  + Math.pow((1 - wi_alpha), CurrentTrainTime - lastTrainTime + 1) * CurrentWeighting;

        return EdgeWeighting;
    }
}
