

public class Theorem extends Expression{
	private String myName;
	
	public Theorem(String name, subExpression input) {
		myName = name;
		root = input;
	}
	public String getName() {
		return myName;
	}
	public subExpression getExpression() {
		return root;
	}
	public boolean matches(Expression exp) {
		if (exp.root.getName().equals(root.getName())) {
			return (matchesHelper(root.getLeft(),exp.root.getLeft())
					&& matchesHelper(root.getRight(), exp.root.getRight()));
		} else {
			return false;
		}
 	}
	private boolean matchesHelper(subExpression thm, subExpression exp) {
		if (thm.getName().equals("|") //theorem has operator
				|| thm.getName().equals("&")
				|| thm.getName().equals("=>")) {
			if (thm.getName().equals(exp.getName())) { //see if operator matches
				return (matchesHelper(thm.getLeft(),exp.getLeft()) //check left operand and right operand
						&& matchesHelper(thm.getRight(),exp.getRight()));
			} else {
				return false;
			}
		} else { //no operand - is 
			//NOT COMPLETE
		}
	}
}
