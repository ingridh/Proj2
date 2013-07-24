import java.util.*;

public class LineNumber {	
	
	public ArrayList<Integer> current;
	
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
	
	
	public boolean isLegalLine(String s) {
		String[] temp= s.split("\\.");
		System.out.println(temp.length);
		
		if (current.size()<temp.length) {
			return false;
		}
		
		//Checking that all but the last digit are the same.
		for (int i=0; i<temp.length-1;i++) {
			int tempInt = Integer.parseInt(temp[i]);
			System.out.println(tempInt + "compare" + current.get(i));
			if (tempInt!=current.get(i)) {
				return false; 
			}
		}
		int lastcurr = (current.get(temp.length-1));
		int lastInt = Integer.parseInt(temp[temp.length-1]);
		System.out.println(lastInt + "last compare" + current.get(temp.length-1));
		System.out.println("last" + (lastInt <lastcurr) );
		return (lastcurr<lastInt);	
	}
	//What if it's a letter?	
	
				
	public String toString() {
		String temp = "";
		//Turns ArrayList into a string with periods.
		for (int i = 0; i<current.size();i++) {
			temp += Integer.toString(current.get(i)) +".";
		}
		return temp.substring(0,temp.length()-1); //Exclude last period.
	}
}
