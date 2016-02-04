import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This is the main class - the program is run by java Shop
 *
 * @author Shankly Richard Cragg (shc27)
 * @version 1.1 (07th March 2015)
 */

public class Shop {

	private String shopName;
	private boolean exist = false;
	private Scanner scan;
	private UKTill till;
	private ArrayList<Item> items;
	private int totalCostDue; // Store as pence
	private int newQuantity;
	private static final String SHOP_STOCK_DATA_FILE = "./stock.txt";
	private static final String SHOP_TILL_DATA_FILE = "./till.txt";

	public Shop(String name) {
		shopName = name;
		till = new UKTill();
		scan = new Scanner(System.in);
		items = new ArrayList<Item>();
	}

	/**
	 * Using stockShop an owner can add various goods to the system.
	 * The owner will enter the barcode, the name, the cost, and how
	 * many of the item he wishes to add. An object is created with
	 * these attributes. It will then get added to the stock arrayList.
	 */
	public void stockShop() {
		do {
			String id;
			String n = null;
			//ENTERING ID
			id = getBarcode();
			//do {
			//	id = scan.nextLine();				 // Read input from the console and store in id
			//} while (!id.matches("\\d\\d\\d\\d"));	 // While the string "id" does not equal a series of 4 digits, user must input new value for id
			Item item = new Item(id);				 // Create item with this id
			//-----------------------------------
			
			//ENTERING NAME
			System.out.println("Name:");
			do {
				n = scan.nextLine(); 			// No restriction on the naming of items, can be an integer if the user desires
				//scan.nextLine();
			} while (n.length() == 0);
			item.setName(n);
			//-----------------------------------
			
			//ENTERING COST
			int c = getInt("Cost: (In pennies) ");
			item.setCost(c);
			//-----------------------------------
			
			//ENTERING QUANTITY
			int q = getInt("Quantity: ");
			item.setQuanity(q);
			//-----------------------------------
			
			// Add item to stock
			items.add(item);
			System.out.println("Item added: " + id + " " + n);
			//-----------------------------------
		} while (doContinue());
	}

	/**
	 * Using startTill, an owner can add the various denomination floats to the
	 * till specifying the name, and quantity of each item (If she has 33
	 * "10p pieces", she enters "10p", followed by "33" (on separate lines)).
	 * This gets added t the till.
	 */
	public void startTill() {
		do {
			UKDenomination ct = getDenominationType();
			int nc = getInt("Number of these coins: ");
			DenominationFloat m = new DenominationFloat(ct, nc);
			till.addFloat(m);

			System.out.println("Denomination floats enetered into till: " + m);
		} while (doContinue());
	}

	/**
	 * Using runTill, an owner can sell items the customer has chosen to buy. 
	 * The owner must scan the customers items (By entering the barcode manually).
	 * The customer is then told how much the summation of all the items currently
	 * scanned costs. After each item is scanned the quantity of that item in stock
	 * is reduced. If the customer tries buying an item that is out of stock (or a
	 * barcode is entered that doesn't exist), an error message is displayed. Once
	 * finished, the user is told their total cost to pay.
	 */
	public void runTill() {
	 	do {
	 		String barcode = null;
	 		if(items.size() != 0) {								//Only runs if there is stock
		 		barcode = getBarcode();							//Call getBarcode method
				for (Item i : items) {							//Loops through all the items in the arrayList
					if (barcode.equals(i.getIdentifier())) {	
						displayCost("This item costs £", i.getCost());
						exist = true;							//If the item exists then change the value of exist to true
						totalCostDue += i.getCost();			//Updates the cost of the customers items
						newQuantity = (i.getQuanity() - 1);		//Decrements the quantity by 1
						i.setQuanity(newQuantity);
							if (i.getQuanity() == 0){			//If the quantity is zero, then the item can't be bought, and so it removed
								items.remove(i);
									if(items.size() == 0) {;
										break;
									}
							}
						break;
					}
				}
				if (exist == false) {							//If the item did exist, then the value will be true and nothing will be printed
					System.out.println("That item is out of stock, doesn't exist, or you entered the barcode incorrectly");
				} 	
				exist = false;									//If the value was true, then it needs to be reset back to false so that any subsequent run through works the same as the first
	 		}
			else {												//If no stock, tell the customer
				System.out.println("We are out of stock! There is nothing else you can purchase");
	 		}		 		
	 		System.out.println("Your price to pay (in pennies) is: " + totalCostDue);	//Tells the customer how much they have to pay
		} while (doContinue());	
	}	
	
	
	/**
	 * Using getChange, an owner can tell the system how much of each
	 * denomination she has been given by the customer and the till tells her
	 * what to giveback.
	 * The customer is asked to enter a denomination to pay for
	 * their goods, and the amount of it. The customer does this 
	 * until the amount given is enough, or too much to pay for their
	 * goods. Once this happens, the till automatically works out how 
	 * much money to give back to the customer as change.
	 */
	public void getChange() {
		if (totalCostDue > 0) {												//Only does anything if there is change to be given
			System.out.println("Hand over the money!");					
			do {
				UKDenomination ct = getDenominationType();					//The user enters the type of money they want to give
				int valu = ct.getValue();									//Gets the value of the money they entered
				int numOfCoins = getInt("Please enter amount of currency");
				DenominationFloat n = new DenominationFloat(ct, numOfCoins);//Creates a denomination using the values to user entered
				till.addFloat(n);											//Adds this to the till
				totalCostDue = totalCostDue - (valu * numOfCoins);			//The cost the customer has to pay is decreased by the amount they gave
				System.out.println("Price left to pay is " + totalCostDue);	
			} while (totalCostDue > 0);
	
			// Calculate change
			if (totalCostDue == 0) {										
				System.out.println("You provided the exact amount, thank you!");
			} else {
				DenominationFloat[] change = till.getChange(Math.abs(totalCostDue));
				System.out.println("Here is your change:");
				for (DenominationFloat m : change) {
					if (m != null) {
						System.out.println(m);
					}
				}
			}
		}
		else {
			System.out.println("Nothing in shopping basket");
			System.out.println("\n");
		}
	}
		

