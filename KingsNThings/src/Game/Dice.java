package Game;

public class Dice {
	//individual die values
	private static int[] dieValues = new int[4];
	//private List<DiceView> diceViews;
	
	public Dice(int numDice) {
		dieValues = new int[numDice];
		//diceViews = new DiceView[numDice]();
	}
	
	public int rollDice(){
		//die1Value = (int)Math.ceil(Math.random()*GameConstants.DIE_1_SIDES);
		dieValues[0] = (int)Math.ceil(Math.random()*GameConstants.DIE_1_SIDES);
		// diceView[0].Update( dieValues[0] );
		return dieValues[0];
	}
	
	public static int[] rollDice( int numDice ){
		if (numDice > dieValues.length) { 
			numDice = dieValues.length;
		}
		
		int[] diceRolls = new int[numDice];
		
		for (int i = 0; i < numDice; i++) {
			dieValues[i] = (int)Math.ceil(Math.random()*GameConstants.DIE_1_SIDES);
			// diceView.Update( dieValues[i] );
			diceRolls[i] = dieValues[i];
		}
		
		return diceRolls;
	}
	
	public static int getRoll(int i)
	{
		if(dieValues.length < i+1)
			return -1;
		else
			return dieValues[i];
	}
}
