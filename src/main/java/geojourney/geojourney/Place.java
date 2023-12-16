package geojourney.geojourney;

public class Place {
    private final String name;
    private final String address;
    private final double latitude;
    private final double longitude;
    private final double rating;
    private final Boolean openNow;
    private final String phone;
    private final int totalRating;

    public Place(String name, String address, double latitude, double longitude, double rating, Boolean openNow, String phone, int totalRating) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.openNow = openNow;
        this.phone = phone;
        this.totalRating = totalRating;
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

    public int getTotalRating() {
        return totalRating;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public String getPhone() {
        return phone;
    }

    public void displayPlaceDetails() {
        System.out.println("#######################################");
        System.out.println("name : " + name);
        System.out.println("address : " + address);
        System.out.println("is open now : " + openNow);
        System.out.println("latitude : " + latitude);
        System.out.println("longitude : " + longitude);
        System.out.println("rating : " + rating);
        System.out.println("total rating : " + totalRating);
        System.out.println("phone number : " + phone);
        System.out.println("#######################################");
    }
}
