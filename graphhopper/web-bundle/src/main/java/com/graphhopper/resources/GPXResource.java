package com.graphhopper.resources;


import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.GraphHopperAPI;
import com.graphhopper.http.WebHopper;
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

    //private final GraphHopperAPI graphHopper;
    private final Boolean hasElevation;

    private GHResponse ghResponse;
    private GHRequest request;

    private GraphHopper graphHopper;

    private ArrayList<String> GPX_Point_Array;

    @Inject
    public GPXResource (GraphHopper graphHopper, @Named("hasElevation") Boolean hasElevation) {
        this.graphHopper = graphHopper;
        this.hasElevation = hasElevation;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response doGet(
            @QueryParam("point") GHPoint gpxPoints){

        String message = "Hello GPX";

        GPX_Point_Array = graphHopper.GPX_Point_record(gpxPoints);

        System.out.println(message);

        return Response.ok(WebHopper.JsonObject(GPX_Point_Array)).build();

        //return Response.status(200).entity(message).build();
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
