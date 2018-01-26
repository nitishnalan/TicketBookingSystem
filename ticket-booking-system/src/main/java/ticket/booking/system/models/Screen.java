package ticket.booking.system.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class stores and computes all the information regarding the seating arrangement.
 * Other classes use its variables and methods to perform user actions and other operations
 *  by using hashmapTotalSeatsAvailableOnEachRow and hashmapMaxConsecutiveSeatAvailablitity.
 * and hashmapMaxConsecutiveSeatAvailablitity
 * @author Nitish Nalan
 * 
 *@param seatingArrangement It is a two dimensional array, providing a virtual view of the seating arrangement.
 *@param numberOfRows Total number of rows in the seating arrangement
 *@param numberOfColumns Total number of columns in the seating arrangement.
 *@param hashmapTotalSeatsAvailableOnEachRow It is a hashmap with Key being RowIds and Value being Total number of
 *available seat for respective RowIds
 *@param hashmapMaxConsecutiveSeatAvailablitity It is a hashmap with Key being rowIds in Integer and value being object of
 *SeatAvailabilityPerRow Class.
 */
public class Screen {

	String[][] seatingArrangement;
	int numberOfRows;
	public int numberOfColumns;
	Map<String, Integer>  hashmapTotalSeatsAvailableOnEachRow = new  HashMap<String, Integer>();
	Map<String, SeatAvailabilityPerRow> hashmapMaxConsecutiveSeatAvailablitity = new HashMap<String, SeatAvailabilityPerRow>();
		
	/**
	 * This Contructor is used to initialize seating arrangement for a particular Screen.
	 */
	public Screen() {
		initializeSeatingArrangement();
	}
	
	/**
	 * This constructor is also used to initialize Screen with 2 parameters
	 * @param rows : Number of rows for the seating arrangement
	 * @param columns : Number of columns for the seating arrangement
	 */
	public Screen(int rows, int columns){
		numberOfRows = rows +1;
		numberOfColumns = columns +1;
		seatingArrangement = new String[numberOfRows][numberOfColumns];
		initializeSeats(seatingArrangement, numberOfRows, numberOfColumns);
	}

/*	public Screen(String[][] seatingArrangement2, HashMap<String, Integer> hashSeatVacantRow2,
			HashMap<String, SeatAvailabilityPerRow> hashSeatAvailablitity2) {
		// TODO Auto-generated constructor stub
		this.seatingArrangement = seatingArrangement2;
		this.hashmapTotalSeatsAvailableOnEachRow = hashSeatVacantRow2;
		this.hashmapMaxConsecutiveSeatAvailablitity = hashSeatAvailablitity2;		
		
		initializeSeatingArrangement();
	}*/

	/**
	 * Calls 1st Round of Initialization of the Seating Arrangement.
	 * The count for the number of rows can not be greater than 26.
	 */
	
	private void initializeSeatingArrangement() {
					
		System.out.println("How many rows would you like to have in the Seating Arrangement?");
		Scanner userInputForScreen = new Scanner(System.in);
		
		int rowsActual = userInputForScreen.nextInt();
		int columnsActual = 0 ;
		
		if(rowsActual<=26) {
		
			System.out.println("How many columns would you like to have in the Seating Arrangement?");
			columnsActual = userInputForScreen.nextInt();
			//totalSeatsAvailable = rowsActual * columnsActual;
			numberOfRows = rowsActual +1;
			numberOfColumns = columnsActual +1;
			
			seatingArrangement = new String[numberOfRows][numberOfColumns];
			initializeSeats(seatingArrangement,numberOfRows,numberOfColumns);
			
			printSeatingAvailabilityStatus();
			
			//insertAvailablityOfSeats(seatingArrangement,rowIDs,columnIDs);
			
			//printSeatingAvailabilityStatus(seatingArrangement,rowIDs, columnIDs);
			
		}else {
			System.out.println("The number of rows can not be greater than 26!");
		}		
	}



	/**
	 * Calls 2nd round of initialization for the seating arrangement to assign default seat value of column and row values
	 * @param seatingArrangement two dimensional string Array
	 * @param rowID Number of rows to generate seating arrangement
	 * @param columnID Number of columns to generate seating arrangement
	 */
	
