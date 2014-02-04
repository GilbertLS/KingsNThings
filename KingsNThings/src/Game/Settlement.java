package Game;

import Game.GameConstants.SettlementType;
import Game.GameConstants.ThingType;

/*
 * This class extends the Building class to add specific village/city functionality 
 */
public class Settlement extends Building {
	private SettlementType settlementType;
	
	//temp
	private static int settlementID = 0;
	public Settlement(String backFileName, String frontFileName)
	{
		super(ThingType.SETTLEMENT, "Settlement" + settlementID++, (int)Math.ceil(Math.random()*6), backFileName, frontFileName);
		
		this.settlementType = SettlementType.VILLAGE;
	}
}
