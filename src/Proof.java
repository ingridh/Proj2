import java.util.*;

public class Proof {
	
	private final List<String> validreason= Arrays.asList("assume", "show", "mt", "co", "ic", "mp", "repeat", "print");
	private Hashtable<String, Expression> AssumeProven; //Stores proven or assumed expressions.	
	private Hashtable<String, Expression> toShow; //Expressions that need to be proved. 
	//Need to add variable for expr to be proved?
	private ArrayList<String> toPrint;
	private LineNumber currentline; 
	private Expression currentExpression;

	
	private boolean iAmDebugging=true;

	public Proof (TheoremSet theorems) {
		toPrint = new ArrayList<String>();
		AssumeProven= new Hashtable<String, Expression>();
		toShow= new Hashtable<String, Expression>();
		currentline = new LineNumber();
	}

	public LineNumber nextLineNumber ( ) {

		return currentline;
		
		}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		
		//handle print case with arglength and everything! 
		
		x=x.trim(); //Remove space from beginning and end. 		
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
					if (reason.equals("mp")) {
						correctLogic = (implication.getRoot().getLeft().getName().equals(expr1) &&
								implication.getRoot().getRight().getName().equals(expr2));
					}
				
				//Continue on...
				
			}
			}
			
					
		//Use toString for print?
		/*if (split[0].equals("print")) {
			for(int i=0; i<toPrint.size(); i++) {
				System.out.println(toPrint.get(i));
			} 
			return;
		}*/		
		
		//Check theorem here.
		
		//Successful user input. Placed in AssumeProven.
		AssumeProven.put(currentline.toString(), new Expression(expr2)); 
		//toPrint.set(toPrint.size()-1, toPrint.get(toPrint.size()-1)+x); //Add string to the rest.
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
		return "";
	}

	public boolean isComplete ( ) {
		return currentExpression.proven();	
	}
}
