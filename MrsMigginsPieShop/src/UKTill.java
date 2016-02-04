
/**
 * My first attempt at the UKTill 
 * Contains floats of various UK denominations
 * 
 * @author Mrs Miggins
 * @version February 2015
 */

public class UKTill {
	private DenominationFloat[] contents;

	/**
	 * Builds an empty till
	 */
	public UKTill() {
		contents = new DenominationFloat[UKDenomination.values().length];

		// Initialise the array with empty floats for each denomination. This is
		// kind of having empty trays for each denomination in the till!
		for (UKDenomination denom : UKDenomination.values()) {
			DenominationFloat denomFloat = new DenominationFloat(denom, 0);
			contents[denom.ordinal()] = denomFloat;
		}
	}

	/**
	 * Enables a user to add a denomination float to the Till. Will add a copy
	 * @param theFloat the denomination float to be added to the Till as a copy
	 */
	public void addFloat(DenominationFloat theFloat) {
		// Add to existing float
		DenominationFloat currentFloat = contents[theFloat.getType().ordinal()];
		currentFloat.setQuantity(currentFloat.getQuantity()
				+ theFloat.getQuantity());
	}

	/**
	 * Enables a user to clear a denomination float from the Till.
	 * @param denominationType Denomination float to be removed
	 */
	public void removeFloat(UKDenomination denominationType) {
		contents[denominationType.ordinal()].setQuantity(0);
	}

	/**
	 * Empties out the till and returns the contents
	 * @return An array containing the contents of the till, with
	 * each element dealing with a given UK denomination. If the denomination
	 * does not exist in the till then returns a DenominationFloat with zero
	 * quantity.
	 */
	public DenominationFloat[] emptyTill() {
		DenominationFloat[] tillContents = new DenominationFloat[contents.length];
		for (int i = 0; i < contents.length; i++) {
			DenominationFloat denom = new DenominationFloat(
					contents[i].getType(), contents[i].getQuantity());
			tillContents[i] = denom;
			contents[i].setQuantity(0);
		}
		return tillContents;
	}

	/**
	 * Calculates the change to be returned from the till and decrements the
	 * till for each denomination
	 * 
	 * @param changeDue
	 *            The amount in pence we wish to extract from the till. 
	 * @return An array of denominations of different values that are in sum equal
	 *         to the provided changeDue value in pence. If there isn't
	 *         enough change in the till then display an error message saying 
	 *         that Mrs Miggins owes the customer some money!
	 */
	public DenominationFloat[] getChange(int changeDue) { 	//change is positive
		DenominationFloat[] change = null; 					//Null added so that the class compiles
		int arraySize = 12;									
		change = new DenominationFloat[arraySize];			//Creates the array with the size of the amount of denominations
		int E = 0;											
		for (int i = 11; i > -1; i--) {						//Loops through all the denominations starting from the largest one.
			UKDenomination type = contents[i].getType();	//Gets the denomination from the array contents
			if (changeDue >= type.getValue()) {				//If the change the customer needs is more than the value of a denomination, give them at least of the that denomination, otherwise look at the next decrement of money
				int nQ = contents[i].getQuantity();		
				if (nQ != 0) {
					int num = changeDue / type.getValue();		//Work out the amount of the denomination to give
					nQ = (contents[i].getQuantity() - num);		//The value to set the quantity to is calculated
					if (nQ >= 0) {
						changeDue -= (type.getValue() * num);		//Reduce the change they need to be given by the amount given times its value
						contents[i].setQuantity(nQ);				//Sets the quantity to that value
						DenominationFloat X = new DenominationFloat(contents[i].getType(), num);
						change[E] = X;						//Adds the float to the change array					
						E++;								//Increments the position in the change array
					}
					else if (nQ < 0) {
						num = contents[i].getQuantity();
						changeDue -= (type.getValue() * num);		//Reduce the change they need to be given by the amount given times its value
						contents[i].setQuantity(0);					//Sets the quantity to 0
						DenominationFloat X = new DenominationFloat(contents[i].getType(), num);
						change[E] = X;								//Adds the float to the change array					
						E++;	
					}
				}				
			}
		}
		if (changeDue > 0) {
			System.out.println("Not enough change sorry! I owe you " + changeDue + "p");
		}
		return change;
	}	
	
	/**
	 * toString returns a formatted String representing denomination floats in the Till
	 * @return String a formatted representation of the Till contents
	 */
	public String toString() {
		// StringBuilder is a more efficient way of building Strings. The
		// problem with the String class is that String objects are immutable, i.e.
		// their contents cannot be changed. So every operation you run on a String object
		// (e.g. concatenation + operation) results in another String object being created
		// that holds the result of the operation. This is slow and takes up lots of memory. 
		// StringBuilder allows you to append to an existing StringBuilder.
		StringBuilder results = new StringBuilder();
		for (DenominationFloat f : contents) {
			results.append(f).append("\r\n");
		}
		return results.toString();
	}

}
