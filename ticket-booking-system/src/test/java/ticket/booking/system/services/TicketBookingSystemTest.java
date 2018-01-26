package ticket.booking.system.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import ticket.booking.system.models.BookTickets;
import ticket.booking.system.models.Screen;
import ticket.booking.system.models.SeatAvailabilityPerRow;

/**
 * This class covers all the scenarios of testing of this project
 * @author Nitish Nalan
 *
 */

public class TicketBookingSystemTest {
	
	/**
	 * This class will test 4 scenarios for TicketBookingSystem class
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testConfirmationOfSeats() throws Exception {
		Screen testScreen = new Screen(5,5);
		TicketBookingSystem ticketBookingSystemObj = new TicketBookingSystem(testScreen);
		Map<Integer, BookTickets> actualBookingTicketHashMap = new ConcurrentHashMap<Integer, BookTickets>();
		//ticketBookingSystemObj.toBePrinted = true;
		Map<Integer, BookTickets> expectedBookingTicketHashMap = new ConcurrentHashMap<Integer, BookTickets>();
		
		ticketBookingSystemObj.screenForTicketBooking =  testScreen;
		Map<String, SeatAvailabilityPerRow> testHashMap = new HashMap<String, SeatAvailabilityPerRow>();
		testHashMap = testScreen.getHashmapMaxConsecutiveSeatAvailablitity();
		
		//Test Case 01: Trying to hold 3 tickets - A1,A2,A3 seats shall be assigned. We also confirm the booking.
		int numberOfSeatsRequested = 3;
		BookTickets actualBookTicketsObj = new BookTickets(numberOfSeatsRequested,"");
			
		// setting the expiration time as 2 seconds
		ticketBookingSystemObj.setExpirationTime(2);
		int referenceId = ticketBookingSystemObj.findAndHoldSeatsForUser(numberOfSeatsRequested,actualBookTicketsObj,numberOfSeatsRequested);
		ticketBookingSystemObj.bookAndConfirmSeats(referenceId);
		actualBookingTicketHashMap = ticketBookingSystemObj.getHashMapBookedTickets();
		Thread.sleep(3000);
		BookTickets expectedBookTicketsObj = new BookTickets(referenceId, numberOfSeatsRequested, "A1,A2,A3", true, LocalDateTime.now());
		expectedBookingTicketHashMap.put(referenceId, expectedBookTicketsObj);
		
		boolean testCase1 = compareBookedTicketsMap(actualBookingTicketHashMap, expectedBookingTicketHashMap);
		
		if(!testCase1) {
			throw new Exception("Test Case 1 failed because Actual HashMap is : \n" + actualBookingTicketHashMap.toString() + ""
					+ "\n and Expected HashMap is : \n" + expectedBookingTicketHashMap.toString() + "\n Note : We are not comparing "
							+ "time of hold parameter.");
		}
		
		
		//Test Case 02: Trying to book 4 tickets - B1,B2,B3,B4 seats shall be assigned. We also confirm the booking.
		numberOfSeatsRequested = 4;
		actualBookTicketsObj = new BookTickets(numberOfSeatsRequested,"");
					
		// setting the expiration time as 2 seconds
		ticketBookingSystemObj.setExpirationTime(2);
		referenceId = ticketBookingSystemObj.findAndHoldSeatsForUser(numberOfSeatsRequested,actualBookTicketsObj,numberOfSeatsRequested);
		ticketBookingSystemObj.bookAndConfirmSeats(referenceId);
		actualBookingTicketHashMap = ticketBookingSystemObj.getHashMapBookedTickets();
			
		Thread.sleep(3000);
		expectedBookTicketsObj = new BookTickets(referenceId, numberOfSeatsRequested, "B1,B2,B3,B4", true, LocalDateTime.now());
		expectedBookingTicketHashMap.put(referenceId, expectedBookTicketsObj);
		
		boolean testCase2 = compareBookedTicketsMap(actualBookingTicketHashMap, expectedBookingTicketHashMap);
				
		if(!testCase2) {
			throw new Exception("Test Case 2 failed because Actual HashMap is : \n" + actualBookingTicketHashMap.toString() + ""
				+ "\n and Expected HashMap is : \n" + expectedBookingTicketHashMap.toString() + "\n Note : We are not comparing "
									+ "time of hold parameter.");
		}
				
		//Test Case 03: Trying to book 9 tickets - C1,C2,C3,C4,C5,D1,D2,D3,A4 seats shall be assigned. We also confirm the booking.
		numberOfSeatsRequested = 9;
		actualBookTicketsObj = new BookTickets(numberOfSeatsRequested,"");
					
		// setting the expiration time as 2 seconds
		ticketBookingSystemObj.setExpirationTime(2);
		referenceId = ticketBookingSystemObj.findAndHoldSeatsForUser(numberOfSeatsRequested,actualBookTicketsObj,numberOfSeatsRequested);
		ticketBookingSystemObj.bookAndConfirmSeats(referenceId);
		actualBookingTicketHashMap = ticketBookingSystemObj.getHashMapBookedTickets();
			
		Thread.sleep(3000);
		expectedBookTicketsObj = new BookTickets(referenceId, numberOfSeatsRequested, "C1,C2,C3,C4,C5,D1,D2,D3,A4", true, LocalDateTime.now());
		expectedBookingTicketHashMap.put(referenceId, expectedBookTicketsObj);
		
		boolean testCase3 = compareBookedTicketsMap(actualBookingTicketHashMap, expectedBookingTicketHashMap);
				
		if(!testCase3) {
			throw new Exception("Test Case 3 failed because Actual HashMap is : \n" + actualBookingTicketHashMap.toString() + ""
				+ "\n and Expected HashMap is : \n" + expectedBookingTicketHashMap.toString() + "\n Note : We are not comparing "
									+ "time of hold parameter.");
		}
		
		//Test Case 04: Trying to book 4 tickets but not confirming the booking. Hence the booktickets object will not be found in the hashmap.
		numberOfSeatsRequested = 4;
		actualBookTicketsObj = new BookTickets(numberOfSeatsRequested,"");
					
		// setting the expiration time as 10 seconds
		ticketBookingSystemObj.setExpirationTime(2);
		referenceId = ticketBookingSystemObj.findAndHoldSeatsForUser(numberOfSeatsRequested,actualBookTicketsObj,numberOfSeatsRequested);
		//ticketBookingSystemObj.bookAndConfirmSeats(referenceId);
		Thread.sleep(7000);
		actualBookingTicketHashMap = ticketBookingSystemObj.getHashMapBookedTickets();
					
		boolean testCase4 = compareBookedTicketsMap(actualBookingTicketHashMap, expectedBookingTicketHashMap);
				
		if(!testCase4) {
			throw new Exception("Test Case 4 failed because Actual HashMap is : \n" + actualBookingTicketHashMap.toString() + ""
				+ "\n and Expected HashMap is : \n" + expectedBookingTicketHashMap.toString() + "\n Note : We are not comparing "
									+ "time of hold parameter.");
		}


	}
	
	/**
	 * This class is responsible to test different scenarios for the seating arrangement of the screen object.
	 * @throws Exception
	 */
	@Test
	public void testBasicBookingOrder() throws Exception {
		
		// 1. initialize screen object
		Screen testScreen = new Screen(5,5);
		TicketBookingSystem ticketBookingSystemObj = new TicketBookingSystem(testScreen);
		
		ticketBookingSystemObj.screenForTicketBooking =  testScreen;
		Map<String, SeatAvailabilityPerRow> testHashMap = new HashMap<String, SeatAvailabilityPerRow>();
		testHashMap = testScreen.getHashmapMaxConsecutiveSeatAvailablitity();
		
		//Test Case 1: to check  whether the number of rows and columns is calculated  as expected
		if(!testScreen.equals(getExpectedScreen("1"))){
			
			throw (new Exception("Test case 1 failed due to expected screen "
					+ "is : \n"
					+ getExpectedScreen("1") + "\n the actual screen is : "
							+ "\n" + testScreen));
			
		}
		
		
		int numberOfSeatsRequested = 3;
		BookTickets b1 = new BookTickets(numberOfSeatsRequested,"");
		ticketBookingSystemObj.findAndHoldSeatsForUser(numberOfSeatsRequested,b1,numberOfSeatsRequested);
			
		//Test Case 2: to check after booking 3 seats
		
		if(!testScreen.equals(getExpectedScreen("2"))){
			throw (new Exception("Test case 2 failed due to expected screen is: "
					+ "\n" + getExpectedScreen("2") + "\n Actual is: \n " + testScreen));
		}
				
		//Test Case 3:
		//After booking 3 tickets trying to book 5 tickets
		
		numberOfSeatsRequested = 5;
		b1 = new BookTickets(numberOfSeatsRequested,"");
		ticketBookingSystemObj.findAndHoldSeatsForUser(numberOfSeatsRequested,b1,numberOfSeatsRequested);
		
		if(!testScreen.equals(getExpectedScreen("3"))){
			throw (new Exception("Test case 3 failed due to expected screen is: "
					+ "\n" + getExpectedScreen("3") + "\n Actual is: \n " + testScreen));
		}
		

		//Test Case 4:
		//After booking 3 tickets, then 5 tickets, trying to book 8 tickets

		numberOfSeatsRequested = 9;
		b1 = new BookTickets(numberOfSeatsRequested,"");
		ticketBookingSystemObj.findAndHoldSeatsForUser(numberOfSeatsRequested,b1,numberOfSeatsRequested);

		if(!testScreen.equals(getExpectedScreen("4"))){
			throw (new Exception("Test case 4 failed due to expected screen is: "
					+ "\n" + getExpectedScreen("4") + "\n Actual is: \n " + testScreen));
		}
		
		
		
	}
		
