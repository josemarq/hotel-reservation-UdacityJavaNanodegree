package model;

import model.RoomType;

public class FreeRoom extends Room {

    public FreeRoom(String roomNumber, RoomType enumeration) {
        super(roomNumber, 0.0, enumeration);
    }

    @Override
    public String toString() {
        //return "Room number: " + roomNumber + " Room Type: " + enumeration + " Price: $0 (Free)";
        return super.toString();
    }
}
