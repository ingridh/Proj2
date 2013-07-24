import java.util.*;

public class LineNumber {	
	
	public ArrayList<Integer> current;
	
	boolean iAmDebugging = true;
	
	public LineNumber(int s) {
		current = new ArrayList<Integer>();
		current.add(s);
	}

	
	public void findNextLine(String s) {
		
		//Needs another case for valid but unnecessary contradictions.
		//Find nextline if completes subproof/proof.
		
		if (s.equals("show") && current.get(0)!=1) {
			current.add(1); //Start subproof.
		} else {
			if (s.equals("ic")) {
					if (current.size()!=1) { //Inside a subproof.
						current.remove(current.size()-1); //Exit subproof.
					}
				}
			//Next line outside of subproof.
			current.set(current.size()-1, current.get(current.size()-1)+1);
			}
		}
	
	
	public boolean isLegalLine(String s) throws IllegalLineException {		
		String[] temp= s.split("\\.");
		//Correct amount of periods.
		int counter=0;
		for (int i=0; i<s.length(); i++) {
			if (s.charAt(i)=='.') {
				counter++;
			}
		}
		if (counter!=temp.length-1) {
			throw new IllegalLineException("Line has too many periods.");
		}
		/*if (iAmDebugging) {
			System.out.println(temp.length);	
		}*/ if (current.size()<temp.length) {
			return false; //Line cannot be longer than current.
		}
		//Checking that all but the last digit are the same.
		for (int i=0; i<temp.length-1;i++) {
			int tempInt = Integer.parseInt(temp[i]);
			/*if (iAmDebugging) {
				System.out.println(tempInt + "compare" + current.get(i));
			}*/ if (tempInt!=current.get(i)) {
				return false; 
			}
		} try {
			return Integer.parseInt(temp[temp.length-1]) < current.get(temp.length-1);	
		} catch (NumberFormatException i) { //Not integers.
			throw new IllegalLineException("Line reference can only be numbers.");
		}
	}
	
	public String toString() {
		String temp = "";
		//Turns ArrayList into a string with periods.
		for (int i = 0; i<current.size();i++) {
			temp += Integer.toString(current.get(i)) +".";
		}
		return temp.substring(0,temp.length()-1); //Exclude last period.
	}
}