	/**
	 * This method generates various seating arrangement based on the testing scenario and is our expected scenarios for the
	 * seating arrangement of the screens.
	 * @param testType Type of testing scenario we would like to perform.
	 * @return Expected Screen Object
	 */
	private Screen getExpectedScreen(String testType){
		Screen expectedScreen = new Screen(5,5);
		String[][] seatingArrangement = {{}};
					
		switch(testType)
		{
			case "1": 
				seatingArrangement = new String[][] 
					{{" ","1","2","3","4","5"},
					{"A","0","0","0","0","0"},
					{"B","0","0","0","0","0"},
					{"C","0","0","0","0","0"},
					{"D","0","0","0","0","0"},
					{"E","0","0","0","0","0"}};
				
				break;
				
			case "2":
				seatingArrangement = new String[][] 
					{{" ","1","2","3","4","5"},
					{"A","1","1","1","0","0"},
					{"B","0","0","0","0","0"},
					{"C","0","0","0","0","0"},
					{"D","0","0","0","0","0"},
					{"E","0","0","0","0","0"}};
				break;
			
			case "3":
				seatingArrangement = new String[][] 
					{{" ","1","2","3","4","5"},
					{"A","1","1","1","0","0"},
					{"B","1","1","1","1","1"},
					{"C","0","0","0","0","0"},
					{"D","0","0","0","0","0"},
					{"E","0","0","0","0","0"}};
				break;
				
			case "4":
				seatingArrangement = new String[][] 
					{{" ","1","2","3","4","5"},
					{"A","1","1","1","1","0"},
					{"B","1","1","1","1","1"},
					{"C","1","1","1","1","1"},
					{"D","1","1","1","0","0"},
					{"E","0","0","0","0","0"}};
				break;
		}
		expectedScreen.setSeatingArrangement(seatingArrangement);
		expectedScreen.modifyHashMapAfterChangesMadeToScreen(seatingArrangement);
		 
		return expectedScreen;
	}
	

