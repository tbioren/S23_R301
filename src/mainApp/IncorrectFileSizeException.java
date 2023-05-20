package mainApp;

/** 
 * @author S23_R301
 * 
 * This is the custom exception that is thrown when the given chromosome file is not
 * in the correct format (2x10 or 10x10).
 *
 */
public class IncorrectFileSizeException extends Exception {
	public final String CORRECT_SIZE_20 = "2 x 10";
	public final String CORRECT_SIZE_100 = "10 x 10";
	
	/**
	 * Default Constructor
	 */
	public IncorrectFileSizeException() {}

	/**
	 * Prints an error message that informs the user that the given file size has an
	 * incorrect chromosome format
	 */
	public void printErrorMessage() {
		System.err.println("Incorrect chromosome size\nPlease enter a " + CORRECT_SIZE_20 + " or " + CORRECT_SIZE_100);
	}
}
