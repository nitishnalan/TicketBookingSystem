package ticket.booking.system.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import ticket.booking.system.models.BookTickets;
import ticket.booking.system.models.Screen;
import ticket.booking.system.models.SeatAvailabilityPerRow;

/**
 * TicketBookingSystem is the class which manages all the user action inputs, and perform the actions like holding
 * the seat on behalf of the user, show the availability of the seats, confirming the booking of the seats and
 * if the booking is not confirmed then by default the expiration time is set to 60 seconds. After 60 seconds a thread
 * would run and purge the bookings which are not yet confirmed.
 * @author Nitish Nalan
 *@param expirationTime  It is a duration in seconds after which, if the booking is not confirmed by the user
	the seats held shall become available to other users.
 *@param screenForTicketBooking Object instance of Screen class.
 *@param hashMapBookedTickets It is a hashmap which stores booking reference number a Key, and BookTickets as value 
 *with respect to booking reference number. It is used by this class to store all the seats which are requested by
 *the users to be used.
 *@param tempBookingRefNumb Booking reference number generated for the users.
 *@param toBePrinted Boolean flag used to handle if the sysout statements needs to be printed or not.
 *@param seatingArrangement Seating Arrangement pointing to instance seating arrangement of Screen class. 
 *@param hashmapTotalSeatsAvailableOnEachRow hashmapTotalSeatsAvailableOnEachRow pointing to instance seating arrangement of Screen class.
 *@param hashmapMaxConsecutiveSeatAvailablitity hashmapMaxConsecutiveSeatAvailablitity pointing to instance seating arrangement of Screen class.
 *@param numberOfRows numberOfRows pointing to instance seating arrangement of Screen class.
 *@param numberOfColumns numberOfColumns pointing to instance seating arrangement of Screen class.
 */
public class TicketBookingSystem implements Runnable{

	
	private int expirationTime = 60;
	
	public Screen screenForTicketBooking;
	public  String[][] seatingArrangement;
	public  Map<String, Integer>  hashmapTotalSeatsAvailableOnEachRow = new  HashMap<String, Integer>();
	public  Map<String, SeatAvailabilityPerRow> hashmapMaxConsecutiveSeatAvailablitity = new HashMap<String, SeatAvailabilityPerRow>();
	public int rowIDs,columnIDs;	
	public  Map<Integer, BookTickets> hashMapBookedTickets = new ConcurrentHashMap<Integer, BookTickets>();
	int tempBookingRefNumb = 0;
	public boolean toBePrinted = false;
	
	/**
	 * This constructor initializes all the variables which are referencing to instance of Screen class object.
	 * @param screenObj
	 */

	public TicketBookingSystem(Screen screenObj) {
		seatingArrangement = screenObj.getSeatingArrangement();
		hashmapTotalSeatsAvailableOnEachRow = screenObj.getHashmapTotalSeatsAvailableOnEachRow();
		hashmapMaxConsecutiveSeatAvailablitity = screenObj.getHashmapMaxConsecutiveSeatAvailablitity();
		screenForTicketBooking = screenObj;
		rowIDs = screenObj.getRowIDs();
		columnIDs = screenObj.getColumnIDs();
		
	}

