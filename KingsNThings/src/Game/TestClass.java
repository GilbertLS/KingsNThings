package Game;

public class TestClass {
	
	private int m_x;
	
	public int GetX(){
		return m_x;
	}
	
	public TestClass(
		int x	
	){
		m_x = x;
	}
	
	public void AddFiveToX(){
		m_x += 5;
	}
}

