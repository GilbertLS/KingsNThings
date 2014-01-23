package Tests;

import static org.junit.Assert.*;

import org.junit.*;

import Game.Networking.Event;

public class EventTests {
	
	Event testEvent;
	
	@Before
	public void Setup(){
		int eventId = 3;
		String[] params = { "true", "false", "gah" };
		int[] intendedPlayers = { 1, 2, 3 };
		
		testEvent = new Event(
			eventId,
			params,
			intendedPlayers
		);
	}
	
	@Test
	public void StringifyEvent() {
		String actualString = testEvent.toString();
		
		assertEquals(
			"3|true,false,gah,|1,2,3,|",
			actualString
		);
	}
	
	@Test
	public void UnstringifyEvent() {		
		Event actualEvent = Event.Destringify(testEvent.toString()); 
		
		assertEquals(testEvent.eventId, actualEvent.eventId);
		assertArrayEquals(testEvent.eventParams, actualEvent.eventParams);
		assertArrayEquals(testEvent.intendedPlayers, actualEvent.intendedPlayers);
	}
	
	@Test
	public void CreateEventOneParameter(){
		Event event = new Event(3);
		
		Event.Destringify(event.toString());
	}

}
