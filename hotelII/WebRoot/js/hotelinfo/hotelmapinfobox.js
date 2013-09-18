/* An InfoBox is like an info window, but it displays
* under the marker, opens quicker, and has flexible styling.
* @param {GLatLng} latlng Point to place bar at
* @param {Map} map The map on which to display this InfoBox.
* @param {Object} opts Passes configuration options - content,
*   offsetVertical, offsetHorizontal, className, height, width
*/
function InfoBox(opts) {
    google.maps.OverlayView.call(this);
    this.latlng_ = opts.latlng;
    this.map_ = opts.map;
    this.content_ = opts.content || "";
    this.offsetVertical_ = opts.offsetVertical || -107;//-195;
    this.offsetHorizontal_ = opts.offsetHorizontal || -150;//-128;
    this.height_ = opts.height || 76;
    this.width_ = opts.width || 216;

    var me = this;
    this.boundsChangedListener_ =
    google.maps.event.addListener(this.map_, "bounds_changed", function() {
        return me.panMap.apply(me);
    });

    // Once the properties of this OverlayView are initialized, set its map so
    // that we can display it.  This will trigger calls to panes_changed and
    // draw.
    this.setMap(this.map_);
}

/* InfoBox extends GOverlay class from the Google Maps API
*/
InfoBox.prototype = new google.maps.OverlayView();

/* Creates the DIV representing this InfoBox
*/
InfoBox.prototype.remove = function() {
    if (this.div_) {
        this.div_.parentNode.removeChild(this.div_);
        this.div_ = null;
    }
};

/* Redraw the Bar based on the current projection and zoom level
*/
InfoBox.prototype.draw = function() {
    // Creates the element if it doesn't exist already.
    this.createElement();
    if (!this.div_) return;

    // Calculate the DIV coordinates of two opposite corners of our bounds to
    // get the size and position of our Bar
    var pixPosition = this.getProjection().fromLatLngToDivPixel(this.latlng_);
    if (!pixPosition) return;

    // Now position our DIV based on the DIV coordinates of our bounds
    this.div_.style.width = this.width_ + "px";
    this.div_.style.left = (pixPosition.x + this.offsetHorizontal_) + "px";
    this.div_.style.height = this.height_ + "px";
    this.div_.style.top = (pixPosition.y + this.offsetVertical_) + "px";
    this.div_.style.display = 'block';
};

/* Creates the DIV representing this InfoBox in the floatPane.  If the panes
* object, retrieved by calling getPanes, is null, remove the element from the
* DOM.  If the div exists, but its parent is not the floatPane, move the div
* to the new pane.
* Called from within draw.  Alternatively, this can be called specifically on
* a panes_changed event.
*/
InfoBox.prototype.createElement = function() {
    var panes = this.getPanes();
    var div = this.div_;
    if (!div) {
        // This does not handle changing panes.  You can set the map to be null and
        // then reset the map to move the div.
        div = this.div_ = document.createElement("div");
        div.style.border = "0px none";
        div.style.position = "absolute";
        div.style.background = "url('image/1.gif')";
        div.style.backgroundRepeat="no-repeat";
        div.style.width = 216+"px";//this.width_ + "px";
        div.style.height = 76+"px";//this.height_ + "px";
        var contentDiv = document.createElement("div");
        contentDiv.style.padding = "0px 0px 0px 0px "
        contentDiv.style.margin = "-9px 0px 0px 6px "
        contentDiv.style.width =216+"px";// (this.width_ - 12) + "px";
        contentDiv.style.height = 76+"px";//(this.height_ - 38) + "px";
        //contentDiv.style.overflow = "auto "
        contentDiv.style.cursor = "auto";
        contentDiv.innerHTML = this.content_;


        var topDiv = document.createElement("div");
        topDiv.style.textAlign = "right";
        var closeImg = document.createElement("img");
        closeImg.style.margin = "-4px 0px 0px 0px "
        closeImg.style.width = "30px";
        closeImg.style.height = "30px";
        closeImg.style.cursor = "pointer";
        closeImg.src = "image/closebigger.gif";
        topDiv.appendChild(closeImg);

        function removeInfoBox(ib) {
            return function() {
                ib.setMap(null);
            };
        }

        google.maps.event.addDomListener(closeImg, 'click', removeInfoBox(this));

        div.appendChild(topDiv);
        div.appendChild(contentDiv);
        div.style.display = 'none';
        panes.floatPane.appendChild(div);
        this.panMap();
    } else if (div.parentNode != panes.floatPane) {
        // The panes have changed.  Move the div.
        div.parentNode.removeChild(div);
        panes.floatPane.appendChild(div);
    } else {
        // The panes have not changed, so no need to create or move the div.
    }
}

