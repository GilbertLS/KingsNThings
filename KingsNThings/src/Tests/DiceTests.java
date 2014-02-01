package Tests;

import static org.junit.Assert.*;

import org.junit.*;

import Game.Dice;

public class DiceTests {
	@Test
	public void RollDice(){
		Dice dice = new Dice(1);
		
		int roll = dice.rollDice();
		
		AssertValidRoll(roll);
	}
	
	@Test
	public void RollMultipleDice(){
		Dice dice = new Dice(4);
		
		int[] rolls = dice.rollDice(4);
		
		
		for(int roll : rolls){
			AssertValidRoll(roll);
		}
	}
	
	// This test may fail, but it is very unlikely
	@Test
	public void RollLotsOfTimes(){
		Dice dice = new Dice(1);
		
		boolean[] listOfRolls = new boolean[6];
		
		for (int i = 0; i < 10000; i++){
			int roll = dice.rollDice();
			AssertValidRoll(roll);
			listOfRolls[roll-1] = true;
		}
		
		for (int i = 0; i < 6; i++){
			assertEquals( true, listOfRolls[i] );
		}
		
	}
	
	private void AssertValidRoll(int roll){
		boolean validRoll = (roll >= 1 && roll <= 6);
		
		assertEquals(
			true,
			validRoll
		);
	}
}
