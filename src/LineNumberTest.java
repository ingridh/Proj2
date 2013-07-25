import junit.framework.TestCase;

import java.util.*;

public class LineNumberTest extends TestCase {

	
	public void testConstructor() {
		LineNumber a = new LineNumber();
		assertEquals("1", a.toString());	
	}
	
	public void testSetLineNum()  {
		boolean thrown=false;
		
		//Legal Lines.
		LineNumber c = new LineNumber();
		try {
			c.setLineNumber("3.2.1");
		} catch (IllegalLineException e1) {
			thrown = true;
		}
		assertEquals("3.2.1", c.toString());
		assertFalse(thrown);
		try {
			c.setLineNumber("2.1");
		} catch (IllegalLineException e1) {
			thrown = true;
		}
		assertEquals("2.1", c.toString());
		assertFalse(thrown);
		try {
			c.setLineNumber("6.11");
		} catch (IllegalLineException e1) {
			thrown = true;
		}
		assertEquals("6.11", c.toString());
		assertFalse(thrown);
		
		//Illegal Lines. 
		thrown = false;
		try { //Unacceptable character.
			c.setLineNumber("~");
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Line reference can only be numbers and periods.");
			thrown = true;
		}
		assertTrue(thrown);
		
		try {//Invalid letters.
			c.setLineNumber("hello"); 
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Line reference can only be numbers and periods.");
			thrown = true;
		}
		assertTrue(thrown);
		
		try { //Multiple periods.
			c.setLineNumber("3..1");
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Extra period.");			
			thrown = true;
		}
		assertTrue(thrown);
		
		try {//Extra ending period.
			c.setLineNumber("3.1.");
		} catch (IllegalLineException e) {
			assertEquals(e.getMessage(),"Extra period.");			
			thrown = true;
		}
		assertTrue(thrown);
	}


	public void testendSubproof() {
		boolean thrown=false;
		LineNumber a = new LineNumber();
		try {
			a.setLineNumber("3.2.1");
		} catch (IllegalLineException e1) {
			thrown = true;
		}
		assertFalse(thrown);
		a.endSubproof();
		assertEquals("3.3", a.toString());
		a.endSubproof();
		assertEquals("4", a.toString());		
	}
	
	public void testStartSubproof() {
		LineNumber a = new LineNumber();
		a.startSubproof();
		assertEquals("2", a.toString());
		a.startSubproof();
		assertEquals("2.1", a.toString());
		boolean thrown=false;
		try {
			a.setLineNumber("3.2.1");
		} catch (IllegalLineException e1) {
			thrown = true;
		}
		assertFalse(thrown);
		a.startSubproof();
		assertEquals("3.2.1.1", a.toString());
		}
	
	public void testNextLine() {
		boolean thrown=false;
		LineNumber a = new LineNumber();
		try {
			a.setLineNumber("2.2");
		} catch (IllegalLineException e1) {
			thrown = true;
		}
		assertFalse(thrown);
		a.nextLine();
		assertEquals("2.3", a.toString());
		try {
			a.setLineNumber("12.3.9");
		} catch (IllegalLineException e1) {
			thrown = true;
		}
		assertFalse(thrown);
		a.nextLine();
		assertEquals("12.3.10", a.toString());
		
	}
	
	public void testAccessible() {
		boolean thrown = false;
		LineNumber b = new LineNumber();
		LineNumber c = new LineNumber();
		try {
			b.setLineNumber("3.2.4");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertFalse(thrown);		
		
		//Accessible.
		thrown = false;  
		try {
			c.setLineNumber("2");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertFalse(thrown);
		try { //Accessing 2.
			b.isAccessibleLine(c);
		} catch (IllegalInferenceException e) {
			thrown = true;
		}
		assertFalse(thrown);
		
		try {
			c.setLineNumber("3.1");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertFalse(thrown);
		try { //Accessing 3.1.
			b.isAccessibleLine(c);
		} catch (IllegalInferenceException e) {
			thrown = true;
		}
		assertFalse(thrown);
	
		try {
			c.setLineNumber("3.2.2");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertFalse(thrown);
		try { //Accessing 3.2.2.
			b.isAccessibleLine(c);
		} catch (IllegalInferenceException e) {
			thrown = true;
		}
		assertFalse(thrown);
	
		//Not Accessible.
		try { //Last digit is greater.
			c.setLineNumber("3.2.10");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertFalse(thrown);
		try { //Accessing 3.2.10.
			b.isAccessibleLine(c);
		} catch (IllegalInferenceException e) {
			thrown = true;
			assertEquals(e.getMessage(), "Inaccessible line.");
		}
		assertTrue(thrown);
		
		thrown=false;
		try { //Different digits.
			c.setLineNumber("2.1.8");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertFalse(thrown);
		try { //Accessing 2.1.8.
			b.isAccessibleLine(c);
		} catch (IllegalInferenceException e) {
			thrown = true;
			assertEquals(e.getMessage(), "Inaccessible line.");
		}
		assertTrue(thrown);
		
		thrown=false;
		try { //Longer line length.
			c.setLineNumber("3.2.4.5");
		} catch (IllegalLineException e) {
			thrown = true;
		}
		assertFalse(thrown);
		try { //Accessing 3.2.4.5.
			b.isAccessibleLine(c);
		} catch (IllegalInferenceException e) {
			thrown = true;
			assertEquals(e.getMessage(), "Inaccessible line.");
		}
		assertTrue(thrown);
	}
}