	/**
	 * This method is responsible for handling input from the user. It supports all the user functionalities
	 * o Press '1' to check the availability of the seats.
	   o Press '2' to hold the tickets for this screen.
	   o Press '3' to confirm the seat booking
	   o Please type 'HELP' anytime if you wish to review these actions
	   o Please type 'QUIT' to Exit the Program
	 */
	public void startUserProgram() {
		if(toBePrinted) {
			System.out.println("Starting User program!");
			printHelp();
		}				
				
		Scanner userScan = new Scanner(System.in);		
		String userInput = userScan.next().toUpperCase().trim();
				
		while(!userInput.equals("QUIT")) {
			int seatsAvailable = countNumberOfSeats();
			if(userInput.equals("HELP")) {
				printHelp();
			}
			else if(userInput.equals("1")) {				
				System.out.println("Number of Seats Available are : " + seatsAvailable);
				screenForTicketBooking.printSeatingAvailabilityStatus();				
			}
			else if(userInput.equals("2")) {
				System.out.println("Please enter Number of Seats you would like to hold :");
				int numbOfSeats = userScan.nextInt();
				if(numbOfSeats > seatsAvailable) {
					System.out.println("Total Number of Seats available for this screen is : "
					+ seatsAvailable + " \nPlease enter "
					+ "the number of tickets to be booked less than Total Number of tickets.");
					continue;
				}else {
					BookTickets bookTicketObj = new BookTickets(numbOfSeats,"");
					findAndHoldSeatsForUser(numbOfSeats,bookTicketObj,numbOfSeats);
				}
			}
			else if(userInput.equals("3")) {
				System.out.println("Please enter the Booking Reference Number to confirm the booking");
				userInput = userScan.next();				
				int userInputInt = Integer.parseInt(userInput);
				bookAndConfirmSeats(userInputInt);				
			}
			else if(userInput.equals("QUIT")) {
				System.out.println("Exiting from Ticket Booking System.");
				break;
			}else if(!userInput.equals("1") || !userInput.equals("2") || !userInput.equals("3") || !userInput.equals("QUIT") || !userInput.equals("HELP")) {
				System.out.println("The input is incorrect. Please type 'HELP' to get more details on the input allowed.");
			}			
			userInput = userScan.next();
			userInput = userInput.toUpperCase().trim();
		}
		
		System.out.println("Exiting from Ticket Booking System.");
	}
	
	/**
	 * Prints User Actions available for the user
	 */
	private void printHelp() {
		if(toBePrinted) {
			System.out.println("Please read below the actions a User can perform.");
			
			System.out.println("Press '1' to check the Availability of the Seats.\n"
					+ "Press '2' to hold the tickets for this screen.\n"
					+"Press '3' to confirm the seat booking\n"
					+ "Please type 'HELP' anytime if you wish to review this actions\n"
					+ "Please type 'QUIT' to Exit the Program\n");			
		}
			
	}
	
	/**
	 * Counts the number of seats for available for a particular seating arrangement.
	 * @return Integer count of seats which are available in the seating arrangement.
	 */
	public int countNumberOfSeats() {
		int seatCounter = 0;		
		for(int i=1;i<rowIDs;i++) {
			
			for(int j=1; j<columnIDs;j++) {
				
				if(seatingArrangement[i][j].equals("0")) {
					++seatCounter;
				}
			}
		}
		return seatCounter;
	}

	/**
	 * This method takes input of the ticket booking reference number and confirms the booking on behalf of the user.
	 * @param userInputInt Booking reference number for which seat holding needs to be confirmed.
	 */
	public void bookAndConfirmSeats(int userInputInt) {
        LocalDateTime tempTime;
        BookTickets tempBookTicketObj;
        
        	if (hashMapBookedTickets.containsKey(userInputInt)) {
            	
            	tempBookTicketObj = hashMapBookedTickets.get(userInputInt);
            	if(!tempBookTicketObj.isBookingConfirmed()) {
        			tempTime = tempBookTicketObj.getTimeOfHold();
        			LocalDateTime currentTime = LocalDateTime.now(); 
        			
        			Duration dur = Duration.between(currentTime, tempTime);
        			
        			long diffTime = Math.abs(dur.getSeconds());
        			
        			if(diffTime > expirationTime && !tempBookTicketObj.isBookingConfirmed()) {			
        				
        				hashMapBookedTickets.remove(userInputInt);        				
        				if(toBePrinted) {
        					System.out.println("Session Hold is completed for the the booking reference number "
            						+ ": " + tempBookTicketObj.getRefNumber() +  ". Please try booking the tickets again!");
        				}
        				
        			}else {        				
           				 tempBookTicketObj.setBookingConfirmed(true);
        				 hashMapBookedTickets.put(userInputInt, tempBookTicketObj);        				 
        				 if(toBePrinted) {
        					 System.out.println("Booking has been confirmed for the booking reference number : " + tempBookTicketObj.getRefNumber());
        				 }
        				 
        				}
            	}
            	else {            		
   				 if(toBePrinted) {
   					System.out.println("This Booking ID is already confirmed!");
   				 }
            		
            	}
			}
        	else {        		
				 if(toBePrinted) {
					 System.out.println("This Booking ID does not exist!");
				 }
        		
        	}
			        
	}

