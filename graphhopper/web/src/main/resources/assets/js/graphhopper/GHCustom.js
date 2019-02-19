
var GHCustom = function () {
  this.GPXurl = "";
  this.dataType = "json";
  this.do_request = false;
};

GHCustom.prototype.createGPXURL = function(lat,lon){
   this.GPXurl = "http://localhost:8989/gpx?point=" + lat + "%2C" +lon ;
};

GHCustom.prototype.doRequest = function (url ,callback) {

    $.ajax({
        timeout: 30000,
        url: url,
        success: function (json) {
            alert('Ajax request success!');

            if(json.path){
                console.log("Receive!");
            }
            callback(json);
        },
        error: function () {
            this.do_request = false;
            alert('Ajax request error');
        },
        type: "GET",
        dataType: this.dataType,
        crossDomain: true
        //dataType: "text"
    });
};

module.exports = GHCustom;
