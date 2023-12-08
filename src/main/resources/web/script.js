
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
}).addTo(MAP);


// different layers
const cyclOSM = L.tileLayer('https://{s}.tile-cyclosm.openstreetmap.fr/cyclosm/{z}/{x}/{y}.png', {
    attribution: '<a href="https://github.com/cyclosm/cyclosm-cartocss-style/releases" title="CyclOSM - Open Bicycle render">CyclOSM</a> | Map data: &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
});


const googleStreets = L.tileLayer('http://{s}.google.com/vt?lyrs=m&x={x}&y={y}&z={z}',{
    maxZoom: 20,
    subdomains:['mt0','mt1','mt2','mt3']
});

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
    MAP.eachLayer((activeLayer) => {
        MAP.removeLayer(activeLayer)
        MAP.addLayer(baseLayers[layer])
    })
}

function setSatellite() {
    MAP.eachLayer((currentLayer) => {
        MAP.removeLayer(currentLayer);
        MAP.addLayer(googleSat);
    })
}

//Locate
const locate = L.control.locate({flyTo: true}).addTo(MAP);
locate.getContainer().style.display = 'none';

function locateMe() {
    locate.start();
}


function searchPlace(query) {
    geoCoder.setQuery(query);
    geoCoder.markGeocode();
}

const addressSearchControl = L.control.addressSearch("c3751eee6c464cc78ccb3b5c4f73d2c4");
MAP.addControl(addressSearchControl);




let res = document.getElementById("response")
let search = document.getElementById("sr")

let jsobj = {}


/* Process a user input: */
const MIN_ADDRESS_LENGTH = 3;
const DEBOUNCE_DELAY = 300;

/* Process a user input: */
search.addEventListener("input", function(e) {
    const currentValue = this.value;


    // Skip empty or short address strings
    if (!currentValue || currentValue.length < MIN_ADDRESS_LENGTH) {
        return false;
    }

    /* Call the Address Autocomplete API with a delay */
    currentTimeout = setTimeout(() => {
        currentTimeout = null;

        /* Create a new promise and send geocoding request */
        const promise = new Promise((resolve, reject) => {
            currentPromiseReject = reject;

            // Get an API Key on https://myprojects.geoapify.com
            const apiKey = "c3751eee6c464cc78ccb3b5c4f73d2c4";

            var url = `https://api.geoapify.com/v1/geocode/autocomplete?text=${encodeURIComponent(currentValue)}&format=json&limit=5&apiKey=${apiKey}`;

            fetch(url)
                .then(response => {
                    currentPromiseReject = null;

                    // check if the call was successful
                    if (response.ok) {
                        response.json().then(data => resolve(data));
                    } else {
                        response.json().then(data => reject(data));
                    }
                });
        });

        promise.then((data) => {
            // here we get address suggestions
            for (const key in data["results"]) {
                jsobj[key.toString()] = data["results"][key].formatted
            }

            console.log(jsobj)

        }, (err) => {
            if (!err.canceled) {
                console.log(err);
            }
        });
    }, DEBOUNCE_DELAY);
});




function setSearchValue(value) {

    let JSObject = {}
    const MIN_ADDRESS_LENGTH = 3;
    const DEBOUNCE_DELAY = 300;
    let currentValue = value
    // Skip empty or short address strings
    if (!currentValue || currentValue.length < MIN_ADDRESS_LENGTH) {
        return false;
    }

    /* Call the Address Autocomplete API with a delay */
    currentTimeout = setTimeout(() => {
        currentTimeout = null;

        /* Create a new promise and send geocoding request */
        const promise = new Promise((resolve, reject) => {
            currentPromiseReject = reject;

            // Get an API Key on https://myprojects.geoapify.com
            const apiKey = "c3751eee6c464cc78ccb3b5c4f73d2c4";

            var url = `https://api.geoapify.com/v1/geocode/autocomplete?text=${encodeURIComponent(currentValue)}&format=json&limit=5&apiKey=${apiKey}`;

            fetch(url)
                .then(response => {
                    currentPromiseReject = null;

                    // check if the call was successful
                    if (response.ok) {
                        response.json().then(data => resolve(data));
                    } else {
                        response.json().then(data => reject(data));
                    }
                });
        });


        promise.then((data) => {
            // here we get address suggestions
            for (const key in data["results"]) {
                JSObject[key.toString()] = data["results"][key].formatted
            }
            let results = document.createTextNode(JSON.stringify(JSObject))
            res.appendChild(results)
            console.log(data)
        }, (err) => {
            if (!err.canceled) {
                console.log(err);
            }
        });
    }, DEBOUNCE_DELAY);



    return JSObject;
}


