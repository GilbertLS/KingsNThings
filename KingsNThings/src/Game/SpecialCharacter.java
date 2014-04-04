package Game;

import Game.GameConstants.CurrentPhase;
import Game.GameConstants.ThingType;
import Game.Networking.Event;
import Game.Networking.EventList;
import Game.Networking.GameClient;
import Game.Networking.GameControllerEventHandler;
import Game.Networking.Response;

/*
 * This class extends the Combatant class to add specific Special Character functionality
 */
public class SpecialCharacter extends Combatant implements IIncomable, ISpecialPower{

	public SpecialCharacter(String name, int combatValue, String frontFileName)
	{
		super(ThingType.SPECIAL_CHARACTER, name, combatValue, frontFileName);
		
		isFlipped = false;
	}
	
	public SpecialCharacter IsFlying(boolean isFlying){
		super.isFlying = isFlying;
		return this;
	}
	
	public SpecialCharacter IsMagic(boolean isMagic){
		super.isMagic = isMagic;
		return this;
	}
	
	public SpecialCharacter IsRange(boolean isRange){
		super.isRange = isRange;
		return this;
	}
	
	public SpecialCharacter IsCharge(boolean isCharge){
		super.isCharge = isCharge;
		return this;
	}
	
	public int getIncome()
	{
		return 1;
	}

	@Override
	public void performSpecialPower() {
		if(name.equals("Master Theif"));
			performMasterTheifPower();
	}

	private void performMasterTheifPower() {
		//designate other player
		int targetIndex = Integer.parseInt(GameClient.game.gameView.performPhase(CurrentPhase.SELECT_TARGET_PLAYER));
		Player targetPlayer = GameClient.game.gameModel.playerFromIndex(targetIndex);
		
		//rolls two dice for each player
		int[] rolls = Dice.rollDice(2);
		int targetRoll = rolls[0] + rolls[1];
		
		rolls = Dice.rollDice(2);
		int userRoll = rolls[0] + rolls[1];
		
		if(targetRoll == userRoll){	//roll again
			rolls = Dice.rollDice(2);
			targetRoll = rolls[0] + rolls[1];
			
			rolls = Dice.rollDice(2);
			userRoll = rolls[0] + rolls[1];
			
			if(userRoll < targetRoll){
				//send eliminate theif event to all players
			}
		}
		else if (userRoll > targetRoll){	//theif was successful
			String action = GameClient.game.gameView.performPhase(CurrentPhase.CHOOSE_THEIF_ACTION);
			
			if(action.equals("gold")){
				//send steal gold update event to all players
			}
			else if(action.equals("recruit")){
				//send steal recruit event to all players
			}
		}
	}
	
	//need method for special ability
}