/* Pan the map to fit the InfoBox.
*/
InfoBox.prototype.panMap = function() {
    // if we go beyond map, pan map
    var map = this.map_;
    var bounds = map.getBounds();
    if (!bounds) return;

    // The position of the infowindow
    var position = this.latlng_;

    // The dimension of the infowindow
    var iwWidth = this.width_;
    var iwHeight = this.height_;

    // The offset position of the infowindow
    var iwOffsetX = this.offsetHorizontal_;
    var iwOffsetY = this.offsetVertical_;

    // Padding on the infowindow
    var padX = 40;
    var padY = 40;

    // The degrees per pixel
    var mapDiv = map.getDiv();
    var mapWidth = mapDiv.offsetWidth;
    var mapHeight = mapDiv.offsetHeight;
    var boundsSpan = bounds.toSpan();
    var longSpan = boundsSpan.lng();
    var latSpan = boundsSpan.lat();
    var degPixelX = longSpan / mapWidth;
    var degPixelY = latSpan / mapHeight;

    // The bounds of the map
    var mapWestLng = bounds.getSouthWest().lng();
    var mapEastLng = bounds.getNorthEast().lng();
    var mapNorthLat = bounds.getNorthEast().lat();
    var mapSouthLat = bounds.getSouthWest().lat();

    // The bounds of the infowindow
    var iwWestLng = position.lng() + (iwOffsetX - padX) * degPixelX;
    var iwEastLng = position.lng() + (iwOffsetX + iwWidth + padX) * degPixelX;
    var iwNorthLat = position.lat() - (iwOffsetY - padY) * degPixelY;
    var iwSouthLat = position.lat() - (iwOffsetY + iwHeight + padY) * degPixelY;

    // calculate center shift
    var shiftLng =
      (iwWestLng < mapWestLng ? mapWestLng - iwWestLng : 0) +
      (iwEastLng > mapEastLng ? mapEastLng - iwEastLng : 0);
    var shiftLat =
      (iwNorthLat > mapNorthLat ? mapNorthLat - iwNorthLat : 0) +
      (iwSouthLat < mapSouthLat ? mapSouthLat - iwSouthLat : 0);

    // The center of the map
    var center = map.getCenter();

    // The new map center
    var centerX = center.lng() - shiftLng;
    var centerY = center.lat() - shiftLat;

    // center the map to the new shifted center
    map.setCenter(new google.maps.LatLng(centerY, centerX));

    // Remove the listener after panning is complete.
    google.maps.event.removeListener(this.boundsChangedListener_);
    this.boundsChangedListener_ = null;
};

var map;
var infoBox=null;
function initialize(lat,lng,name) {  
	 var myOptions = {zoom: 14,
	 				  center: new google.maps.LatLng(lat,lng),
	 				  mapTypeId: google.maps.MapTypeId.ROADMAP}  
	 map = new google.maps.Map(document.getElementById("map_canvas"),myOptions);
	 showMarker(lat,lng,name);
 } 
function showMarker(lat,lng,name){
   var image = new google.maps.MarkerImage('image/mango.gif',       
   new google.maps.Size(20, 32),       
   new google.maps.Point(0,0),       
   new google.maps.Point(0, 32));   
   var shadow = new google.maps.MarkerImage('image/shadow.gif',       
   new google.maps.Size(37, 32),      
   new google.maps.Point(0,0),       
   new google.maps.Point(0, 32));       
   var shape = {coord: [1, 1, 1, 20, 18, 20, 18 , 1],type: 'poly'};
   var myLatLng = new google.maps.LatLng(lat,lng); 
   var marker = new google.maps.Marker({         
   									  position: myLatLng,         
   									  map: map,         
   									  shadow: shadow,         
   									  icon: image,         
   									  shape: shape,         
   									  title: name});
  /*google.maps.event.addListener(marker, "click", function(e) {
    setTimeout(function(){ infoBox = new InfoBox({ latlng: marker.getPosition(), map: map, content: name});},100);
  });*/
 // google.maps.event.trigger(marker, "click");
}
