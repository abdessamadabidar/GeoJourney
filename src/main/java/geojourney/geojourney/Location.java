package geojourney.geojourney;

public class Location {
    private String address;
    private double longitude;
    private double latitude;

    public String getAddress() {
        return address;
    }

    public Location() {
        address = "";
        longitude = 0.0;
        latitude = 0.0;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
