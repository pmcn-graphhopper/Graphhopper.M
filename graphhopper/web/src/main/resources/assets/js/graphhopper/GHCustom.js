
var graphhopperTools = require('./tools.js');

var GHCustom = function () {
  this.GPXurl = "";
  this.dataType = "json";
  this.do_request = false;
  this.elevation = false;
};

GHCustom.prototype.createGPXURL = function(lat,lon){
   this.GPXurl = "https://pmcn-graphhopper.tk/gpx?point=" + lat + "%2C" +lon ;
};

GHCustom.prototype.route = function(lat,lon){
    this.GPXurl = "https://pmcn-graphhopper.tk/gpx?point=" + lat + "%2C" +lon +"&routing=t";
}

GHCustom.prototype.hasElevation = function () {
    return this.elevation;
};

GHCustom.prototype.doRequest = function (url ,callback) {
    var that = this;
    $.ajax({
        timeout: 30000,
        url: url,
        success: function (json) {
            alert('Ajax request success!');

            if(json.encoded){
                console.log("Receive!");

                for (var i = 0; i < json.paths.length; i++) {
                    var path = json.paths[i];

                    // convert encoded polyline to geo json
                    var tmpArray = graphhopperTools.decodePath(path.points, that.elevation);
                    path.points = {
                        "type": "LineString",
                        "coordinates": tmpArray
                    };
                }

            }
            callback(json);
        },
        error: function () {
            alert('Ajax request error');
        },
        type: "GET",
        dataType: this.dataType,
        crossDomain: true
        //dataType: "text"
    });
};

module.exports = GHCustom;
