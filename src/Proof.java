import java.util.*;

public class Proof {
	
	private List<String> validreason;

	private ArrayList<String> toPrint;
	private LineNumber currentline;
	private boolean iAmDebugging=true;

	public Proof (TheoremSet theorems) {
		currentline = new LineNumber(1); //LineNumber always starts with 1.
		toPrint = new ArrayList<String>();
		validreason = Arrays.asList("assume", "show", "mt", "co", "ic", "mp", "repeat", "print");
	}

	public LineNumber nextLineNumber ( ) {
		return currentline;
	}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		String[] split = x.split("\\s+"); //Split string by white space.
		toPrint.add(currentline + "\t");
		if (iAmDebugging) {
			System.out.println(split[0]);
		}
		if (!validreason.contains(split[0])) {
			throw new IllegalLineException("Bad theorem name."); //Not a valid theorem.			
		}
		if (split[0].equals("mp") || split[0].equals("mt") || split[0].equals("co")) {	
			if (split.length!=3) {
				throw new IllegalLineException("Wrong number of things.");
			}
		} else {
		if (split[0].equals("print")) {
			for(int i=0; i<toPrint.size(); i++) {
				System.out.println(toPrint.get(i));
			} 
		}
		if (split.length!=2) {
			throw new IllegalLineException("Wrong number of things.");
		}if (split[0].equals("assume")) {
		}
		if (split[0].equals("ic")) {
		}
		if (split[0].equals("show")) {
		}
		if (split[0].equals("repeat")) {
		}		
		//Check theorem here
		}
		toPrint.set(toPrint.size()-1, toPrint.get(toPrint.size()-1)+x); //Add string to the rest.
		currentline.findNextLine(split[0]);
	}

	public String toString ( ) {
		return "";
	}

	public boolean isComplete ( ) {
		return false;
	}
}
