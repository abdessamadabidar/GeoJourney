
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

function markLocation(lat, lng) {
    const latlng = L.latLng(lat, lng);
    removeMarkers()
    L.marker(latlng).addTo(MAP);
    MAP.flyTo(latlng, 15)

}

function markRestaurants(data) {
    const coordinates = data["coordinates"];
    for (index in coordinates) {
        const latlng = L.latLng(coordinates[index].lat, coordinates[index].lng);
        L.marker(latlng).addTo(MAP);
    }
    MAP.zoomIn(5);

}


function markBanks(data) {
    const coordinates = data["coordinates"];
    for (index in coordinates) {
        const latlng = L.latLng(coordinates[index].lat, coordinates[index].lng);
        L.marker(latlng).addTo(MAP);
    }
    MAP.zoomIn(5);

}

function markHospitals(data) {
    const coordinates = data["coordinates"];
    for (index in coordinates) {
        const latlng = L.latLng(coordinates[index].lat, coordinates[index].lng);
        L.marker(latlng).addTo(MAP);
    }
    MAP.zoomIn(5);

}