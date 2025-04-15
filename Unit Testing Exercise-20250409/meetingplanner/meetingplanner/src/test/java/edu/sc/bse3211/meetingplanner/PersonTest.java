package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class PersonTest {
    private Person person;
    private Room room;
    private ArrayList<Person> attendees;

    @Before
    public void setUp() throws Exception {
        person = new Person("Test Employee");
        room = new Room("TestRoom");
        attendees = new ArrayList<>();
        attendees.add(person);
        
        // Initialize calendar if needed
        try {
            java.lang.reflect.Field calendarField = Person.class.getDeclaredField("calendar");
            calendarField.setAccessible(true);
            if (calendarField.get(person) == null) {
                calendarField.set(person, new Calendar());
            }
        } catch (Exception e) {
            fail("Failed to initialize calendar");
        }
    }

    @Test
    public void testAddVacation_Success() throws TimeConflictException {
        Meeting vacation = new Meeting(6, 15, 0, 23, attendees, new Room(), "vacation");
        person.addMeeting(vacation);
        
        assertTrue(person.isBusy(6, 15, 0, 23));
        assertTrue(person.printAgenda(6, 15).contains("vacation"));
    }

    @Test
    public void testMultiDayVacation() throws TimeConflictException {
        Meeting vacation1 = new Meeting(9, 1, 0, 23, attendees, new Room(), "vacation");
        Meeting vacation2 = new Meeting(9, 2, 0, 23, attendees, new Room(), "vacation");
        
        person.addMeeting(vacation1);
        person.addMeeting(vacation2);
        
        assertTrue(person.isBusy(9, 1, 0, 23));
        assertTrue(person.isBusy(9, 2, 0, 23));
        
        String agenda = person.printAgenda(9);
        assertTrue("September 1 should be in agenda", agenda.contains("9/1"));
        assertTrue("September 2 should be in agenda", agenda.contains("9/2"));
    }

    @Test(expected = TimeConflictException.class)
    public void testAddVacation_WithMeetingConflict() throws TimeConflictException {
        Meeting meeting = new Meeting(6, 15, 10, 12, attendees, room, "Test Meeting");
        person.addMeeting(meeting);
        
        Meeting vacation = new Meeting(6, 15, 0, 23, attendees, new Room(), "vacation");
        person.addMeeting(vacation);
    }

    @Test
    public void testAddMeeting_DuringVacation() throws TimeConflictException {
        Meeting vacation = new Meeting(7, 20, 0, 23, attendees, new Room(), "vacation");
        person.addMeeting(vacation);
        
        try {
            Meeting meeting = new Meeting(7, 20, 14, 15, attendees, room, "Test Meeting");
            person.addMeeting(meeting);
            fail("Should have thrown TimeConflictException");
        } catch (TimeConflictException e) {
            assertTrue(e.getMessage().contains("conflict"));
        }
    }

    @Test
    public void testVacationAppearsInAgenda() throws TimeConflictException {
        Meeting vacation = new Meeting(8, 10, 0, 23, attendees, new Room(), "vacation");
        person.addMeeting(vacation);
        
        String agenda = person.printAgenda(8, 10);
        assertTrue("Agenda should show vacation", agenda.contains("vacation"));
        assertTrue("Agenda should show date", agenda.contains("8/10"));
    }

    @Test(expected = TimeConflictException.class)
    public void testVacationOnInvalidDate() throws TimeConflictException {
        // February 30th should be invalid
        Meeting vacation = new Meeting(2, 30, 0, 23, attendees, new Room(), "vacation");
        person.addMeeting(vacation);
    }

    @Test
    public void testVacationSameDayStartEnd() throws TimeConflictException {
        Meeting vacation = new Meeting(10, 5, 0, 23, attendees, new Room(), "vacation");
        person.addMeeting(vacation);
        
        assertTrue("Full day should be busy", person.isBusy(10, 5, 0, 23));
        assertTrue("Partial day should be busy", person.isBusy(10, 5, 14, 15));
    }
}