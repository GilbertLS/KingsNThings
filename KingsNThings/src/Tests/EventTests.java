package Tests;

import static org.junit.Assert.*;

import org.junit.*;

import Game.Networking.Event;

public class EventTests {
	
	Event testEvent;
	
	@Before
	public void Setup(){
		int eventId = 3;
		String[] params = { "true", "false", "gah", "woah", "yowdy" };
		boolean[] intendedPlayers = { true, true, true };
		
		testEvent = new Event()
			.EventId(eventId)
			.EventParameters(params)
			.IntendedPlayers(intendedPlayers)
			.ExpectsResponse(true);
	}
	
	@Test
	public void StringifyEvent() {
		String actualString = testEvent.toString();
		
		assertEquals(
			"3|true,false,gah,woah,yowdy,|true,true,true,|true|",
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
	public void UnstringifyEventGetParams() throws Exception {		
		Event actualEvent = Event.Destringify(testEvent.toString()); 
		
		String s = actualEvent.getEventParameters();
		
		assertEquals("true false gah woah yowdy ", s);
	}
	
	@Test
	public void CreateEventOneParameter(){
		Event event = new Event().EventId(0);
		
		Event.Destringify(event.toString());
	}
	
	@Test
	public void DestringifyExpectsResponse(){
		Event event = Event.Destringify(testEvent.ExpectsResponse(true).toString());
		assertEquals(true, event.expectsResponseEvent);
		
		event = Event.Destringify(testEvent.ExpectsResponse(false).toString());
		assertEquals(false, event.expectsResponseEvent);
	}

}
