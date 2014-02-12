package Game;

public class Utility {
	public static int[] CastToIntArray(String[] strings){
		int[] ints = new int[strings.length];
		
		for (int i = 0; i < strings.length; i++){
			try {
				ints[i] = Integer.parseInt(strings[i]);
			} catch (Exception e){
				ints[i] = -1;
			}
		}
		
		return ints;
	}
}