	 public Map<Integer, BookTickets> getHashMapBookedTickets() {
		return hashMapBookedTickets;
	}

	public void setHashMapBookedTickets(Map<Integer, BookTickets> hashMapBookedTickets) {
		this.hashMapBookedTickets = hashMapBookedTickets;
	}
	
	public boolean isToBePrinted() {
		return toBePrinted;
	}

	public void setToBePrinted(boolean toBePrinted) {
		this.toBePrinted = toBePrinted;
	}

	/**
	  * This method finds and holds the best seats for the user.
	  * 
	 * @param numbOfSeatsRequested Number of seats requested by the user.
	 * @param tempBookTicketObj Object of BookTickets class.
	 * @param totalNumbOfSeatsReq Total number of seats requested by the user.
	 * @return -1 if the seat is not booked else returns booking reference number
	 */
	public Integer findAndHoldSeatsForUser(int numbOfSeatsRequested, BookTickets tempBookTicketObj,int totalNumbOfSeatsReq) {
		
		String seatId;
		int rowId;
		int totalSeats, max_consecutive_seats,noOfSeats;
		int seatCheckCounter = 0;
		String seatBooked = "";
		SeatAvailabilityPerRow sapr = new SeatAvailabilityPerRow();
		boolean maxSeatsFound = false;
		BookTickets bookingTicket = tempBookTicketObj;
		boolean isSeatReserved = false;
		
		for (Map.Entry<String, SeatAvailabilityPerRow> entry : screenForTicketBooking.getHashmapMaxConsecutiveSeatAvailablitity().entrySet()) {
						
			sapr = entry.getValue();
			totalSeats = sapr.getTotalSeatsVacant();
			max_consecutive_seats = sapr.getMaxConsecutiveSeats();
			
			//1st case : to find consecutive seats first
			if(max_consecutive_seats >= numbOfSeatsRequested) {
				seatId = entry.getKey();
				rowId = (int) seatId.charAt(0) - 64;
				
				for(int j=1; j<screenForTicketBooking.numberOfColumns; j++) {
					if(screenForTicketBooking.getSeatingArrangement()[rowId][j].equals("0") && seatCheckCounter!=numbOfSeatsRequested) {
						if(seatCheckCounter < numbOfSeatsRequested) {
							++seatCheckCounter;
							
							if(seatBooked!="") {
								seatBooked = seatBooked + "," + seatId + Integer.toString(j);
							}else
							{
								seatBooked = seatId + Integer.toString(j);
							}							
						}else {
							break;
						}
						
					}else
					{
						if(seatCheckCounter!=numbOfSeatsRequested) {
							seatCheckCounter =0;
							seatBooked = "";
						}						
					}
				}
				 
				if(bookingTicket.getSeats()!="") {
					seatBooked = bookingTicket.getSeats() + "," + seatBooked;
				}

				bookingTicket.setSeats(seatBooked);
				
				holdTheSeats(bookingTicket);	
				totalNumbOfSeatsReq = totalNumbOfSeatsReq - numbOfSeatsRequested;
				numbOfSeatsRequested = totalNumbOfSeatsReq;
				if(totalNumbOfSeatsReq==0) {
					++tempBookingRefNumb;
					noOfSeats = bookingTicket.getNumbOfSeats();
					bookingTicket.setRefNumber(tempBookingRefNumb);
					bookingTicket = new BookTickets(tempBookingRefNumb,noOfSeats,seatBooked,false,LocalDateTime.now());
					isSeatReserved = true;
				
   				 	if(toBePrinted) {
   					 	System.out.println("Booking Reference Number : " + bookingTicket.getRefNumber());
   						System.out.println("Seats Booked are : " + bookingTicket.getSeats());
   				 	}
					
					hashMapBookedTickets.put(bookingTicket.getRefNumber(), bookingTicket);
					
					//This method initialized Thread which would run after 60 seconds to check whether booking was confirmed or not
					initiateThreadToRunTheScheduleJob();
					
					maxSeatsFound = true;
				}else {
				
				}
				
				break;
			}else {
				
			}
			
		}
		
		if(!maxSeatsFound) {
			//This code runs when there is no maximum consecutive seats found

			whenMaxSeatNotFoundHoldTheNextBestSeats(numbOfSeatsRequested,bookingTicket,totalNumbOfSeatsReq);
			isSeatReserved = true;						
		}else {
	
		}
		return isSeatReserved ? bookingTicket.getRefNumber() : -1;
	}

