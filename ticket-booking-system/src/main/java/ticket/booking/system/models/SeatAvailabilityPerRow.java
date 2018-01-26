package ticket.booking.system.models;

/**
 * This class is use to maintain the hashmap "hashmapMaxConsecutiveSeatAvailablitity" of Screen Class.
 * @author Nitish Nalan
 * @param rowId Row ID of the seating arrangement.
 * @param totalSeatsVacant Total number of seats which are in this row Id.
 * @param maxConsecutiveSeats Maximum Consecutive Seats available in this row Id.   
 * 
 */
public class SeatAvailabilityPerRow {
	String rowID;
	public int totalSeatsVacant;
	public int maxConsecutiveSeats;

	public SeatAvailabilityPerRow() {
		// TODO Auto-generated constructor stub
	}

	public String getRowID() {
		return rowID;
	}

	public void setRowID(String rowID) {
		this.rowID = rowID;
	}

	public int getTotalSeatsVacant() {
		return totalSeatsVacant;
	}

	public void setTotalSeatsVacant(int totalSeatsVacant) {
		this.totalSeatsVacant = totalSeatsVacant;
	}

	public int getMaxConsecutiveSeats() {
		return maxConsecutiveSeats;
	}

	public void setMaxConsecutiveSeats(int maxConsecutiveSeats) {
		this.maxConsecutiveSeats = maxConsecutiveSeats;
	}

	
}
