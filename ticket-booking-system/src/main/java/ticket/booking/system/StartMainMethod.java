package ticket.booking.system;

import ticket.booking.system.models.Screen;
import ticket.booking.system.services.TicketBookingSystem;

public class StartMainMethod {
	public static void main(String args[]) {
		
		Screen screenObj = new Screen();		
		screenObj.modifyHashMapAfterChangesMadeToScreen(screenObj.getSeatingArrangement());
		//start ticket booking system
		TicketBookingSystem ticketBookingObject = new TicketBookingSystem(screenObj);
		ticketBookingObject.setToBePrinted(true);
		ticketBookingObject.startUserProgram();
	}
	
}
