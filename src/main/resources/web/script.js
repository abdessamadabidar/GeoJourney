
// current position
const POSITION = [35.173635, -3.865641]

// custom pin icon
const CUSTOM_ICON = L.icon({
    iconUrl: 'images/icons8-pin-48.png',
    iconSize:     [32, 32],
});

// zoom level
const ZOOM_LEVEL = 7

// custom map
const MAP = L.map('map', {
    zoomControl: false
}).setView(POSITION, ZOOM_LEVEL).setMinZoom(3);


// marker
const marker = L.marker(POSITION, {icon: CUSTOM_ICON});




// default tileLayer
const osm = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
});


// different layers
const cyclOSM = L.tileLayer('https://{s}.tile-cyclosm.openstreetmap.fr/cyclosm/{z}/{x}/{y}.png', {
    attribution: '<a href="https://github.com/cyclosm/cyclosm-cartocss-style/releases" title="CyclOSM - Open Bicycle render">CyclOSM</a> | Map data: &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
});


const googleStreets = L.tileLayer('http://{s}.google.com/vt?lyrs=m&x={x}&y={y}&z={z}',{
    maxZoom: 20,
    subdomains:['mt0','mt1','mt2','mt3']
}).addTo(MAP);

const googleHybrid = L.tileLayer('http://{s}.google.com/vt?lyrs=s,h&x={x}&y={y}&z={z}',{
    maxZoom: 20,
    subdomains:['mt0','mt1','mt2','mt3']
});

const googleSat = L.tileLayer('http://{s}.google.com/vt?lyrs=s&x={x}&y={y}&z={z}',{
    maxZoom: 20,
    subdomains:['mt0','mt1','mt2','mt3']
});

const googleTerrain = L.tileLayer('http://{s}.google.com/vt?lyrs=p&x={x}&y={y}&z={z}',{
    maxZoom: 20,
    subdomains:['mt0','mt1','mt2','mt3']
});


const baseLayers = {
    "OpenStreetMap": osm,
    "Google Hybrid": googleHybrid,
    "Google Streets": googleStreets,
    "Cycle OpenStreetMap": cyclOSM,
    "Google Terrain": googleTerrain
};

const overlays = {
    "Marker": marker,
};

L.control.layers(baseLayers, overlays).addTo(MAP);


function setZoom(id) {
    if (id === "zoomin") {
        MAP.zoomIn();
    }
    else {
        MAP.zoomOut()
    }
}

function changeTileLayer(layer) {
   removeTileLayers();
   MAP.addLayer(baseLayers[layer])

}

function setSatellite() {
    removeTileLayers();
    MAP.addLayer(googleSat);
}

//Locate
const locate = L.control.locate({flyTo: true}).addTo(MAP);
locate.getContainer().style.display = 'none';

function locateMe() {
    locate.start();
}


const removeMarkers = () => {
    MAP.eachLayer(layer => {
        if (layer instanceof L.Marker) {
            layer.remove();
        }
    })
}

const removeTileLayers = () => {
    MAP.eachLayer((activeLayer) => {
        if (activeLayer instanceof L.TileLayer) {
            MAP.removeLayer(activeLayer)
        }
    })
}
const removeCircles = () => {
    MAP.eachLayer(layer => {
        if (layer instanceof L.CircleMarker) {
            layer.remove();
        }
    })
}



const simpleIcon = L.icon({
    iconUrl: 'images/simple.png',
    iconSize: [38, 40],
    iconAnchor: [20, 35],
    popupAnchor: [-3, -76],
});


var circleMarker = new L.circleMarker();


function markLocation(lat, lng) {
    const latlng = L.latLng(lat, lng);
    removeMarkers()
    removeCircles()
    L.marker(latlng, {icon: simpleIcon}).addTo(MAP);
    circleMarker =  L.circleMarker(latlng, {radius: 20}).addTo(MAP);
    MAP.flyTo(latlng, 15)

}

function changeCircleRadius(radius) {
   if (circleMarker) {
       circleMarker.setRadius(radius);
   }
}




// hospital icon marker
const hospitalIcon = L.icon({
    iconUrl: 'images/hospital.png',
    iconSize: [38, 40],
    iconAnchor: [20, 35],
    popupAnchor: [-3, -76],
});

const restaurantIcon = L.icon({
    iconUrl: 'images/restaurant.png',
    iconSize: [38, 40],
    iconAnchor: [20, 35],
    popupAnchor: [-3, -76],
});

const bankIcon = L.icon({
    iconUrl: 'images/bank.png',
    iconSize: [38, 40],
    iconAnchor: [20, 35],
    popupAnchor: [-3, -76],
});


function markHospitals(data) {
    const coordinates = data["coordinates"];
    for (index in coordinates) {
        const latlng = L.latLng(coordinates[index].lat, coordinates[index].lng);
        L.marker(latlng, {icon: hospitalIcon}).bindTooltip(coordinates[index].name).openTooltip().addTo(MAP);
        L.circleMarker(latlng, {radius: 15, color: "#ff2e54"}).addTo(MAP)

    }
    MAP.zoomIn(5);

}


function markRestaurants(data) {
    const coordinates = data["coordinates"];
    for (index in coordinates) {
        const latlng = L.latLng(coordinates[index].lat, coordinates[index].lng);
        L.marker(latlng, {icon: restaurantIcon}).bindTooltip(coordinates[index].name).openTooltip().addTo(MAP);
        L.circleMarker(latlng, {radius: 15, color: "#ff842f"}).addTo(MAP)

    }
    MAP.zoomIn(5);

}



function markBanks(data) {
    const coordinates = data["coordinates"];
    for (index in coordinates) {
        const latlng = L.latLng(coordinates[index].lat, coordinates[index].lng);
        L.marker(latlng, {icon: bankIcon}).bindTooltip(coordinates[index].name).openTooltip().addTo(MAP);
        L.circleMarker(latlng, {radius: 15, color: "#1dd878"}).addTo(MAP)

    }
    MAP.zoomIn(5);

}

function getCurrentPosition() {
    return 
}
navigator.geolocation.getCurrentPosition((position) => {
        console.log(position.coords.latitude, position.coords.longitude)
})