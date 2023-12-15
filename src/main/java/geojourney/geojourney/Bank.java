package geojourney.geojourney;



public class Bank {
    private final String name;
    private final String address;
    private final double latitude;
    private final double longitude;
    private final double rating;
    private final Boolean openNow;

    public Bank(String name, String address, double latitude, double longitude, double rating, Boolean openNow) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.openNow = openNow;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRating() {
        return rating;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void displayBank() {
        System.out.println("name : " + name);
        System.out.println("address : " + address);
        System.out.println("is open now : " + openNow);
        System.out.println("latitude : " + latitude);
        System.out.println("longitude : " + longitude);
        System.out.println("rating : " + rating);

    }
}
