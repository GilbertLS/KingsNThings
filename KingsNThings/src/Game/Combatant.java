package Game;

/*
 * This class adds functionality to the Thing class to facilitate combat.
 */
public class Combatant extends Thing{
	//The damage dealt by this combatant
	private int combatValue;	
	
	//Booleans to determine any Combatant variations
	private boolean isFlying, isMagic, canCharge, hasSpecial;
}
