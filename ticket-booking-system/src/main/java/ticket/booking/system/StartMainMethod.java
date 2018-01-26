package ticket.booking.system;

import ticket.booking.system.models.Screen;
import ticket.booking.system.services.TicketBookingSystem;

/**
 * This is the class which starts this program
 * @author Nitish Nalan
 *
 */
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