	/**
	 * Using getBalance it tells the owner what is left in the till
	 */
	public void getBalance() {
		System.out.println(till);
	}

	/**
	 * runMenu provides the main menu to the shop allowing a user to select
	 * their required operation
	 */
	public void runMenu() {
		// This is the main menu which runs the whole shop

		String choice;
		do {
			printMenu();
			choice = scan.nextLine();

			switch (choice) {
			case "1":
				stockShop();
				break;
			case "2":
				startTill();
				break;
			case "3":
				runTill();
				break;
			case "4":
				getChange();
				break;
			case "5":
				getBalance();
				break;
			case "6":
				System.out.println("Thankyou for running " + shopName
						+ " program");
				break;
			default:
				System.err.println("Incorrect choice entered");
			}
		} while (!choice.equals("6"));
	}

	/**
	 * This is used to ask the customer if they wish to continue doing the
	 * action they are currently doing.
	 * @return
	 */
	private boolean doContinue() {
		System.out.println("Continue? (Y/N)");
		String answer = scan.next().toUpperCase();
		scan.nextLine();
		return answer.equals("Y");
	}

	/**
	 * This is used to enter a barcode and gurantees it is
	 * in the correct format
	 * @return
	 */
	private String getBarcode() {
		String result = null;
		System.out.println("Please enter Identifier of length 4 (Must be integers):"); // Define to the user what to enter
		do {
 			result = scan.next();					// Read input from the console and store in id
 		} while (!result.matches("\\d\\d\\d\\d"));	// While the string "id" does not equal a series of 4 digits, user must input new value for id
		return result;
	}
	
	/**
	 * This is used when an int is required to be entered
	 * @param message
	 * @return
	 */
	private int getInt(String message) {
		boolean correct = false;
		int result = 0;
		do {
			System.out.println(message);
			try {
				result = scan.nextInt();
				scan.nextLine();
				correct = true;
			} catch (InputMismatchException ime) {
				System.err.println("Please enter an number");
				scan.nextLine();
			}
		} while (!correct);
		return result;
	}

	/**
	 * This is used to convert the price from pence into pounds
	 * and prints a message to the customer
	 * @param message
	 * @param amountInPence
	 */
	private void displayCost(String message, int amountInPence) {
		System.out.format("%s %d.%02d\n", message, amountInPence / 100,
				amountInPence % 100);
	}

	/**
	 * This is called when the user is required to enter a choice
	 * for the type of money they wish to use (e.g. £2) 
	 * @return
	 */
	private UKDenomination getDenominationType() {
		UKDenomination result;
		do {
			System.out.println("Enter the denomination type. One of: ");
			for (UKDenomination denom : UKDenomination.values()) {
				System.out.print(denom + " ");
			}
			String choice = null;
			choice = scan.nextLine();
			result = UKDenomination.fromString(choice);
			if (result == null) {
				System.err.println("Incorrect denomination entered. Try again!");
			}
		} while (result == null);
		return result;
	}

