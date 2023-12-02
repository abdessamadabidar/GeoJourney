document.addEventListener("DOMContentLoaded",  () => {
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
    }).setView(POSITION, ZOOM_LEVEL);

   // MAP.zoomControl.setPosition('topright')

    // marker
    const marker = L.marker(POSITION, {icon: CUSTOM_ICON});


    // default tileLayer
    const osm = L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
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

    // const googleSat = L.tileLayer('http://{s}.google.com/vt?lyrs=s&x={x}&y={y}&z={z}',{
    //     maxZoom: 20,
    //     subdomains:['mt0','mt1','mt2','mt3']
    // });

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

})