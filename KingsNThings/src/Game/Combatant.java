package Game;

import gui.GameView;
import Game.GameConstants.BattleTurn;
import Game.GameConstants.ThingType;

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
	
	public Combatant Flying(boolean isFlying){
		this.isFlying = isFlying;
		return this;
	}
	
	public Combatant Magic(boolean isMagic){
		this.isMagic = isMagic;
		return this;
	}
	
	public Combatant Ranged(boolean isRange){
		this.isRange = isRange;
		return this;
	}
	
	public Combatant Charge(boolean isCharge){
		this.isCharge = isCharge;
		return this;
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
	
	public int GetCombatRoll(
			BattleTurn turn,
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
				int roll = GameView.battleView.RollDice(this, 0, numPreviousRolls);
				
				if (roll <= this.GetCombatValue() ){
					rolls++;
				}
			}
			
		return rolls;	
	}
	
	public int GetCombatRoll(
		BattleTurn turn
	) {
		int rolls = 0;
		if (!(
				( this.IsMagic() && turn == BattleTurn.MAGIC ) ||
				( this.IsRange() && turn == BattleTurn.RANGED ) ||
				( !this.IsMagic() && !this.IsRange() && !( turn == BattleTurn.MAGIC) && !( turn == BattleTurn.RANGED )))){
				return 0;
		}
			
		int numRolls = isCharge ? 2 : 1;
		for (int i = 0; i < numRolls; i++){
			int roll = (int)Math.ceil(Math.random()*GameConstants.DIE_1_SIDES);
			
			if (roll <= this.GetCombatValue() ){
				rolls++;
			}
		}
			
		return rolls;
	}
}
