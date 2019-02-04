package com.aryan.android.kangaroocust;

public class Vehicle {
    String HotelCity;
    String HotelLocation;
    String HotelName;
    String OriginalCost;
    String ReducedCost;
    String RoomId;
    String VehicleImage;

    public Vehicle(){};

    public String getVehicleImage() {
        return VehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.VehicleImage = vehicleImage;
    }

    public String getOriginalCost() {
        return OriginalCost;
    }

    public void setOriginalCost(String originalCost) {
        OriginalCost = originalCost;
    }

    public String getReducedCost() {
        return ReducedCost;
    }

    public void setReducedCost(String reducedCost) {
        ReducedCost = reducedCost;
    }

    public String getHotelLocation() {
        return HotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        HotelLocation = hotelLocation;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getHotelCity() {
        return HotelCity;
    }

    public void setHotelCity(String hotelCity) {
        HotelCity = hotelCity;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }
}