	/**
	 * This method is used to initialize thread, which would run and trigger the method responsible for removing seats which are not confirmed.
	 */
	private void initiateThreadToRunTheScheduleJob() {
	// TODO Auto-generated method stub
		
		Thread t1 = new Thread((this));
		
		t1.start();
	}
	
	/**
	 * This method will be used when the seats to be booked are more than maximum consecutive seats available in any rows of the seating arrangement.	
	 * @param numbOfSeatsRequested Number of seats requested to be booked.
	 * @param bookingTicketObj Object of BookingTickets class which will hold partial seats if they are booked.
	 * @param totalNumbOfSeatsReq Total number of seats to be booked.
	 */

	private void whenMaxSeatNotFoundHoldTheNextBestSeats(int numbOfSeatsRequested, BookTickets bookingTicketObj, int totalNumbOfSeatsReq) {
		int tempSeatRequested = 0;
		while(totalNumbOfSeatsReq != 0) {
			if(numbOfSeatsRequested!=1) {
				tempSeatRequested = numbOfSeatsRequested -1;
				findAndHoldSeatsForUser(tempSeatRequested, bookingTicketObj,totalNumbOfSeatsReq);
				break;
				
			}else
			{
				findAndHoldSeatsForUser(numbOfSeatsRequested, bookingTicketObj,totalNumbOfSeatsReq);
				break;
			}
		}
		
	}

	/**
	 * This method holds the seat in the seating arrangement, so that the same seat can not	be assigned to more than one customer.
	 * @param bookingTicket Object of BookTickets class which have the seats which needs to be held on behalf of the user.
	 */
	
