package Game;

import Game.GameConstants.SettlementType;
import Game.GameConstants.SpecialType;
import Game.GameConstants.ThingType;

/*
 * This class extends the Building class to add specific village/city functionality 
 */
public class Settlement extends Building {
	private SettlementType settlementType;
	
	//temp
	private static int settlementID = 0;
	public Settlement()
	{
		super(ThingType.SETTLEMENT, "Settlement" + settlementID, SpecialType.RANGED, (int)Math.ceil(Math.random()*6));
		
		this.settlementType = SettlementType.VILLAGE;
	}
}