	public void initializeSeats(String[][] seatingArrangement, int rowID, int columnID) {
		
		//initial assignment of all the seats to "0" -- Which marks as available
		for(int i=0;i<rowID;i++) {			
			
			for(int j=0; j<columnID; j++) {
				if(j<9) {
					seatingArrangement[i][j] = Integer.toString(0);
				}
				else {
					seatingArrangement[i][j] = Integer.toString(0) + " ";
				}
				
			}			
		}
		
		seatingArrangement[0][0] = " ";
		char rowNumb;
		//Assigning Row and Column IDs to the Matrix
		for(int i=1; i<rowID; i++) {
			int j=0;
			rowNumb = (char) (64+i);
			seatingArrangement[i][j] = Character.toString(rowNumb);
			
			hashmapTotalSeatsAvailableOnEachRow.put(seatingArrangement[i][j],columnID-1);
			SeatAvailabilityPerRow sapr = new SeatAvailabilityPerRow();
			sapr.rowID = seatingArrangement[i][j];
			sapr.totalSeatsVacant = columnID-1;
			sapr.maxConsecutiveSeats = columnID-1;
			
			hashmapMaxConsecutiveSeatAvailablitity.put(seatingArrangement[i][j], sapr);
		}
		
		for(int j=1; j<columnID;j++) {
			int i = 0;
			seatingArrangement[i][j] = Integer.toString(j);
		}
	}
	
	
	/**
	 * Prints Seating Arrangement
	 */
	public void printSeatingAvailabilityStatus() {
		
		for(int i=0;i<this.numberOfRows;i++) {
			
			for(int j=0; j<numberOfColumns; j++) {
				System.out.print(this.seatingArrangement[i][j]+ " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Regularly updates the hashmapMaxConsecutiveSeatAvailablitity with total number of seats available in a single row
	   and maximum consecutive seats available in a single row of the seating arrangement.
	 * @param rowID Particular rowID of the seating arrangement.
	 * @param totalSeats Total seats available in the above mentioned rowID.
	 */

	public void populateBothHashMapForScreen(String rowID, int totalSeats) {
		int tempMaxVal = Integer.MIN_VALUE;
		SeatAvailabilityPerRow sapr = new SeatAvailabilityPerRow();
		
		sapr = hashmapMaxConsecutiveSeatAvailablitity.get(rowID);
		
		int tempRowId = (int) (rowID.charAt(0)) - 64;
		int consecutiveSeatCounter = 0;
		
		for(int j=1; j<numberOfColumns;j++) {
			
			if(seatingArrangement[tempRowId][j].equals("0")) {
				++consecutiveSeatCounter;
				if(consecutiveSeatCounter > tempMaxVal)
				tempMaxVal = consecutiveSeatCounter;
			}else
			{				
				consecutiveSeatCounter = 0;
			}
		}
		
		if(totalSeats!=0) {
			sapr.totalSeatsVacant = totalSeats;
			sapr.maxConsecutiveSeats = tempMaxVal;
		}else
		{
			sapr.totalSeatsVacant = totalSeats;
			sapr.maxConsecutiveSeats = 0;
		}
		
		
		hashmapMaxConsecutiveSeatAvailablitity.put(rowID, sapr);
	}

	
	/**
	 * Each time a booking is done or cancelled this method is called to update the hashmapTotalSeatsAvailableOnEachRow 
	 * and hashmapMaxConsecutiveSeatAvailablitity.
	 * @param seatingArrangement Current seating arrangement for a screen.	 */
	
	public void modifyHashMapAfterChangesMadeToScreen(String[][] seatingArrangement) {
		
		this.seatingArrangement = seatingArrangement;
		Character tempChar;
		String seatforHash; 
		for(int i = 1; i<seatingArrangement.length;i++) {
			for(int j=1; j<seatingArrangement[0].length;j++) {
				if(seatingArrangement[i][j].equals("1")) {
					tempChar = (char) (i+64);
					seatforHash = Character.toString(tempChar);
					hashmapTotalSeatsAvailableOnEachRow.put(seatforHash, hashmapTotalSeatsAvailableOnEachRow.get(seatforHash)-1);
					populateBothHashMapForScreen(seatforHash,hashmapTotalSeatsAvailableOnEachRow.get(seatforHash));
				}
			}
		}

	}
	
	public String[][] getSeatingArrangement() {
		return seatingArrangement;
	}

	public void setSeatingArrangement(String[][] seatingArrangement) {
		this.seatingArrangement = seatingArrangement;
	}

	public int getRowIDs() {
		return numberOfRows;
	}

	public void setRowIDs(int rowIDs) {
		this.numberOfRows = rowIDs;
	}

	public int getColumnIDs() {
		return numberOfColumns;
	}

	public void setColumnIDs(int columnIDs) {
		this.numberOfColumns = columnIDs;
	}

	public Map<String, Integer> getHashmapTotalSeatsAvailableOnEachRow() {
		return hashmapTotalSeatsAvailableOnEachRow;
	}

	public void setHashmapTotalSeatsAvailableOnEachRow(Map<String, Integer> hashmapTotalSeatsAvailableOnEachRow) {
		this.hashmapTotalSeatsAvailableOnEachRow = hashmapTotalSeatsAvailableOnEachRow;
	}

	public Map<String, SeatAvailabilityPerRow> getHashmapMaxConsecutiveSeatAvailablitity() {
		return hashmapMaxConsecutiveSeatAvailablitity;
	}

/*	public void setHashSeatAvailablitity(HashMap<String, SeatAvailabilityPerRow> hashmapMaxConsecutiveSeatAvailablitity) {
		this.hashmapMaxConsecutiveSeatAvailablitity = hashmapMaxConsecutiveSeatAvailablitity;
	}*/
	
	public void sethashmapMaxConsecutiveSeatAvailablitity(Map<String, SeatAvailabilityPerRow> hashSeatAvailablitity) {
		this.hashmapMaxConsecutiveSeatAvailablitity = hashSeatAvailablitity;
	}
	
	/**
	 * Used by the test cases to compare two object of the Screen Class.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Screen ) {
						
			String[][] screenToBeCompared = this.getSeatingArrangement();
			String[][] objToBeCompared = ((Screen) obj).getSeatingArrangement();
			boolean screenCompared = false, hashMapCompared =false;
			Map<String, SeatAvailabilityPerRow> screenHashmapToBeCompared = this.getHashmapMaxConsecutiveSeatAvailablitity();
			Map<String, SeatAvailabilityPerRow> objHashMapToBeCompared = ((Screen) obj).getHashmapMaxConsecutiveSeatAvailablitity();
			
	
			if(this.getSeatingArrangement().length == ((Screen) obj).getSeatingArrangement().length){
				if(screenToBeCompared[0].length == objToBeCompared[0].length) {
					for(int i=0; i<this.getSeatingArrangement().length;i++) {
						for(int j=0; j<this.getSeatingArrangement().length;j++) {
							if(!screenToBeCompared[i][j].equals(objToBeCompared[i][j])) {
								return false;
							}
						}
					}
					
					screenCompared = true;
				}else {
					return false;
				}
			}else {
				return false;
			}
			
			
			for(String row : screenHashmapToBeCompared.keySet()) {
				SeatAvailabilityPerRow obj1 = screenHashmapToBeCompared.get(row);
				SeatAvailabilityPerRow obj2 = objHashMapToBeCompared.get(row);
				
				if(obj1.totalSeatsVacant != obj2.totalSeatsVacant || obj1.maxConsecutiveSeats!=obj2.maxConsecutiveSeats) {
					return false;
				}
			}
			
			for(String row : objHashMapToBeCompared.keySet()) {
				if(!screenHashmapToBeCompared.containsKey(row)) {
					return false;
				}
			}
			
			hashMapCompared = true;
			
			if(screenCompared && hashMapCompared) {
				return true;
			}
		
		} else {
			return false;
		}
		
		return false;
				
	} 
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("");
		//builder.append("");
		StringBuilder seatsAvailable = new StringBuilder("");
		
		for(int i=0;i<this.numberOfRows;i++) {			
			//System.out.print(" ");
			for(int j=0; j<numberOfColumns; j++) {
				//System.out.print(this.seatingArrangement[i][j]+ " ");				
				builder.append(this.seatingArrangement[i][j]+ " ");
			}
			//System.out.println();			
			builder.append("\n");
		}
		
		
		// string builder hashmap
		seatsAvailable.append("HashMapSeatAvailability is : \n");
		int totalSeats, max_consecutive_seats;
		SeatAvailabilityPerRow sapr = new SeatAvailabilityPerRow();
		for (Map.Entry<String, SeatAvailabilityPerRow> entry : hashmapMaxConsecutiveSeatAvailablitity.entrySet()) {
			
			sapr = entry.getValue();
			totalSeats = sapr.getTotalSeatsVacant();
			max_consecutive_seats = sapr.getMaxConsecutiveSeats();
			
			seatsAvailable.append("Key : " + entry.getKey() + " -- totalSeats : " + totalSeats + " -- max_consecutive_seats : " + max_consecutive_seats + "\n");
		}
		
		builder.append("\n" + seatsAvailable);
		
		return builder.toString();
	}



}