	/**
	 * This method compares ActualHashMapBookingTicket with the ExpectedHashMapBookingTicket.
	 * We do not compare time of hold for the scenarios here.
	 * We compare booking reference number, number of seats requested to be booked, seats which are assigned
	 * and if the booking status is same or not.
	 * @param actualBookingTicketHashMap
	 * @param expectedBookingTicketHashMap
	 * @return
	 */
	private boolean compareBookedTicketsMap(Map<Integer, BookTickets> actualBookingTicketHashMap,Map<Integer, BookTickets> expectedBookingTicketHashMap) {		
		boolean compareFlag = false;
		if(!actualBookingTicketHashMap.isEmpty()) {
			for (int eachEntry : actualBookingTicketHashMap.keySet()) {
				BookTickets actualBookTicketObject = actualBookingTicketHashMap.get(eachEntry);
				
				BookTickets expectedBookTicketObject = expectedBookingTicketHashMap.get(eachEntry);
				
				if(actualBookTicketObject.getRefNumber() == expectedBookTicketObject.getRefNumber() && actualBookTicketObject.getNumbOfSeats() == expectedBookTicketObject.getNumbOfSeats()) {
					if(actualBookTicketObject.getSeats().equals(expectedBookTicketObject.getSeats()) && actualBookTicketObject.isBookingConfirmed() == expectedBookTicketObject.isBookingConfirmed()){
						compareFlag = true;
					}else
					{
						compareFlag = false;
					}
				}
				else {
					compareFlag = false;
				}
			}
			
		}

         return compareFlag;  
	}
}