	private void holdTheSeats(BookTickets bookingTicket) {
		
		String[] seatsToBeBooked = bookingTicket.getSeats().split(",");
		String seat = "";
		seatingArrangement = screenForTicketBooking.getSeatingArrangement();
		hashmapTotalSeatsAvailableOnEachRow = screenForTicketBooking.getHashmapTotalSeatsAvailableOnEachRow();
		
		for(int eachSeat=0; eachSeat<seatsToBeBooked.length; eachSeat++) {
			seat = seatsToBeBooked[eachSeat];			
			
			int rowArrInd = ((int)seat.charAt(0)-64);
			String seatInd = Character.toString(seat.charAt(1));
			int colArrInd = Integer.parseInt(seatInd);
			
			if(!seatingArrangement[rowArrInd][colArrInd].equals("1")) {
				seatingArrangement[rowArrInd][colArrInd] = "1";
				hashmapTotalSeatsAvailableOnEachRow.put(Character.toString(seat.charAt(0)), hashmapTotalSeatsAvailableOnEachRow.get(Character.toString(seat.charAt(0)))-1);
			}			
					
		}
		
		screenForTicketBooking.setHashmapTotalSeatsAvailableOnEachRow(hashmapTotalSeatsAvailableOnEachRow);
		screenForTicketBooking.populateBothHashMapForScreen(Character.toString(seat.charAt(0)), hashmapTotalSeatsAvailableOnEachRow.get(Character.toString(seat.charAt(0))));
		screenForTicketBooking.setSeatingArrangement(seatingArrangement);
	}
		
	
	/**
	 * If the user does not confirm the booking the this method reverts the availability of the booked seats.
	 * @param bookingTicket object of BookTickets class which has all the details of the booking to be purged.
	 */
	private void removeTheSeatsHeld(BookTickets bookingTicket) {

		String[] seatsToBeBooked = bookingTicket.getSeats().split(",");
		String seat="";
		seatingArrangement = screenForTicketBooking.getSeatingArrangement();
		hashmapTotalSeatsAvailableOnEachRow = screenForTicketBooking.getHashmapTotalSeatsAvailableOnEachRow();
		for(int eachSeat=0; eachSeat<seatsToBeBooked.length; eachSeat++) {
			seat = seatsToBeBooked[eachSeat];
			int rowArrInd = ((int)seat.charAt(0)-64);
			String seatInd = Character.toString(seat.charAt(1));
			int colArrInd = Integer.parseInt(seatInd);		
			
			seatingArrangement[rowArrInd][colArrInd] = "0";
			
			hashmapTotalSeatsAvailableOnEachRow.put(Character.toString(seat.charAt(0)), hashmapTotalSeatsAvailableOnEachRow.get(Character.toString(seat.charAt(0)))+1);
			screenForTicketBooking.setHashmapTotalSeatsAvailableOnEachRow(hashmapTotalSeatsAvailableOnEachRow);
			screenForTicketBooking.populateBothHashMapForScreen(Character.toString(seat.charAt(0)), hashmapTotalSeatsAvailableOnEachRow.get(Character.toString(seat.charAt(0))));
		}
		
		hashmapMaxConsecutiveSeatAvailablitity = screenForTicketBooking.getHashmapMaxConsecutiveSeatAvailablitity();
		screenForTicketBooking.setSeatingArrangement(seatingArrangement);
	}

	/**
	 * This method is triggered by the thread which iterates through the hashMapBookedTickets and identifies the tickets to be purged.
	 * 
	 */
	public void runJobToPurgeBookTicketsWhichAreNotConfirmedYet() {

        LocalDateTime tempTime;
        BookTickets tempBookTicketObj;
       
        if(!hashMapBookedTickets.isEmpty()) {
            for(int eachEntry: hashMapBookedTickets.keySet()){
            	
            	tempBookTicketObj = hashMapBookedTickets.get(eachEntry);
    			tempTime = tempBookTicketObj.getTimeOfHold();
    			    			
    			LocalDateTime currentTime = LocalDateTime.now(); 
    			
    			Duration durationCalculation = Duration.between(currentTime, tempTime);
    			
    			long differenceTime = Math.abs(durationCalculation.getSeconds());
    			
    			if(differenceTime > expirationTime && !tempBookTicketObj.isBookingConfirmed()) {
    				hashMapBookedTickets.remove(eachEntry);
    				removeTheSeatsHeld(tempBookTicketObj);
    			}
            }        	
        }
        else {
        
        }
        
    }
	
    
   	public void run() {
   		//LocalDateTime currentTime = LocalDateTime.now();
		
   		try {
   			Thread.sleep(expirationTime * 1000);
   			runJobToPurgeBookTicketsWhichAreNotConfirmedYet();
   		} catch (InterruptedException e) {   			
   			e.printStackTrace();
   		}
   		
   	}

	public int getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(int expirationTime) {
		this.expirationTime = expirationTime;
	}

 }
