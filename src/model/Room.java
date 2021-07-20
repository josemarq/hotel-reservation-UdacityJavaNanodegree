package model;

import java.util.Objects;

public class Room implements IRoom {
    private final String roomNumber;
    private final Double price;
    private RoomType enumeration;

    public Room(String roomNumber, Double price, RoomType enumeration) {
    this.roomNumber = roomNumber;
    this.price = price;
    this.enumeration = enumeration;

    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    public RoomType getRoomType() {
        return enumeration;
    }

    public boolean isFree() {
        return price != null || price.equals(0.0);
    }

    @Override
    public String toString() {
        return "Room Number: " + roomNumber + " Price: " + price + " Type of Bed: " + enumeration;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }

        if(!(obj instanceof Room)) {
            return false;
        }

        final Room room = (Room) obj;
        return Objects.equals(this.roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }
}
