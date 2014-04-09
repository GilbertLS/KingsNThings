package Game;

import Game.GameConstants.SettlementType;
import Game.GameConstants.ThingType;

/*
 * This class extends the Building class to add specific village/city functionality 
 */
public class Settlement extends Building implements IIncomable{
	private SettlementType settlementType;
	
	public Settlement(String name, int combatValue, String frontFileName, String backFileName, SettlementType settlementType)
	{		
		super(ThingType.SETTLEMENT, name, combatValue, backFileName, frontFileName);
		
		this.settlementType = settlementType;
	}
	
	public int getIncome()
	{
		return combatValue;
	}
	
	public Settlement copy() {
		Settlement settlement = new Settlement(name, combatValue, frontFileName, backFileName, settlementType);
		return settlement;
	}
}
