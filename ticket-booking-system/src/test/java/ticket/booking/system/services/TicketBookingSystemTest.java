package ticket.booking.system.services;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import ticket.booking.system.models.BookTickets;
import ticket.booking.system.models.Screen;
import ticket.booking.system.models.SeatAvailabilityPerRow;

public class TicketBookingSystemTest {
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
		
		int numberOfSeatsRequested = 3;
		BookTickets actualBookTicketsObj = new BookTickets(numberOfSeatsRequested,"");
			
		// setting the expiration time as 30 seconds
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
		
		
		//Test Case 02: Trying to book 4 tickets
		numberOfSeatsRequested = 4;
		actualBookTicketsObj = new BookTickets(numberOfSeatsRequested,"");
					
		// setting the expiration time as 7 seconds
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
				
		//Test Case 03: Trying to book 9 tickets
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
		
		//Test Case 04: Trying to book 4 tickets but not confirming the booking
		numberOfSeatsRequested = 4;
		actualBookTicketsObj = new BookTickets(numberOfSeatsRequested,"");
					
		// setting the expiration time as 10 seconds
		ticketBookingSystemObj.setExpirationTime(2);
		referenceId = ticketBookingSystemObj.findAndHoldSeatsForUser(numberOfSeatsRequested,actualBookTicketsObj,numberOfSeatsRequested);
		//ticketBookingSystemObj.bookAndConfirmSeats(referenceId);
		Thread.sleep(10000);
		actualBookingTicketHashMap = ticketBookingSystemObj.getHashMapBookedTickets();
					
		boolean testCase4 = compareBookedTicketsMap(actualBookingTicketHashMap, expectedBookingTicketHashMap);
				
		if(!testCase4) {
			throw new Exception("Test Case 4 failed because Actual HashMap is : \n" + actualBookingTicketHashMap.toString() + ""
				+ "\n and Expected HashMap is : \n" + expectedBookingTicketHashMap.toString() + "\n Note : We are not comparing "
									+ "time of hold parameter.");
		}


	}
	
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
		//ticketBookingSystemObj.glbNumbOfSeatsRequested = numberOfSeatsRequested;
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
		
	private Screen getExpectedScreen(String testType){
		Screen expectedScreen = new Screen(5,5);
		String[][] seatingArrangement = {{}};
		//switch
			
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
