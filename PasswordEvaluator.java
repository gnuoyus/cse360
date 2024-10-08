package application;

/**
 * <p> PasswordEvaluator Class </p>
 * 
 * <p> Description: The Java based password evaluator to make sure passwords meet requirements.</p>
 *  
 * @author Miriam Khan
 * 
 * @version 1.00		2024-10-02 The Java-based program for the implementing the logic  
 */


public class PasswordEvaluator {
	
	/**********************************************************************************************

	Attributes
	
	**********************************************************************************************/
	
	// These are the variables required to check the password
	// The names of the variables specify their function and each is initialize as required
	public static String passwordErrorMessage = "";
	public static String passwordInput = "";
	public static int passwordIndexofError = -1;
	public static boolean foundUpperCase = false;
	public static boolean foundLowerCase = false;
	public static boolean foundNumericDigit = false;
	public static boolean foundSpecialChar = false;
	public static boolean foundLongEnough = false;
	private static String inputLine = "";
	private static char currentChar;
	private static int currentCharNdx;
	private static boolean running;

	/**********************************************************************************************

	Password Evaluator Actions
	
	**********************************************************************************************/
	
	/**********
	 * Displays the password size, the current index of the character being evaluated, and what that character is
	 */
	private static void displayInputState() {
		System.out.println(inputLine);
		System.out.println(inputLine.substring(0,currentCharNdx) + "?"); //displays password that is being tested
		System.out.println("The password size: " + inputLine.length() + "  |  The currentCharNdx: " + 
				currentCharNdx + "  |  The currentChar: \"" + currentChar + "\""); //displays characteristics of password
	}

	/**********
	 * Public static method which uses logic to determine the character is allowed and gives the reason for it
	 */
	public static String evaluatePassword(String input) {
		//initialize the tools to asses the password
		passwordErrorMessage = "";
		passwordIndexofError = 0;
		inputLine = input;
		currentCharNdx = 0;
		
		//check the case that there is no password
		if(input.length() <= 0) return "*** Error *** The password is empty!";
		
		currentChar = input.charAt(0);		// The current character from the above indexed position

		//initialize the variables to check for a password which are understood by the variable name
		passwordInput = input;
		foundUpperCase = false;
		foundLowerCase = false;	
		foundNumericDigit = false;
		foundSpecialChar = false;
		foundNumericDigit = false;
		foundLongEnough = false;
		running = true;

		//checks if each case for an input to be a password is met and updates variables initialized above
		while (running) {
			displayInputState();
			//Upper case character
			if (currentChar >= 'A' && currentChar <= 'Z') {
				System.out.println("Upper case letter found");
				foundUpperCase = true;
			//Lower case character
			} else if (currentChar >= 'a' && currentChar <= 'z') {
				System.out.println("Lower case letter found");
				foundLowerCase = true;
			//Numeric characters
			} else if (currentChar >= '0' && currentChar <= '9') {
				System.out.println("Digit found");
				foundNumericDigit = true;
			//Special Character
			} else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
				System.out.println("Special character found");
				foundSpecialChar = true;
			//Invalid character
			} else {
				passwordIndexofError = currentCharNdx;
				return "*** Error *** An invalid character has been found!";
			//Character length
			}
			if (currentCharNdx >= 7) {
				System.out.println("At least 8 characters found");
				foundLongEnough = true;
			}
			//go to next character or end if traversed entire password 
			currentCharNdx++; //Increment to the next character
			if (currentCharNdx >= inputLine.length()) //Check if it is greater than the length of the password
				running = false; //End the loop
			else
				currentChar = input.charAt(currentCharNdx); //Go the next character
			
			System.out.println();
		}
		
		//prints error messages for requirements that are not found based on the variables that were updated earlier
		String errMessage = "";
		//Upper case character
		if (!foundUpperCase)
			errMessage += "Upper case; ";
		
		//Lower case character
		if (!foundLowerCase)
			errMessage += "Lower case; ";
		
		//Numeric characters
		if (!foundNumericDigit)
			errMessage += "Numeric digits; ";
		
		//Special Character
		if (!foundSpecialChar)
			errMessage += "Special character; ";
		
		//Character length
		if (!foundLongEnough)
			errMessage += "Long Enough; ";
		
		if (errMessage == "")
			return "";
		
		//tells the user that they did not meet the requirement and updates index where it was not met
		passwordIndexofError = currentCharNdx;
		return errMessage + "conditions were not satisfied";

	}
}
