package main.java.guidelines.utility;

public class Address {
    private final String name;

    private final double latitude;
    private final double longitude;


    public Address(String name, String street, int plz, String city) {
        this.name = name;
        double[] geocodes = HereApi.getGeoCodes(street,city,plz);
        this.latitude = geocodes[0];
        this.longitude = geocodes[1];
    }
}
