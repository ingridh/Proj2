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
		currentline = new LineNumber(1); //LineNumber always starts with 1.
		toPrint = new ArrayList<String>();
		AssumeProven= new Hashtable<String, Expression>();
		toShow= new Hashtable<String, Expression>();
	}

	public LineNumber nextLineNumber ( ) {
		return currentline;
	}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		x=x.trim(); //Remove space from beginning and end. 		
		String[] split = x.split("\\s+"); //Split string by white space.
		String reason = split[0];
		if (iAmDebugging) {
			System.out.println(reason);
		}
		String expr = split[split.length-1];
		if (!validreason.contains(reason)) { //Not a valid theorem or reason.	
			throw new IllegalLineException("Bad theorem name."); 		
		}
		checkArgLen(reason, split.length); //Correct argument length.
		if (reason.equals("show")) {
			currentExpression = new Expression(expr);
			toShow.put(currentline.toString(), currentExpression);				
		} else {
			//Add theorem to this case too?
			if (!reason.equals("assume")) {	//assume doesn't contain line numbers.	
				String [] NumLine = Arrays.copyOfRange(split, 1, split.length-1);
				checkNumLine(reason, NumLine);
			}
					
		//Use toString for print?
		/*if (split[0].equals("print")) {
			for(int i=0; i<toPrint.size(); i++) {
				System.out.println(toPrint.get(i));
			} 
			return;
		}*/		
		
		//Check theorem here
		
		//Successful user input. Placed in AssumeProven.
		AssumeProven.put(currentline.toString(), new Expression(expr)); 
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
		if (splitlen==1 || splitlen>4) { //Can never be in this range.
			throw new IllegalLineException("Wrong number of things.");
		} else if (reason.equals("mp") || reason.equals("mt") || reason.equals("co")) {	
			if (splitlen!=4) {
				throw new IllegalLineException("Wrong number of things.");
			}
		} else if (reason.equals("ic")) {
			if (splitlen!=3) {
				throw new IllegalLineException("Wrong number of things.");
			}
		} else {
			if (splitlen!=2) {
				throw new IllegalLineException("Wrong number of things.");
			} 
		}
	}
	
	private void checkNumLine( String reason, String[] line) throws IllegalLineException {
		for (int i =0; i<line.length;i++) {
			if (!seenLine(line[i])) { //No such line exists.	
				throw new IllegalLineException("Inaccessible line."); 		
			}
			if (!currentline.isLegalLine(line[i])) { //Illegal references.
				throw new IllegalLineException("Bad line number."); //Needs to include the wrong line?				
			}
		}	
		
	}
	
	private boolean seenLine(String s) { //checks if user has called line before.
		Enumeration<String> e = AssumeProven.keys();
		while(e.hasMoreElements()) {
			if (e.nextElement().equals(s)) {
				return true;
			}
		}
		return false;
	}
	

	public String toString ( ) {
		return "";
	}

	public boolean isComplete ( ) {
		return currentExpression.proven();	
	}
}
