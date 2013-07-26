import java.util.*;

public class Proof {
	
	private final List<String> validreason= Arrays.asList("assume", "show", "mt", "co", "ic", "mp", "repeat", "print");
	private HashMap<String, Expression> AssumeProven; //Stores proven or assumed expressions.	
	private HashMap<String, Expression> toShow; //Expressions that need to be proved. 
	private ArrayList<String> showOrder; //Line number referring to show in order.
	private ArrayList<String> toPrint;
	private LineNumber currentline; 
	private TheoremSet myTheorems;
	private boolean justSeenShow; //if a show was the last step.

	
	private boolean iAmDebugging=true;

	public Proof (TheoremSet theorems) {
		toPrint = new ArrayList<String>();
		AssumeProven= new HashMap<String, Expression>();
		toShow= new HashMap<String, Expression>();
		currentline = new LineNumber();
		showOrder= new ArrayList<String>();
		myTheorems = theorems;
	}

	public LineNumber nextLineNumber ( ) {
		return currentline;
		}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {

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
		String inputExprStr = split[split.length-1];
		Expression inputExpr = new Expression(inputExprStr);
		if (!validreason.contains(reason)) { //Not a valid theorem or reason.	
			throw new IllegalLineException("Bad theorem name."); 		
		}
		checkArgLen(reason, split.length); //Correct argument length.
		if (reason.equals("show")) {
			toShow.put(currentline.toString(), inputExpr);	
			showOrder.add(currentline.toString());
			toPrint.add(currentline + "\t"+x); //Store line in toPrint.
			currentline.startSubproof();
			justSeenShow = true;
		} else {
			String innerShow = showOrder.get(showOrder.size()-1);
			if (reason.equals("assume")) {
				if (!justSeenShow) {
					throw new IllegalInferenceException("Assume must be after show.");
				}
				AssumeProven.put(currentline.toString(), inputExpr); 
			} else {
				String [] NumLine = Arrays.copyOfRange(split, 1, split.length-1);
				checkNumLine(NumLine);
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
								implication.getRoot().getRight().getName().equals(inputexpr));
					}*/
					if (reason.equals("co")) {
						//Fix this!!! Var must be outside to be correct.
						if (AssumeProven.get(NumLine[0]).getRoot().getName().equals("~")) {
							Expression hasNegate = AssumeProven.get(NumLine[0]);
							Expression noNegate = AssumeProven.get(NumLine[1]);
						} else {
							Expression hasNegate = AssumeProven.get(NumLine[1]);
							Expression noNegate = AssumeProven.get(NumLine[0]);							
						}
						if (hasNegate.getRoot().equals(noNegate.getRoot())) {
						}					
						//Check if ~ is first then if right node equals the other line.
					}
				
				
			} else {
				if (reason.equals("ic")) {
					// Line referred to must be the right of the expr inferred. 
					//Also must be something the innermost show. 
					if (!(inputExpr.getRoot().getRight().equals(AssumeProven.get(NumLine[0]).getRoot()) && 
								inputExpr.getRoot().equals(toShow.get(innerShow).getRoot()))) {
							throw new IllegalInferenceException("Bad inference.");
					} 
				}
				
				if (reason.equals("repeat")) {	
					if (!inputExpr.getRoot().equals(AssumeProven.get(NumLine[0]).getRoot())) {
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
		
		}
		//Successful user input. Placed in AssumeProven.
		AssumeProven.put(currentline.toString(), inputExpr); 
		toPrint.add(currentline + "\t"+x); //Store line in toPrint.
		// Proving innermost show and ending a subproof. 
		if (inputExpr.getRoot().equals(toShow.get(innerShow).getRoot())) {
			AssumeProven.put(innerShow, toShow.get(innerShow));
			showOrder.remove(showOrder.size()-1);		
		} else{
			currentline.nextLine();
		}
		justSeenShow=false; //This step is not a show.
	}
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
			if (iAmDebugging) {
				System.out.println(!AssumeProven.containsKey(currentLineStr));
			}
			if (!AssumeProven.containsKey(currentLineStr)) { //Throws for nonexistent lines.	
				throw new IllegalInferenceException("Line doesn't exist: " + currentLineStr); 		
			}
		}	
	}

	public String toString ( ) {
		String temp="";
		for(int i=0; i<toPrint.size(); i++) {
			temp+= toPrint.get(i) + "\n";
		} 
		return temp;
	}

	public boolean isComplete ( ) {
		return ((showOrder.isEmpty()) && (currentline.toString() != "1"));
	}
}