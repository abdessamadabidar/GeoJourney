package geojourney.geojourney;

public class Restaurant {
    private final String name;
    private final double rating;
    private final String address;
    private final String phoneNumber;
    private final Boolean openNow;
    private final double latitude;
    private final double longitude;
    public Restaurant(String name, double rating, String address, String phoneNumber, Boolean openNow, double latitude, double longitude) {
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openNow = openNow;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void displayRestaurant() {
        System.out.println("name : " + name);
        System.out.println("address : " + address);
        System.out.println("phone number : " + phoneNumber);
        System.out.println("is open now : " + openNow);
        System.out.println("latitude : " + latitude);
        System.out.println("longitude : " + longitude);
        System.out.println("rating : " + rating);

    }
}
