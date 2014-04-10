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
	
	public SpecialCharacter(ThingType thingType, String name, int combatValue, String frontFileName)
	{
		super(ThingType.TERRAIN_LORD, name, combatValue, frontFileName);
		
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
	public String performSpecialPower() {
		if(name.equals("Master Thief"))
			return performMasterthiefPower();
		else return "";
	}

	private String performMasterthiefPower() {
		//add special character
		String ret = "thief~";
		
		//add current player index
		ret += ""+GameClient.game.gameModel.getCurrPlayerNumber() + "~";
		
		//designate other player
		GameClient.game.sendMessageToView("Please choose a target player for Master Thief");
		ret += GameClient.game.gameView.performPhase(CurrentPhase.SELECT_TARGET_PLAYER) +"~";
		
		//rolls two dice for each player
		int[] rolls = Dice.rollDice(2);
		int targetRoll = rolls[0] + rolls[1];
		
		rolls = Dice.rollDice(2);
		int userRoll = rolls[0] + rolls[1];
		
		if(userRoll == targetRoll){	//roll again
			rolls = Dice.rollDice(2);
			targetRoll = rolls[0] + rolls[1];
			
			rolls = Dice.rollDice(2);
			userRoll = rolls[0] + rolls[1];
			
			if(userRoll < targetRoll){
				GameClient.game.gameView.performUserFeedback("Master Thief was eliminated");
				ret += "eliminate~";
				return ret += thingID + "~";
			}
		}
		else if (userRoll > targetRoll){	//thief was successful
			GameClient.game.sendMessageToView("Thief was successful, Please choose to steal gold or a recruit");
			String action = GameClient.game.gameView.performPhase(CurrentPhase.CHOOSE_THIEF_ACTION);
			
			if(action.equals("gold")){
				return ret += "gold~";
			}
			else if(action.equals("recruit")){
				return ret += "recruit~";
			}
		}
			
		return "";
	}
	
	public SpecialCharacter copy() {
		SpecialCharacter copy = new SpecialCharacter(name, combatValue, frontFileName);
		copy.Flying(isFlying);
		copy.Ranged(isRange);
		copy.Magic(isMagic);
		copy.Charge(isCharge);
		
		return copy;
	}
}
