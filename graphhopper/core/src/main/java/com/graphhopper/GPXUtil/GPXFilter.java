package com.graphhopper.GPXUtil;

import com.graphhopper.util.DistanceCalc2D;
import com.graphhopper.util.DistanceCalcEarth;
import com.graphhopper.util.PointList;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class GPXFilter {
    private DistanceCalcEarth distanceCalcEarth = new DistanceCalcEarth();
    private PointList  FilteredPointList = new PointList();

    private double LimitMaxSpeed = 110;
    private long fromPointTime =0;
    private long toPointTime =0;

    private double AverageAccuracy = 0;
    private int AverageCount =0;

    public PointList FilterSpeedWithAcc(PointListCustom plc_input){

        if(plc_input.size() >= 2){
            for(int k=plc_input.size()-2 ; k < plc_input.size()-1 ; k++){
                if(getSpeed(plc_input,k,k+1) < LimitMaxSpeed && AccuarcyWithFilter(plc_input,k)){
                    FilteredPointList.add(plc_input.getLat(k),plc_input.getLon(k),plc_input.getEle(k));
                }

            }
        }
        return FilteredPointList;
    }


    private double getSpeed(PointListCustom plc_input, int fromNode, int toNode){
        double Speed,fromLat,fromLon,toLat,toLon;
        double distance;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            fromPointTime = simpleDateFormat.parse(plc_input.getTime(fromNode)).getTime();
            toPointTime = simpleDateFormat.parse(plc_input.getTime(toNode)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fromLat = plc_input.getLat(fromNode);
        fromLon = plc_input.getLon(fromNode);
        toLat = plc_input.getLat(toNode);
        toLon = plc_input.getLon(toNode);

        System.out.println("From: " + fromLat + ',' +fromLon);
        System.out.println("To: " + toLat + ',' +toLon);

        distance = distanceCalcEarth.calcDist(fromLat,fromLon,toLat,toLon);
        System.out.println("distance:"+ distance);

        System.out.println("Point time:" + (Math.abs(Double.valueOf(fromPointTime-toPointTime))/1000));
        //System.out.println("To Point time:" + toPointTime);

        Speed = distance / (Math.abs(Double.valueOf(fromPointTime-toPointTime))/1000)*3.6;
        System.out.println("Speed: " + Speed);

        return Speed;
    }

    private boolean AccuarcyWithFilter(PointListCustom plc_input,int index){

        if(plc_input.size <=10){
            return plc_input.getAccuracy(index) <= 99f;
        }
        else {

            for(int i = index >15 ? index - 15 : 0; index + 15 < plc_input.size() -1 ? i < index +15 : i < plc_input.size(); i++){
                AverageAccuracy += plc_input.getAccuracy(i);
                AverageCount++;
            }

            AverageAccuracy = AverageAccuracy / AverageCount;

            if(AverageAccuracy < 7)
                AverageAccuracy = 15;
            else
                AverageAccuracy = Math.ceil((AverageAccuracy * 2) / 10) * 10;

            System.out.println("current point Accuracy:" + plc_input.getAccuracy(index));
            System.out.println("AverageAccuracy:" + AverageAccuracy);

            return plc_input.getAccuracy(index) <= AverageAccuracy;
        }
    }
}
