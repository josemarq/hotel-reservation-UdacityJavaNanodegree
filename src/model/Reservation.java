package model;

import java.util.Date;

public class Reservation {

    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public IRoom getRoom() {
        return this.room;
    }

    public Date getCheckInDate() {
        return this.checkInDate;
    }

    public Date getCheckOutDate() {
        return this.checkOutDate;
    }

    @Override
    public String toString() {
        return "Reservations:\n" + customer + "\nRoom Information:\n--------\n" + room + "\nCheck-in: " + checkInDate + "\nCheck-out: " + checkOutDate;
    }

   public boolean isRoomReserved(Date checkInDate, Date checkOutDate) {
        if (checkInDate.before(this.checkOutDate) && checkOutDate.after(this.checkInDate)) {
            return true;
        }
        return false;
    }
}
