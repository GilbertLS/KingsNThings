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
		boolean[] intendedPlayers = { true, true, true };
		
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
			"3|true,false,gah,|true,true,true,|",
			actualString
		);
	}
	
	@Test
	public void UnstringifyEvent() throws Exception {		
		Event actualEvent = Event.Destringify(testEvent.toString()); 
		
		assertEquals(testEvent.eventId, actualEvent.eventId);
		assertArrayEquals(testEvent.eventParams, actualEvent.eventParams);
		//assertArrayEquals(testEvent.intendedPlayers, actualEvent.intendedPlayers);
		
		for (int i = 0; i < testEvent.intendedPlayers.length; i++){
			assertEquals(testEvent.intendedPlayers[i], actualEvent.intendedPlayers[i]);
		}
	}
	
	@Test
	public void CreateEventOneParameter(){
		Event event = new Event(3);
		
		Event.Destringify(event.toString());
	}

}
