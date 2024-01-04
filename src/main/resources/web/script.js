
// current position
const CURRENT_POSITION = [35.173867226238855, -3.862133730680905]

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
}).setView(CURRENT_POSITION, ZOOM_LEVEL).setMinZoom(3);


// marker
const marker = L.marker(CURRENT_POSITION, {icon: CUSTOM_ICON});




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

function displayPlaceCard(name, rating, ratingTotal, address, phone, isOpenNow) {
    return `<div class="tooltip-container__flex">` +
                `<div class="tooltip-container__flex__item">` +
                    `<img src="images/icons8-accueil-24.png" />` +
                    `<span class="tooltip-item-text">${name}</span>` +
                `</div>` +
                `<div class="tooltip-container__flex__item">` +
                    `<img src="images/icons8-étoile-24.png" />` +
                    `<span class="tooltip-item-text">${rating} / ${ratingTotal}</span>` +
                `</div>` +
                `<div class="tooltip-container__flex__item">` +
                    `<img src="images/icons8-adresse-24.png" />` +
                    `<span class="tooltip-item-text">${address}</span>` +
                `</div>` +
                `<div class="tooltip-container__flex__item">` +
                    `<img src="images/icons8-téléphone-24.png" />` +
                    phone +
                `</div>` +
                `<div class="tooltip-container__flex__item">` +
                    `<img src="images/icons8-horloge-24.png" />` +
                    isOpenNow +
                `</div>` +
        `</div>`
}

function markHospitals(data) {
    const coordinates = data["coordinates"];
    for (index in coordinates) {
        const latlng = L.latLng(coordinates[index].lat, coordinates[index].lng);
        let isOpenNow = ''
        if (coordinates[index].isOpenNow) isOpenNow = '<span class="tooltip-item-text" style="color: forestgreen">Open</span>'
        else isOpenNow = '<span class="tooltip-item-text" style="color: tomato">Close</span>'
        let phoneNumber = '';
        if (coordinates[index].phone === null) phoneNumber = '<span class="tooltip-item-text">00 00 00 00 00</span>'
        else phoneNumber = `<span className="tooltip-item-text">${coordinates[index].phone}</span>`

        L.marker(latlng, {icon: hospitalIcon}).bindTooltip(displayPlaceCard(coordinates[index].name, coordinates[index].rating, coordinates[index].totalRating, coordinates[index].address, phoneNumber, isOpenNow), {opacity: 1, className: 'tooltip-container'}).openTooltip().addTo(MAP);
        L.circleMarker(latlng, {radius: 15, color: "#ff2e54"}).addTo(MAP)

    }
    MAP.flyTo(CURRENT_POSITION, 10)
}


function markRestaurants(data) {
    const coordinates = data["coordinates"];
    for (index in coordinates) {
        const latlng = L.latLng(coordinates[index].lat, coordinates[index].lng);
        let isOpenNow = ''
        if (coordinates[index].isOpenNow) isOpenNow = '<span class="tooltip-item-text" style="color: forestgreen">Open</span>'
        else isOpenNow = '<span class="tooltip-item-text" style="color: tomato">Close</span>'
        let phoneNumber = '';
        if (coordinates[index].phone === null) phoneNumber = '<span class="tooltip-item-text">00 00 00 00 00</span>'
        else phoneNumber = `<span className="tooltip-item-text">${coordinates[index].phone}</span>`

        L.marker(latlng, {icon: restaurantIcon}).bindTooltip(displayPlaceCard(coordinates[index].name, coordinates[index].rating, coordinates[index].totalRating, coordinates[index].address, phoneNumber, isOpenNow), {opacity: 1, className: 'tooltip-container'}).openTooltip().addTo(MAP);
        L.circleMarker(latlng, {radius: 15, color: "#ff842f"}).addTo(MAP)

    }
    MAP.flyTo(CURRENT_POSITION, 10)
}




function markBanks(data) {
    const coordinates = data["coordinates"];
    for (index in coordinates) {
        const latlng = L.latLng(coordinates[index].lat, coordinates[index].lng);
        let isOpenNow = ''
        if (coordinates[index].isOpenNow) isOpenNow = '<span class="tooltip-item-text" style="color: forestgreen">Open</span>'
        else isOpenNow = '<span class="tooltip-item-text" style="color: tomato">Close</span>'
        let phoneNumber = '';
        if (coordinates[index].phone === null) phoneNumber = '<span class="tooltip-item-text">00 00 00 00 00</span>'
        else phoneNumber = `<span className="tooltip-item-text">${coordinates[index].phone}</span>`
        L.marker(latlng, {icon: bankIcon}).bindTooltip(displayPlaceCard(coordinates[index].name, coordinates[index].rating, coordinates[index].totalRating, coordinates[index].address, phoneNumber, isOpenNow), {opacity: 1, className: 'tooltip-container'}).openTooltip().addTo(MAP);
        L.circleMarker(latlng, {radius: 15, color: "#1dd878"}).addTo(MAP)

    }
    MAP.flyTo(CURRENT_POSITION, 10)
}

function setMarkerOnMyCurrentPosition(position) {
    const latlng = new L.LatLng(position.coords.latitude, position.coords.longitude)
    L.marker(latlng, {icon: simpleIcon}).addTo(MAP);
    circleMarker =  L.circleMarker(latlng, {radius: 20}).addTo(MAP);
    MAP.flyTo(latlng, 15)
}


function locateMe() {
    // Note : No permissions allowed in JavaFx
    // navigator.geolocation.getCurrentPosition(setMarkerOnMyCurrentPosition)
    // Static locating
    const latlng = new L.LatLng(35.173867226238855, -3.862133730680905)
    L.marker(latlng, {icon: simpleIcon}).addTo(MAP);
    circleMarker =  L.circleMarker(latlng, {radius: 20}).addTo(MAP);
    MAP.flyTo(latlng, 15)
}




const route = L.Routing.control({
    waypoints: [
        L.latLng(35.136891459450766, -3.8735033658682374),
        L.latLng(35.13752515730887, -3.848766388816785)
    ],

}).addTo(MAP);

