
public class Expression {
	
	public subExpression root;

	public Expression () {
		root = null;
	}
	
	public Expression (String s) throws IllegalLineException {
		if (s != null) {
			if (s.contains(" ")) {
				throw new IllegalLineException ("Error: expression cannot have spaces");
			} else {
				root = constructorHelper(s);
			}
		}
	}
	
	private subExpression constructorHelper (String s) throws IllegalLineException{
    	subExpression node;
		if (s.charAt (0) != '(') {
	    	if ((s.charAt (0) == '~') && (s.length() > 1)) {
	    		node = new subExpression (s,"~", constructorHelper(s.substring(1,s.length())),null);
	    	} else if ((s.length() == 1) && (Character.isLowerCase(s.charAt(0)))) {
	    		node = new subExpression (s,s);
	    	} else {
	    		throw new IllegalLineException("Error: proposition must be single lowercase letter");
	    	}
	         return node;
		} else if ((s.charAt(1) == '~') && (s.charAt(2) == '(')) {		// allows for either ~(p=>q) or (~(p=>q))
			return new subExpression (s,"~", constructorHelper(s.substring(2,s.length() - 1)), null);
	    } else {							// finds top-level operand to be root node of the Expression Tree
	        int nesting = 0;
	        String opnd1 = null;
	        String opnd2 = null;
	        String op = null;
	        for (int k=1; k<s.length()-1; k++) {
	            if (s.charAt(k) == '(') {
	            	nesting++;
	            } else if (s.charAt(k) == ')') {
	            	nesting--;
	            } else if ((s.charAt(k) == '&' || s.charAt(k) == '|') && (nesting == 0)) {
	    	        opnd1 = s.substring (1, k);
	    	        opnd2 = s.substring (k+1, s.length()-1);
	    	        op = s.substring (k, k+1);
	            	break;
	            } else if ((s.charAt(k) == '=') && (nesting == 0)) {
	    	        opnd1 = s.substring (1, k);
	    	        opnd2 = s.substring (k+2, s.length()-1);
	    	        op = s.substring (k, k+2);
	    	        break;
	            }
	        }
	        if ((opnd1 == null) || (opnd2 == null || (op == null))) {
	        	throw new IllegalLineException("Error: unbalanced parentheses or no operands");
	        }
	        if (ProofChecker.iAmDebugging) {
	        	System.out.println ("expression = " + s);
	        	System.out.println ("operand 1  = " + opnd1);
	        	System.out.println ("operator   = " + op);
	        	System.out.println ("operand 2  = " + opnd2);
	        	System.out.println ( );
	        }
	        return new subExpression(s,op, constructorHelper(opnd1), constructorHelper(opnd2));
	    }
	}
	
	public void print ( ) {
	    if (root != null) {
	        printHelper (root, 0);
	    }
	    System.out.println("");
	}
		
	private static final String indent1 = "    ";
		
	private static void printHelper (subExpression cur, int indent) {
	    if (cur.myRight != null) {
	    	Expression.printHelper(cur.myRight, indent+1);
	    }
	    println (cur.name, indent);
	    if (cur.myLeft != null) {
	    	Expression.printHelper(cur.myLeft, indent+1);;
	    }
	}
			
	private static void println (Object obj, int indent) {
	    for (int k=0; k<indent; k++) {
	        System.out.print (indent1);
	    }
	    System.out.println (obj);
	}
	
	public subExpression getRoot() {
		return root;
	}
	
	public boolean proven() {
		if ((root.value == true) && (root.isSet == true)) {
			return true;
		}
		return false;
	}
	
	// invariants: no parentheses in subExpression names
	// no operands at leaves
	// if subExpression has myLeft, but no myRight, name must be "~"
	public boolean isOK () {
		try {
			check(root);
		} catch(IllegalLineException e){
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	private void check (subExpression x) throws IllegalLineException {
		if ((x.name.contains("(")) || (x.name.contains(")"))) {
			throw new IllegalLineException("parentheses in proposition");
		}
		
		if (x.myLeft != null) {
			if ((x.myRight == null) && (x.name != "~")) {
				throw new IllegalLineException("unbalanced expression");
			}
			check(x.myLeft);
		}
		if (x.myRight != null) {
			if (x.myLeft == null) {
				throw new IllegalLineException("unbalanced expression");
			}
			check(x.myRight);
		}
	}
	
	public void set (String s, boolean b) {
		// NOT COMPLETE
		try {
			findAndSet (root,s,b);
		} catch (IllegalInferenceException e) {
			System.out.println(e.getMessage());;
		}
	}

	public void findAndSet (subExpression x, String s, boolean b) throws IllegalInferenceException {
		// sets every subExpression with total == s to be true
		// searched Expression tree in preorder
		if (ProofChecker.iAmDebugging==true) {
			if (x == root) {
				System.out.println("setting " + s + " to " + b);
			}
			System.out.println("checking node " + x.getTotal());
		}
		if (s.equals(x.total)) {
			x.value = b;
			x.isSet = true;
			if (s.charAt(0) == '~') {
				x.myLeft.value = !b;
				x.myLeft.isSet = true;
				if (ProofChecker.iAmDebugging==true) {
					System.out.println(x.myLeft.getTotal() + " is now set to " + !b);
				}
			}
			if (ProofChecker.iAmDebugging==true) {
				System.out.println(x.getTotal() + " is now set to " + b);
			}
		}
		if (x.myLeft != null) {
			findAndSet(x.myLeft,s,b);
		}
		if (x.myRight != null) {
			findAndSet(x.myRight,s,b);
		}
	}
	
	public static class subExpression {
		
		public boolean value;	// true of false value of node (can be set by "assume" or by checking logic)
		public boolean isSet;	// false if value is false by default
		public String name;	// string representation of the proposition or operand
		public String total;	// string representation of expression of which this node is the highest level operand
		public subExpression myLeft;
		public subExpression myRight;
		
		public subExpression (String t, String s) {
			name = s;
			total = t;
			myLeft = myRight = null;
		}
		
		public subExpression (String t, String s, subExpression left, subExpression right) {
			name = s;
			total = t;
			myLeft = left;
			myRight = right;
		}
		
		public boolean getValue() {
			return value;
		}
		
		public boolean getIsSet() {
			return isSet;
		}
		
		public String getName() {
			return name;
		}
		public String getTotal() {
			return total;
		}
		public subExpression getLeft() {
			return myLeft;
		}
		public subExpression getRight() {
			return myRight;
		}
		public boolean isVariable() {
			if (myLeft == null && myRight == null) {
				return true;
			} else {
				return false;
			}
		}
		public boolean equals(subExpression e) {
			boolean lReturn = false;
			boolean rReturn = false;
			if (e == null) {
				return false;
			}
			if (!(name.equals(e.name))){
				System.out.println("My name"+ name);
				System.out.println("compare"+e.name);
				return false;
			} else {
				if (myLeft != null && e.myLeft != null) {
					lReturn = myLeft.equals(e.myLeft);
				} else if (myLeft == null && e.myLeft == null) {
					lReturn = true;
				}
				if (myRight != null && e.myRight != null) {
					rReturn = myRight.equals(e.myRight);
				} else if (myRight == null && e.myRight == null) {
					rReturn = true;
				}
			}
			return lReturn && rReturn;
		}
		public void setValid() {
			this.value = true;
			if(myLeft != null) {
				myLeft.setValid();
			}
			if(myRight != null) {
				myRight.setValid();
			}
		}

	}
}
