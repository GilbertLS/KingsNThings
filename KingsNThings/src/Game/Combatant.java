package Game;

import gui.GameView;
import Game.GameConstants.BattleTurn;
import Game.GameConstants.ThingType;
import Game.Networking.GameClient;

/*
 * This class adds functionality to the Thing class to facilitate combat.
 */
public abstract class Combatant extends Thing{
	//The damage dealt by this combatant
	protected int combatValue;	

	protected boolean isFlying = false;
	protected boolean isRange = false;
	protected boolean isCharge = false;
	protected boolean isMagic = false;
	
	public Combatant(ThingType thingType, String name, int combatValue, String frontFileName)
	{
		super(thingType, name, frontFileName);
		
		this.combatValue = combatValue;
	}
	
	public int GetCombatValue(){
		return combatValue;
	}
	
	public boolean IsFlying(){
		return isFlying;
	}
	
	public boolean IsRange(){
		return isRange;
	}
	
	public boolean IsCharge(){
		return isCharge;
	}
	
	public boolean IsMagic(){
		return isMagic;
	}
	
	// remove promptView
	public int GetCombatRoll(
			BattleTurn turn,
			boolean promptView,
			int numPreviousRolls
	){
		int rolls = 0;
		if (!(
				( this.IsMagic() && turn == BattleTurn.MAGIC ) ||
				( this.IsRange() && turn == BattleTurn.RANGED ) ||
				( !this.IsMagic() && !this.IsRange() && !( turn == BattleTurn.MAGIC) && !( turn == BattleTurn.RANGED )))){
				return 0;
			}
			
			int numRolls = isCharge ? 2 : 1;
			for (int i = 0; i < numRolls; i++){
				int roll = GameClient.game.gameModel.rollDice();
				
				if (promptView){
					GameView.battleView.RollDice(this, 0, roll, numPreviousRolls);
				}
				
				if (roll <= this.GetCombatValue() ){
					rolls++;
				}
			}
			
		return rolls;	
	}
}
