package Tests;

import static org.junit.Assert.*;
import org.junit.Test;
import Game.TestClass;;

public class TestOfTestClass {

	@Test
	public void AddFiveToInt() {
		int x = 0;
		TestClass testClass = new TestClass(x);
		testClass.AddFiveToX();
		assertEquals(x + 5, testClass.GetX());
	}
	
	@Test
	public void DontAddFiveToInt() {
		int x = 0;
		TestClass testClass = new TestClass(x);
		assertEquals(x, testClass.GetX());
	}

}
