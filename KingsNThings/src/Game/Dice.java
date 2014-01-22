package Game;

/*
 * This class emulates up to 4 dice
 */
public class Dice {
	//individual die values
	private static int die1Value, die2Value, die3Value, die4Value;
	
	public Dice()
	{
		die1Value = 0;
		die2Value = 0;
		die3Value = 0;
		die4Value = 0;
	}
	
	public void rollTwoDice()
	{
		die1Value = (int)Math.ceil(Math.random()*GameConstants.DIE_1_SIDES);
		die2Value = (int)Math.ceil(Math.random()*GameConstants.DIE_2_SIDES);
	}
	
	public int getDie1Value(){return die1Value;}
	public int getDie2Value(){return die2Value;}
	public int getDie3Value(){return die3Value;}
	public int getDie4Value(){return die4Value;}
}
