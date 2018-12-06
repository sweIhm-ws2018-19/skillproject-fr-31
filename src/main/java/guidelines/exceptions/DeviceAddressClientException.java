package main.java.guidelines.exceptions;



public class DeviceAddressClientException extends Exception {

    public DeviceAddressClientException(String message, Exception e) {
        super(message, e);
    }

    public DeviceAddressClientException(String message) {
        super(message);
    }

    public DeviceAddressClientException(Exception e) {
        super(e);
    }

}
