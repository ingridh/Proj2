import junit.framework.TestCase;

import java.util.*;

public class LineNumberTest extends TestCase {

	
	public void testConstructor() {
		LineNumber a = new LineNumber(1);
		assertEquals(a.toString(), "1");
		LineNumber b = new LineNumber(56);
		assertEquals(b.toString(), "56");

		
		//Illegal Lines. 
		boolean thrown=false;
		try {
			a.isLegalLine("~"); //Unacceptable character.
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertTrue(thrown);
				
		thrown= false;
		try {
			a.isLegalLine("hello"); //Letters not allowed.
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
		thrown= false;
		try {
			a.isLegalLine("2."); //Extra period.
		} catch (IllegalLineException e) {
			thrown = true;
		}
		
		thrown= false;
		try {
			a.isLegalLine("2..4"); //Two periods in a row.
		} catch (IllegalLineException e) {
			thrown = true;
		}

	}

	public void testNextLine() {
		//finish after extendproof.
	}
	
}
