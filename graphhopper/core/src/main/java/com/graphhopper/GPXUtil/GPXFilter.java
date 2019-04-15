package com.graphhopper.GPXUtil;

import com.graphhopper.util.DistanceCalc2D;
import com.graphhopper.util.DistanceCalcEarth;
import com.graphhopper.util.PointList;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 author Yu-Hsiang Lin
 **/


public class GPXFilter {
    private DistanceCalcEarth distanceCalcEarth = new DistanceCalcEarth();

    private long fromPointTime =0;
    private long toPointTime =0;
    private double AverageAccuracy = 0;

    public PointListCustom FilterSpeedWithAcc(PointListCustom plc_input){

        PointListCustom  FilteredPointList = new PointListCustom();

        if(plc_input.size() >= 2){
            for(int k=0 ; k < plc_input.size()-1 ; k++){
                double limitMaxSpeed = 100;
                double NoMove = 0;
                double NowSpeed = getSpeed(plc_input,k,k+1);
                if( NowSpeed < limitMaxSpeed && NowSpeed != NoMove && AccuarcyWithFilter(plc_input,k)){
                    FilteredPointList.add(plc_input.getLat(k),plc_input.getLon(k),plc_input.getEle(k),plc_input.getAccuracy(k),plc_input.getTime(k));
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

        distance = distanceCalcEarth.calcDist(fromLat,fromLon,toLat,toLon);

        Speed = distance / (Math.abs(Double.valueOf(fromPointTime-toPointTime))/1000)*3.6;

        if(Speed > 0){
            System.out.println("From: " + fromLat + ',' +fromLon);
            System.out.println("To: " + toLat + ',' +toLon);
            System.out.println("Speed: " + Speed);
            System.out.println("distance:"+ distance);
        }

        return Speed;
    }

    private boolean AccuarcyWithFilter(PointListCustom plc_input,int index){

        if(plc_input.size <=10){
            System.out.println("current point Accuracy:" + plc_input.getAccuracy(index));
            System.out.println(" ");
            return plc_input.getAccuracy(index) <= 99f;
        }
        else {

            if(plc_input.getAccuracy(index) > 200)
                return false;

            int AverageCount =0;

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
            System.out.println(" ");
            return plc_input.getAccuracy(index) <= AverageAccuracy;
        }
    }

    //detect stay place, it not use history gpx
    public PointListCustom PlaceStayCheck(PointListCustom plc_input){

        //detect by distance
        int CurrentGPXIndex = plc_input.size() - 1;
        int PreviousGPXIndex = plc_input.size() - 2;
        // distance from calc current place - previous place
        double distance = distanceCalcEarth.calcDist(plc_input.getLat(PreviousGPXIndex),plc_input.getLon(PreviousGPXIndex),plc_input.getLat(CurrentGPXIndex),plc_input.getLon(CurrentGPXIndex));
        int stayCheckDistance = 50;
        int stayCheckTime = 3;
        long PreviousTime =0;
        long CurrentTime=0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PointListCustom plcStayPlace = new PointListCustom();

        //if distance < 50m
        if(distance < stayCheckDistance){
            //check to stay place when to start
            for(int TrackPrev = PreviousGPXIndex -1 ; TrackPrev > 0 ; TrackPrev--){

                double checkStartStay = distanceCalcEarth.calcDist(plc_input.getLat(TrackPrev),plc_input.getLon(TrackPrev),
                        plc_input.getLat(CurrentGPXIndex),plc_input.getLon(CurrentGPXIndex));

                // if true, the Previous Node maybe Stay Place
                if(checkStartStay > stayCheckDistance){
                    // time check , set time as 10 min
                    try {
                        PreviousTime = simpleDateFormat.parse(plc_input.getTime(TrackPrev+1)).getTime();
                        CurrentTime = simpleDateFormat.parse(plc_input.getTime(CurrentGPXIndex)).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(Math.abs(CurrentTime-PreviousTime) >= stayCheckTime * 60 *1000){
                        plcStayPlace.add(plc_input.getLat(TrackPrev+1),plc_input.getLon(TrackPrev+1),Double.NaN,0,plc_input.getTime(TrackPrev+1));
                        TrackPrev = 0;
                    }
                }
            }
        }
        System.out.println(" ");
        System.out.println("add Stay Node: " + plcStayPlace);
        System.out.println(" ");
        return plcStayPlace;
    }

    //filter stay place same point
    public PointListCustom SamePointFiltered(PointListCustom plc_stay){

        PointListCustom plc_SamePoint_Filtered;
        plc_SamePoint_Filtered = plc_stay.clone(false);
        int FilterSameDistance = 40;
        int plc_stay_size = plc_stay.size();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for(int plc_start = 0; plc_start < plc_stay_size -1; plc_start++){
            for(int plc_everyCheck = plc_start +1; plc_everyCheck < plc_stay_size; plc_everyCheck++){
                if(distanceCalcEarth.calcDist(plc_stay.getLat(plc_start),plc_stay.getLon(plc_start),plc_stay.getLat(plc_everyCheck),plc_stay.getLon(plc_everyCheck))< FilterSameDistance){

                    long startTime =0;
                    long nextTime=0;

                    try{
                        startTime =  simpleDateFormat.parse(plc_SamePoint_Filtered.getTime(plc_start)).getTime();
                        nextTime = simpleDateFormat.parse(plc_SamePoint_Filtered.getTime(plc_everyCheck)).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(startTime > nextTime)
                        plc_SamePoint_Filtered.setPLC(plc_start,plc_SamePoint_Filtered.getLat(plc_start),plc_SamePoint_Filtered.getLon(plc_start),
                                Double.NaN,0,plc_SamePoint_Filtered.getTime(plc_everyCheck));

                    plc_SamePoint_Filtered.remove(plc_everyCheck);
                    plc_everyCheck--;
                    plc_stay_size = plc_SamePoint_Filtered.size();
                }
            }
        }
        return  plc_SamePoint_Filtered;
    }
}
