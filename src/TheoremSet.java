import java.util.*;

public class TheoremSet {
	private HashMap<String,Expression> myTheorems;
	public TheoremSet () {
		myTheorems = new HashMap<String,Expression>();
	}

	public Expression put (String s, Expression e) {
		myTheorems.put(s,e);
		return null;
	}
	public boolean containsTheorem(String name) {
		return myTheorems.containsKey(name);
	}
	public Expression getTheorem(String name) {
		return myTheorems.get(name);
	}
	public boolean containsExpression(Expression e) {
		return myTheorems.containsValue(e);
	}
}
