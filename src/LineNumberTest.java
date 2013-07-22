import junit.framework.TestCase;

import java.util.*;

public class LineNumberTest extends TestCase {

	
	public void testConstructor() {
		LineNumber a = new LineNumber(1);
		assertEquals(a.toString(), "1");
		LineNumber b = new LineNumber(56);
		assertEquals(b.toString(), "56");
	}

	public void testNextLine() {
		//finish after extendproof.
	}
	
}
