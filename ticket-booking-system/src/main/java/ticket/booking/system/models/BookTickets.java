package ticket.booking.system.models;
import java.time.LocalDateTime;

public class BookTickets {

	int refNumber;
	int numbOfSeats = 0;
	String seatNames = "";
	public boolean bookingConfirmed = false;
	LocalDateTime timeOfHold;
	
	/**
	 * Constructor of BookTickets class which is used to initialize an object with number of seats required and the seats booked. 
	 * @param numbOfSeatsRequested Number of seats requested by the user.
	 * @param seat Seats which are assigned to a particular object of this class.
	 */
	public BookTickets(int numbOfSeatsRequested, String seat) {
		this.setNumbOfSeats(numbOfSeatsRequested); 
		this.setSeats(seat);
	}

	/**
	 * Constructor of BookTickets class which is used to initialize an object with booking reference number, number of seats required, 
	 * seats booked, is booking confirmed boolean flag, and time of the system when the booking was initialized.
	 * @param bookingRefNumb Booking reference number.
	 * @param numbOfSeatsRequested Number of seat requested by the user.
	 * @param seatBooked Seats which are assigned to a particular object of this class.
	 * @param isBookingConfirmed Boolean variable to check if the booking has been confirmed.
	 * @param time Time of the system when the booking was initialized.
	 */
	public BookTickets(int bookingRefNumb, int numbOfSeatsRequested, String seatBooked, boolean isBookingConfirmed,
			LocalDateTime time) {
		// TODO Auto-generated constructor stub
		this.refNumber = bookingRefNumb;
		this.numbOfSeats = numbOfSeatsRequested;
		this.seatNames = seatBooked;
		this.bookingConfirmed = isBookingConfirmed;
		this.setTimeOfHold(time);
	}

	public int getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(int refNumber) {
		this.refNumber = refNumber;
	}

	public int getNumbOfSeats() {
		return numbOfSeats;
	}

	public void setNumbOfSeats(int numbOfSeats) {
		this.numbOfSeats = numbOfSeats;
	}

	public String getSeats() {
		return seatNames;
	}

	public void setSeats(String seats) {
		this.seatNames = seats;
	}

	public boolean isBookingConfirmed() {
		return bookingConfirmed;
	}

	public void setBookingConfirmed(boolean bookingConfirmed) {
		this.bookingConfirmed = bookingConfirmed;
	}

	public LocalDateTime getTimeOfHold() {
		return timeOfHold;
	}

	public void setTimeOfHold(LocalDateTime timeOfHold) {
		this.timeOfHold = timeOfHold;
	}

	@Override
	public String toString() {
		return "BookTickets [refNumber=" + refNumber + ", numbOfSeats=" + numbOfSeats + ", seatNames=" + seatNames
				+ ", bookingConfirmed=" + bookingConfirmed + ", timeOfHold=" + timeOfHold + "]";
	}
	
	

}
