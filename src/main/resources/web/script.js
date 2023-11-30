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
    L.marker(POSITION, {icon: CUSTOM_ICON}).addTo(MAP);



    // default tileLayer
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(MAP);




})