	private void printMenu() {
		System.out.println("Welcome to " + shopName + ". Please enter choice:");
		System.out.println("1 - Stock the shop");
		System.out.println("2 - Add coins to the till");
		System.out.println("3 - Process customer order");
		System.out.println("4 - Process customer payment");
		System.out.println("5 - Display till balance");
		System.out.println("6 - Exit shop program");
	}

	/**
	 * Calls the methods to save the Till and the Stock
	 * @exception IOException thrown when file problems occur
	 */
	public void save() throws IOException {
		saveTill();
		saveStock();
	}
		
	
	/**
	 * This puts the arrayList till into a text file
	 * to remember the money in the till
	 */
	public void saveTill() throws IOException {
		PrintWriter outfile = new PrintWriter(new FileWriter(SHOP_TILL_DATA_FILE));
		//outfile.print(" ");
		outfile.println(till);
		outfile.close();
	}
	
	/**
	 * This puts the arrayList items into a text file
	 * to remember the items in stock
	 */
	public void saveStock() throws IOException {
		int num = items.size();						//Stores the amount of items so that when loading we know how many times to iterate
		PrintWriter outfile1 = new PrintWriter(new FileWriter(SHOP_STOCK_DATA_FILE));
		outfile1.println(num);
		for (Item i : items) {
			outfile1.println(i.getIdentifier());
			outfile1.println(i.getName());
			outfile1.println(i.getCost());
			outfile1.println(i.getQuanity());
		}
		outfile1.close();
	}

	/**
	 * Calls the methods to load the Till and the Stock
	 * @exception IOException thrown when file problems occur
	 */
	public void load() throws IOException {
		loadStock();
		loadTill();
	}
	
	/**
	 * Reads in data from the stock file and puts it into the stock arrayList.
	 * Fills the shop with stock still available from the last time the shop was open.
	 * @throws IOException
	 */
	public void loadStock() throws IOException {
		items.clear();
		Scanner infile = new Scanner(new FileReader(SHOP_STOCK_DATA_FILE));
		BufferedReader br = new BufferedReader(new FileReader(SHOP_STOCK_DATA_FILE));	//Use a buffered reader to check if the file is empty
		if (br.readLine() != null) {											//If it is empty, do not run the load code
			int num = infile.nextInt();					//Checks how many items are inside the file
			for (int i = 0; i < num; i++) {				//Loops for as many items
					String id = infile.next();
					infile.nextLine();
					String n = infile.next();
					infile.nextLine();
					int c = infile.nextInt();
					infile.nextLine();
					int q = infile.nextInt();
					infile.nextLine();
					Item item = new Item(id, n, c, q);	//Creates an item with all the data read in
					items.add(item);					//Adds this item to an array list of items
			}
			infile.close();
			br.close();
		}
	}
	/**
	 * Reads in data from the till file and puts it into the till.
	 * Fills the till with money from last time.
	 * @throws IOException
	 */
	
	public void loadTill() throws IOException {
		final int num = 12;
		UKDenomination returnedDenomType;
		String denomType;		//For storing the read in type of money
		int denomQuantity;		//for storing the read in amount of said money
		Scanner infile = new Scanner(new FileReader(SHOP_TILL_DATA_FILE));
		BufferedReader br = new BufferedReader(new FileReader("./till.txt"));	//Use a buffered reader to check if the file is empty
			if (br.readLine() != null) {										//If it is empty, do not run the load code
				for (int i = 0; i < num; i++) {
					denomType = infile.next();
					returnedDenomType = UKDenomination.fromString(denomType);
					denomQuantity = infile.nextInt();
					infile.nextLine();
					DenominationFloat X = new DenominationFloat(returnedDenomType, denomQuantity);
					till.addFloat(X);
				}
			}
		infile.close();
		br.close();
	}
	
	public static void main(String[] args) {
		// Don't touch any of this code
		Shop migginsPieShop = new Shop("Mrs Miggins Pie Shop");
		try {
			migginsPieShop.load();
		} catch (IOException e) {
			// Something went wrong so start a new shop
			System.err.println("Sorry but we were unable to load shop data: "
					+ e.getMessage());
		}

		migginsPieShop.runMenu();

		try {
			migginsPieShop.save();
		} catch (IOException e) {
			System.err
					.println("Sorry but we just lost everything. Unable to save shop data: "
							+ e.getMessage());
		}
	}
}
