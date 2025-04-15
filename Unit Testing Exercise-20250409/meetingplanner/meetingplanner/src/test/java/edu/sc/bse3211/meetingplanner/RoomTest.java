package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;  

public class RoomTest {

    // Constructor and ID Tests
    @Test
    public void testDefaultConstructor_IDIsEmpty() {
        Room room = new Room();
        assertEquals("Default constructor should initialize ID as empty", "", room.getID());
    }

    @Test
    public void testParameterizedConstructor_IDSetCorrectly() {
        Room room = new Room("LLT6A");
        assertEquals("Constructor should set room ID correctly", "LLT6A", room.getID());
    }

    // Meeting Management Tests
    @Test
    public void testAddMeeting_Success() throws TimeConflictException {
        Room room = new Room("LLT6A");
        Meeting meeting = new Meeting(3, 10, 9, 10, new ArrayList<>(), room, "Standup");
        room.addMeeting(meeting);
        assertTrue("Meeting should exist in the room", room.isBusy(3, 10, 9, 10));
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_ConflictThrowsException() throws TimeConflictException {
        Room room = new Room("LLT6A");
        Meeting m1 = new Meeting(3, 10, 9, 11, new ArrayList<>(), room, "Meeting 1");
        Meeting m2 = new Meeting(3, 10, 10, 12, new ArrayList<>(), room, "Meeting 2");
        room.addMeeting(m1);
        room.addMeeting(m2); // Should throw
    }

    @Test
    public void testAddMeeting_ExceptionMessageIncludesRoomID() {
        Room room = new Room("LLT6A");
        Meeting m1 = new Meeting(3, 10, 9, 11, new ArrayList<>(), room, "Meeting 1");
        Meeting m2 = new Meeting(3, 10, 9, 11, new ArrayList<>(), room, "Meeting 2");
        try {
            room.addMeeting(m1);
            room.addMeeting(m2);
            fail("Expected TimeConflictException not thrown");
        } catch (TimeConflictException e) {
            assertTrue("Exception message should include room ID", e.getMessage().contains("LLT6A"));
        }
    }

    // Agenda Printing Tests
    @Test
    public void testPrintAgendaMonth_WithMeeting() throws TimeConflictException {
        Room room = new Room("LLT6A");
        Meeting meeting = new Meeting(3, 10, 9, 10, new ArrayList<>(), room, "Standup");
        room.addMeeting(meeting);
        String agenda = room.printAgenda(3);
        assertTrue("Agenda should include meeting description", agenda.contains("Standup"));
        assertTrue("Agenda should include room ID", agenda.contains("LLT6A"));
    }

    @Test
    public void testPrintAgendaDay_WithMeeting() throws TimeConflictException {
        Room room = new Room("LLT6A");
        Meeting meeting = new Meeting(3, 10, 9, 10, new ArrayList<>(), room, "Standup");
        room.addMeeting(meeting);
        String agenda = room.printAgenda(3, 10);
        assertTrue("Agenda should include meeting details", agenda.contains("3/10"));
        assertTrue("Agenda should include room ID", agenda.contains("LLT6A"));
    }

    @Test
    public void testPrintAgendaDay_NoMeetings() {
        Room room = new Room("LLT6A");
        String agenda = room.printAgenda(3, 10);
        assertEquals("Agenda for empty day should be formatted correctly", 
                     "Agenda for 3/10:\n", agenda);
    }

    // Meeting Retrieval and Removal Tests
    @Test
    public void testGetMeeting_ValidIndex() throws TimeConflictException {
        Room room = new Room("LLT6A");
        Meeting meeting = new Meeting(3, 10, 9, 10, new ArrayList<>(), room, "Standup");
        room.addMeeting(meeting);
        Meeting retrieved = room.getMeeting(3, 10, 0);
        assertEquals("Retrieved meeting should match added meeting", meeting, retrieved);
    }

    @Test
    public void testRemoveMeeting_Success() throws TimeConflictException {
        Room room = new Room("LLT6A");
        Meeting meeting = new Meeting(3, 10, 9, 10, new ArrayList<>(), room, "Standup");
        room.addMeeting(meeting);
        room.removeMeeting(3, 10, 0);
        assertFalse("Meeting should be removed", room.isBusy(3, 10, 9, 10));
    }

    @Test
    public void testRemoveMeeting_DoesNotAffectOtherMeetings() throws TimeConflictException {
        Room room = new Room("LLT6A");
        Meeting m1 = new Meeting(3, 10, 9, 10, new ArrayList<>(), room, "Meeting 1");
        Meeting m2 = new Meeting(3, 10, 14, 16, new ArrayList<>(), room, "Meeting 2");
        room.addMeeting(m1);
        room.addMeeting(m2);
        room.removeMeeting(3, 10, 0); // Remove m1
        assertTrue("Second meeting should still exist", room.isBusy(3, 10, 14, 16));
    }

    // Edge Cases
    @Test(expected = TimeConflictException.class)
    public void testIsBusy_InvalidDateFebruary30() throws TimeConflictException {
        Room room = new Room("LLT6A");
        room.isBusy(2, 30, 9, 10); // February 30 is invalid
    }

    @Test
    public void testIsBusy_NonExistentDayApril31() {
        Room room = new Room("LLT6A");
        try {
            // April 31 is marked as busy in Calendar's initialization
            assertTrue("Non-existent day should be busy", room.isBusy(4, 31, 9, 10));
        } catch (TimeConflictException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }


    @Test
    public void testPrintAgendaDay_MultipleMeetings() throws TimeConflictException {
        Room room = new Room("LLT6A");
        Meeting m1 = new Meeting(3, 10, 9, 10, new ArrayList<>(), room, "Standup");
        Meeting m2 = new Meeting(3, 10, 14, 15, new ArrayList<>(), room, "Retrospective");
        room.addMeeting(m1);
        room.addMeeting(m2);
        String agenda = room.printAgenda(3, 10);
        assertTrue("Agenda should include first meeting", agenda.contains("Standup"));
        assertTrue("Agenda should include second meeting", agenda.contains("Retrospective"));
    }

   
    @Test(expected = TimeConflictException.class)
    public void testPrintAgenda_InvalidMonth() throws TimeConflictException {
        Room room = new Room("LLT6A");
        room.printAgenda(13); // Month 13 is invalid
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveMeeting_FromEmptyRoom() throws TimeConflictException {
        Room room = new Room("LLT6A");
        room.removeMeeting(3, 10, 0); // Should throw as no meetings exist
    }

 

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetMeeting_InvalidIndex() throws TimeConflictException {
        Room room = new Room("LLT6A");
        room.getMeeting(3, 10, 0); // Should throw as no meetings exist
    }

  
   
}