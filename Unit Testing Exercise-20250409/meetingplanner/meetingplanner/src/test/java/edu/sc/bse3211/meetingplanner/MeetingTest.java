package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.Test;

public class MeetingTest {
	
	// Normal Execution Scenarios
	
	@Test
	public void testCreateValidMeeting() {
		// Test creating a meeting with valid parameters
		Meeting meeting = new Meeting(4, 10, 14, 16);
		assertEquals("Month should be 4", 4, meeting.getMonth());
		assertEquals("Day should be 10", 10, meeting.getDay());
		assertEquals("Start time should be 14", 14, meeting.getStartTime());
		assertEquals("End time should be 16", 16, meeting.getEndTime());
	}

	@Test
	public void testCreateMeetingWithAttendees() {
		// Test creating a meeting with attendees
		Person alice = new Person("Alice");
		Person bob = new Person("Bob");
		ArrayList<Person> attendees = new ArrayList<>();
		attendees.add(alice);
		attendees.add(bob);
		
		Meeting meeting = new Meeting(4, 10, 14, 16, attendees, null, "Team Meeting");
		assertEquals("Number of attendees should be 2", 2, meeting.getAttendees().size());
		assertTrue("Meeting should contain Alice", meeting.getAttendees().contains(alice));
		assertTrue("Meeting should contain Bob", meeting.getAttendees().contains(bob));
	}

	@Test
	public void testCreateMeetingWithRoom() {
		// Test creating a meeting with a room
		Room room = new Room("Room101");
		Meeting meeting = new Meeting(4, 10, 14, 16, new ArrayList<>(), room, "Team Meeting");
		assertEquals("Room ID should be Room101", "Room101", meeting.getRoom().getID());
	}

	@Test
	public void testModifyMeetingTime() {
		// Test modifying meeting time
		Meeting meeting = new Meeting(4, 10, 14, 16);
		meeting.setStartTime(15);
		meeting.setEndTime(17);
		assertEquals("Start time should be 15", 15, meeting.getStartTime());
		assertEquals("End time should be 17", 17, meeting.getEndTime());
	}

	@Test
	public void testModifyMeetingRoom() {
		// Test modifying meeting room
		Room room1 = new Room("Room101");
		Room room2 = new Room("Room102");
		Meeting meeting = new Meeting(4, 10, 14, 16, new ArrayList<>(), room1, "Team Meeting");
		meeting.setRoom(room2);
		assertEquals("Room should be updated to Room102", "Room102", meeting.getRoom().getID());
	}

    @Test
	public void testAddRemoveAttendees() {
		System.out.println("Running testAddRemoveAttendees...");
		Person alice = new Person("Alice");
		Person bob = new Person("Bob");
		ArrayList<Person> attendees = new ArrayList<>();
		attendees.add(alice);

		Room room = new Room("MainRoom");
		Meeting meeting = new Meeting(7, 10, 9, 10, attendees, room, "Team Briefing");

		meeting.addAttendee(bob);
		assertTrue(meeting.getAttendees().contains(bob));

		meeting.removeAttendee(alice);
		assertFalse(meeting.getAttendees().contains(alice));
	}

	// Erroneous Input Scenarios
	
	@Test
    public void testInvalidDayThrowsException() {
    try {
        Calendar.checkTimes(5, 35, 12, 15);
        fail("Expected TimeConflictException to be thrown");
    } catch (TimeConflictException e) {
        assertTrue(e.getMessage().contains("Day does not exist."));
    }
}


@Test(expected = TimeConflictException.class)
public void testInvalidTime() throws TimeConflictException {
    // Test creating a meeting with invalid time (negative start time)
    Calendar.checkTimes(4, 10, -1, 16);  // Start time is negative (-1)
}


	@Test
public void testEndTimeBeforeStartTime() {
    try {
        // Test creating a meeting with end time before start time
        Calendar.checkTimes(4, 10, 16, 14); // End time (14) before start time (16)
        fail("Expected TimeConflictException to be thrown");
    } catch (TimeConflictException e) {
        // Verify the exception message contains the expected text
        assertTrue(e.getMessage().contains("Meeting starts before it ends."));
    }
}


	@Test(expected = TimeConflictException.class)
	public void testRoomAvailabilityConflict() throws TimeConflictException {
		// Setup organization and rooms
		Organization org = new Organization();
		Room room1 = org.getRooms().get(0); // Get the first room
	
		// Schedule a meeting in Room1 from 10 AM to 12 PM
		Meeting meeting1 = new Meeting(4, 10, 10, 12, new ArrayList<>(), room1, "Team Meeting");
		room1.addMeeting(meeting1); // Adding meeting to room1
	
		// Attempt to schedule a conflicting meeting in Room1 from 11 AM to 1 PM
		Meeting meeting2 = new Meeting(4, 10, 11, 13, new ArrayList<>(), room1, "Another Team Meeting");
	
		// Expecting a TimeConflictException due to the overlap in Room1's schedule
		room1.addMeeting(meeting2); // This will throw TimeConflictException
	}
	
	
	@Test
public void testInvalidMonthThrowsException() {
    try {
        // Call Calendar.checkTimes with an invalid month (13)
        Calendar.checkTimes(13, 10, 14, 16); // January (1) is valid, 13 is an invalid month
        fail("Expected TimeConflictException to be thrown");
    } catch (TimeConflictException e) {
        // If the exception is thrown, check if the message contains the expected text
        assertTrue(e.getMessage().contains("Month does not exist."));
    }
}


@Test
public void testInvalidTimeRangeThrowsException() {
    try {
        // Test creating a meeting with an invalid hour (24)
        Calendar.checkTimes(4, 10, 14, 24); // Hour 24 is invalid, should throw exception
        fail("Expected TimeConflictException to be thrown");
    } catch (TimeConflictException e) {
        // If the exception is thrown, check if the message contains the expected text
        assertTrue(e.getMessage().contains("Illegal hour."));
    }
}

}
