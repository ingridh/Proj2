import java.util.*;

public class Proof {
	
	private final List<String> validreason= Arrays.asList("assume", "show", "mt", "co", "ic", "mp", "repeat", "print");
	private HashMap<String, Expression> AssumeProven; //Stores proven or assumed expressions.	
	private HashMap<String, Expression> toShow; //Expressions that need to be proved. 
	private ArrayList<String> showOrder; //Line number referring to show in order.
	//Need to add variable for expr to be proved?
	private ArrayList<String> toPrint;
	private LineNumber currentline; 
	private Expression currentExpression;
	private TheoremSet myTheorems;

	
	private boolean iAmDebugging=true;

	public Proof (TheoremSet theorems) {
		toPrint = new ArrayList<String>();
		AssumeProven= new HashMap<String, Expression>();
		toShow= new HashMap<String, Expression>();
		currentline = new LineNumber();
		myTheorems = theorems;
	}

	public LineNumber nextLineNumber ( ) {
		return currentline;
		}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		
		//handle print case with arglength and everything! 
		
		x=x.trim(); //Remove space from beginning and end. 	
		if (x.equals("print")) {
			System.out.println(toString());
			return;
		}
		String[] split = x.split("\\s+"); //Split string by white space.
		String reason = split[0];
		if (iAmDebugging) {
			System.out.println(reason);
		}
		String expr2 = split[split.length-1];
		if (!validreason.contains(reason)) { //Not a valid theorem or reason.	
			throw new IllegalLineException("Bad theorem name."); 		
		}
		checkArgLen(reason, split.length); //Correct argument length.
		if (reason.equals("show")) {
			currentExpression = new Expression(expr2);
			toShow.put(currentline.toString(), currentExpression);	
			showOrder.add(currentline.toString());
		} else {
			//Add theorem to this case too?
			if (!reason.equals("assume")) {	//assume doesn't contain line numbers.	
				String [] NumLine = Arrays.copyOfRange(split, 1, split.length-1);
				checkNumLine(NumLine);
				boolean correctLogic;
				Expression implication;
				String expr1;
				if (NumLine.length==2) { //mp, mt, co cases.
					if (AssumeProven.get(NumLine[0]).getRoot().getName().equals("=>")) {
						implication= AssumeProven.get(NumLine[0]);
						expr1= AssumeProven.get(NumLine[1]).getRoot().getName();
					} else {
						expr1= AssumeProven.get(NumLine[0]).getRoot().getName();
						implication= AssumeProven.get(NumLine[1]);
					}	
					/*if (reason.equals("mp")) {
						correctLogic = (implication.getRoot().getLeft().getName().equals(expr1) &&
								implication.getRoot().getRight().getName().equals(expr2));
					}*/
					if (reason.equals("co")) {
						//Check if ~ is first then if right node equals the other line.
						//Dependent on expr
					}
				
				//Continue on...
				
			}
				//Depends on expr.
				if (reason.equals("ic")) {
					Expression resultexpr = new Expression(expr2);
					// Line referred to must be the right of the expr inferred. 
					//Also must be something the innermost show. 
					if (!(resultexpr.root.myRight.total.equals(AssumeProven.get(NumLine[0])) && 
								expr2.equals(toShow.get(showOrder.get(showOrder.size()-1))))) {
							throw new IllegalInferenceException("Bad inference.");
					} 
				}
				
				//Depends on expr. 
				if (reason.equals("repeat")) {	
					if (!expr2.equals(AssumeProven.get(NumLine[0]))) {
						throw new IllegalInferenceException("Bad inference.");
					}
					
				}
			}
			
					
	
		
		//Check theorem here.
			//What exactly is happening here? Where are the special commands checked for? I need to check for theorems
			//after the special commands are checked for.
			if(myTheorems.containsTheorem(reason)) {
				//TheoremChecker myChecker = new TheoremChecker(reason.getTheorem(),**expression ToBeEvaluated**);
				//if TheoremChecker.matchs(reason.getTheorem(),**expression ToBeEvaluated**) {
				//	myChecker.applyThm(reason.getTheorem(),**expression ToBeEvaluated**);
				//}
			}
			
		
		
		//Successful user input. Placed in AssumeProven.
		AssumeProven.put(currentline.toString(), new Expression(expr2)); 
		}
		
		// Ending a subproof. 
		if (expr2.equals(toShow.get(showOrder.get(showOrder.size()-1)))) {
			AssumeProven.put(showOrder.get(showOrder.size()-1), toShow.get(showOrder.get(showOrder.size()-1)));
			showOrder.remove(showOrder.size()-1);		
		}
		
		currentline.findNextLine(reason); //Actually put this in nextlinenum?
		toPrint.add(currentline + "\t"+x); //Store line in toPrint.
	}
	
	private void checkArgLen(String reason, int splitlen) throws IllegalLineException{
		/*if (iAmDebugging) {
			System.out.println("length" + splitlen + "reason "+ reason);
		}*/
		//Checks correct input length corresponding to reason.
		if ((splitlen==1 || splitlen>4)||  //Can never be in this range.
			((reason.equals("mp") || reason.equals("mt") || reason.equals("co")) 
					&& splitlen!=4) ||
			((reason.equals("ic") && splitlen!=3)) ||
			((reason.equals("assume") || reason.equals("show")) &&
					(splitlen!=2))) {
				throw new IllegalLineException("Wrong number of things.");
			}
	}
					
	private void checkNumLine( String[] line) throws IllegalLineException, IllegalInferenceException {
		for (int i =0; i<line.length;i++) {
			String currentLineStr = line[i]; 
			LineNumber currentNumLine = new LineNumber();
			currentNumLine.setLineNumber(currentLineStr);
			currentline.isAccessibleLine(currentNumLine); //Throws error for illegal references.
			seenLine(currentLineStr); //Throws for nonexistent lines.	
			}	
	}
	

	private void seenLine(String s) throws IllegalInferenceException { //checks if user has called line before.
		Enumeration<String> e = AssumeProven.keys();
		while(e.hasMoreElements()) {
			if (e.nextElement().equals(s)) {
				return;
			}
		}
		throw new IllegalInferenceException("Inaccessible line."); 		
	}
	

	public String toString ( ) {
		String temp="";
		for(int i=0; i<toPrint.size(); i++) {
			temp+= toPrint.get(i) + "\n";
		} 
		return temp;
	}

	public boolean isComplete ( ) {
		if ((toShow.isEmpty())	&& (currentline.toString() != "1")) {
			return true;
		}
		return false;
	}
}
