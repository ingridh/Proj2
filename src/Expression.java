public class Expression {
	
	protected subExpression root;

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
	    		node = new subExpression ("~", constructorHelper(s.substring(1,s.length())),null);
	    	} else if ((s.length() == 1) && (Character.isLowerCase(s.charAt(0)))) {
	    		node = new subExpression (s);
	    	} else {
	    		throw new IllegalLineException("Error: proposition must be single lowercase letter");
	    	}
	         return node;
	} else if ((s.charAt(1) == '~') && (s.charAt(2) == '(')) {		// allows for either ~(p=>q) or (~(p=>q))
		return new subExpression ("~", constructorHelper(s.substring(2,s.length() - 1)), null);
	} else {								// finds top-level operand to be root node of the Expression Tree
	        int nesting = 0;
	        String opnd1 = null;
	        String opnd2 = null;
	        String op = null;
	        for (int k=1; k<s.length()-1; k++) {
	            if (s.charAt(k) == '(') {
	            	nesting++;
	            } else if (s.charAt(k) == ')') {
	            	nesting--;
	            } else if ((s.charAt(k) == '&' ||
	            		s.charAt(k) == '|') 
	            		&& (nesting == 0)) {
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
	        return new subExpression(op, constructorHelper(opnd1), constructorHelper(opnd2));
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
	
	public void set (boolean b) {
		root.value = b;
		root.isSet = true;
	}
	public subExpression getRoot() {
		return root;
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
	
	protected static class subExpression {
		
		private boolean value;	// true or false value of node (can be set by "assume" or by checking logic)
		private boolean isSet;	// false if value is false by default
		private String name;	// string representation of the proposition or operand
		private subExpression myLeft;
		private subExpression myRight;
		
		public subExpression (String s) {
			name = s;
			myLeft = myRight = null;
		}
		
		public subExpression (String s, subExpression left, subExpression right) {
			name = s;
			myLeft = left;
			myRight = right;
		}
		public String getName() {
			return name;
		}
		public subExpression getLeft() {
			return myLeft;
		}
		public subExpression getRight() {
			return myRight;
		}
	}
}
