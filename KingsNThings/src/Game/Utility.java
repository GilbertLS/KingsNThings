package Game;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Utility {
	private static Map<Object, Semaphore> semaphores = new HashMap<Object, Semaphore>();

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
	
	public static void PromptForInput(Semaphore sem) {
		try {
			sem.acquire();
		} catch (InterruptedException e) {}
	}
	
	public static void PromptObjectForInput(Object obj) {
		if (Utility.semaphores.keySet().contains(obj.hashCode())) {
			System.out.println("Already waiting for input");
			return;
		}
		
		Semaphore sem = new Semaphore(0);
		semaphores.put(obj.hashCode(), sem);
		Utility.PromptForInput(sem);
	}
	
	public static void GotInput(Semaphore sem) {
		sem.release();
	}
	
	public static void GotObjectInput(Object obj) {
		if (!Utility.semaphores.keySet().contains(obj.hashCode())) {
			System.out.println("Not waiting for input");
			return;
		}
		
		Semaphore sem = semaphores.get(obj.hashCode());
		Utility.GotInput(sem);
		semaphores.remove(obj, sem);
	}
	
	public static void debugPrint(Object s) {
		System.out.println("DEBUG-------------------|" + s 
				+  "|-------------------------------------");
	}
}
