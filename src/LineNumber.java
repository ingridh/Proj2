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
	
				
	public String toString() {
		String temp = "";
		//Turns ArrayList into a string with periods.
		for (int i = 0; i<current.size();i++) {
			temp += Integer.toString(current.get(i)) +".";
		}
		return temp.substring(0,temp.length()-1); //Exclude last period.
	}
}
