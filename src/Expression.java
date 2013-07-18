public class Expression {

	private boolean value;	// true of false value of node (can be set by "assume" or by checking logic)
	private String prop;	// string representation of the proposition or operand
	
	public Expression (String s) throws IllegalLineException {
		
	}
	
	public void set(boolean b) {
		value = b;
	}
}
