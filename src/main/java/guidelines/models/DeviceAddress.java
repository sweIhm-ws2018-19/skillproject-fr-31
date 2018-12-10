package guidelines.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)

public class DeviceAddress {
    private String stateOrRegion;
    private String city;
    private String countryCode;
    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String districtOrCounty;

    public String getStateOrRegion() {
        return stateOrRegion;
    }

    public void setStateOrRegion(String stateOrRegion) {
        this.stateOrRegion = stateOrRegion;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getDistrictOrCounty() {
        return districtOrCounty;
    }

    public void setDistrictOrCounty(String districtOrCounty) {
        this.districtOrCounty = districtOrCounty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceAddress address = (DeviceAddress) o;
        return Objects.equals(getStateOrRegion(), address.getStateOrRegion()) &&
                Objects.equals(getCity(), address.getCity()) &&
                Objects.equals(getCountryCode(), address.getCountryCode()) &&
                Objects.equals(getPostalCode(), address.getPostalCode()) &&
                Objects.equals(getAddressLine1(), address.getAddressLine1()) &&
                Objects.equals(getAddressLine2(), address.getAddressLine2()) &&
                Objects.equals(getAddressLine3(), address.getAddressLine3()) &&
                Objects.equals(getDistrictOrCounty(), address.getDistrictOrCounty());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStateOrRegion(), getCity(), getCountryCode(), getPostalCode(), getAddressLine1(), getAddressLine2(), getAddressLine3(), getDistrictOrCounty());
    }
}
