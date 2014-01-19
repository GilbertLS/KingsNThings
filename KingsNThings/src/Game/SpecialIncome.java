package Game;

import Game.GameConstants.SpecialType;
import Game.GameConstants.Terrain;
import Game.GameConstants.ThingType;

/*
 * This Class represents a random special income "Thing" in the Game,
 * providing specialized special income functionality
 * Implements the Incomable interface to allow generation of income
 * Terrain is an enum defined in GlobalConstants class
 */
public class SpecialIncome extends Thing implements IIncomable {
	private int income;			//amount of income generated by this SpecialIncome
	private Terrain terrain;	//terrain required to play this SpecialIncome
	
	private static int specialIncomeID = 0;
	public SpecialIncome(Terrain terrain)
	{
		super(ThingType.SPECIAL_INCOME, "specialIncome" + specialIncomeID);
		
		this.income = (int)Math.ceil(Math.random()*6);
		this.terrain = terrain;
	}
}
