import java.util.*;

public class TheoremSet {
	ArrayList<Theorem> myTheorems;
	public TheoremSet () {
		myTheorems = new ArrayList<Theorem>();
	}

	public Expression put (String s, Expression e) {
		Theorem input = new Theorem(s,e.getRoot());
		myTheorems.add(input);
		return null;
	}
	public boolean containsTheorem(String name) {
		if (myTheorems.isEmpty()) {
			return false;
		}else {
			for (Theorem t : myTheorems) {
				if (t.getName().equals(name)) {
					return true;
				}
			}
			return false;
		}
	}
	public Theorem getTheorem(String name) {
		if (containsTheorem(name)) {
			for (Theorem t : myTheorems) {
				if (t.getName().equals(name)) {
					return t;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	public boolean matchesThm(Expression exp) {
		if (exp == null) {
			return false;
		}
		for (Theorem t : myTheorems) {
			if (t.matches(exp)) {
				return true;
			}
		}
		return false;
	}
}
