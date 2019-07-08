package com.graphhopper.GPXUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *Training weight
 *
 author Yu-Hsiang Lin
 **/


public class GPXTraining {

    public double TrainWeighting(double CurrentWeighting, int CurrentTrainTime, int lastTrainTime, String PerTime){
        double EdgeWeighting = 0;

        //if(CurrentTrainTime -lastTrainTime == 0)
        EdgeWeighting = (1 - AbnormalSigmoid(PerTime))  +  AbnormalSigmoid(PerTime) * CurrentWeighting;
        //else
        //    EdgeWeighting = AbnormalSigmoid(PerTime) * CurrentWeighting ;

        return EdgeWeighting;
    }

    private double AbnormalSigmoid(String PerTime){
        double score = 0 , rt = 0;
        long CurrentTime =0, LastTime=0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newTime = new Date();
        String nowTime = simpleDateFormat.format(newTime);

        try{
            CurrentTime = simpleDateFormat.parse(nowTime).getTime();
            LastTime = simpleDateFormat.parse(PerTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        rt = 5 - (Math.log10((Math.abs(CurrentTime-LastTime)/1000) / 3600) / 2);

        score = 1  /  (1 + Math.exp(-rt));

        System.out.println("rt : " + rt + "score:" + score);

        return  score;
    }
}
