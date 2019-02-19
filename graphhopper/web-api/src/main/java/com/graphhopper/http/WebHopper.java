package com.graphhopper.http;


import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint;

import java.util.ArrayList;

/**
 * Code which handles polyline encoding and other web stuff.
 *
 author Yu-Hsiang Lin
 **/


public class WebHopper {

    private static Boolean pointsEncoded = true;

    public static ObjectNode JsonObject(ArrayList<String> pointList){

        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("path", pointsEncoded);
        json.putPOJO("GPX_Point",pointList);

        return json;
    }
}
