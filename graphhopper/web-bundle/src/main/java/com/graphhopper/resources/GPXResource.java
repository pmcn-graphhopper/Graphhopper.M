package com.graphhopper.resources;


import com.graphhopper.*;
import com.graphhopper.Database.DBHelper;
import com.graphhopper.GPXUtil.GPXFilter;
import com.graphhopper.http.WebHopper;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 author Yu-Hsiang Lin
  **/

@Path("gpx")

public class GPXResource {

    private final Boolean hasElevation;
    private GHResponse ghResponse;
    private GraphHopper graphHopper;

    private ArrayList<String> GPX_Point_Array;
    private double fromlat,fromlon,tolat,tolon;

    @Inject
    public GPXResource (GraphHopper graphHopper, @Named("hasElevation") Boolean hasElevation) {
        this.graphHopper = graphHopper;
        this.hasElevation = hasElevation;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
    public Response doGet(
            @QueryParam("point") List<GHPoint> gpxPoints,
            @QueryParam("setHome") @DefaultValue("f") String strhome,
            @QueryParam("accuracy")  @DefaultValue("0.0")  String acc,
            @QueryParam("time")  @DefaultValue(" ")  String time,
            @QueryParam("routing") @DefaultValue("f") String route){

        if (route.equalsIgnoreCase("t"))
        {
            GHPoint fromPoint = gpxPoints.get(0);
            fromlat = fromPoint.getLat();
            fromlon = fromPoint.getLon();

            GHPoint toPoint = gpxPoints.get(1);
            tolat = toPoint.getLat();
            tolon = toPoint.getLon();

            System.out.println("from coordinate: "+ fromlat +','+ fromlon);
            System.out.println("to coordinate: "+ tolat +','+ tolon);

            ghResponse = graphHopper.calcPath(fromlat, fromlon,tolat, tolon);

            return Response.ok(WebHopper.RouteJsonObject(ghResponse)).build();

        } else if(strhome.equalsIgnoreCase("t")){

            GHPoint HomePoint = gpxPoints.get(0);
            tolat = HomePoint.getLat();
            tolon = HomePoint.getLon();
            //System.out.println(tolat +","+tolon);

            DBHelper dbHelper = new DBHelper();
            dbHelper.DBConnection();

            return Response.ok(WebHopper.GResponse()).build();
        }
        else {
            GHPoint GpxPoint = gpxPoints.get(0);
            GPX_Point_Array = graphHopper.GPX_Point_record(GpxPoint,acc,time);

            return Response.ok(WebHopper.JsonObject(GPX_Point_Array)).build();
        }
    }

    //v2. @QueryParam + List<GHPoint> #List can point > 2
    /*@GET
    public Response doGet(
            @QueryParam("point") List<GHPoint> gpxPoints){

        String message = "Hello GPX";

        for(int i=0; i<gpxPoints.size(); i++){
            GHPoint point = gpxPoints.get(i);
            System.out.println(point);
        }
        return Response.status(200).entity(message).build();
    }*/

    // v1. Path + @PathParam
    /*@GET
    @Path("/{point}")
    public Response doGet(
        @PathParam("point") String gpxPoint){

        String message = "GPX = "+ gpxPoint;
        System.out.println(message);
        return Response.status(200).entity(message).build();
    }*/

}
