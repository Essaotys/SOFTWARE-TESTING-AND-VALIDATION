import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class SchedulingSystemTest {

    public static void main(String[] args) {
        PlannerInterface planner = new PlannerImplementation();

        // Create a test person
        Person john = new Person("John Doe");

        // Test 1: Person has no meetings scheduled — should return available
        LocalDateTime tomorrow10AM = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0));
        LocalDateTime tomorrow11AM = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(11, 0));

        System.out.println("Test 1: Person with no meetings should be available");
        boolean isAvailable = planner.checkEmployeeAvailability(john, tomorrow10AM, tomorrow11AM);
        System.out.println("Is John available tomorrow 10-11 AM? " + isAvailable);
        System.out.println("Expected: true, Actual: " + isAvailable);
        System.out.println();

        // Test 2: Handling scheduling conflicts
        System.out.println("Test 2: Handling scheduling conflicts");
        Meeting meeting1 = new Meeting("Project Review", tomorrow10AM, tomorrow11AM);
        boolean scheduled1 = planner.scheduleMeeting(john, meeting1);
        System.out.println("First meeting scheduled: " + scheduled1);

        Meeting meeting2 = new Meeting("Overlapping Meeting", tomorrow10AM.plusMinutes(30),
                tomorrow11AM.plusMinutes(30));
        boolean scheduled2 = planner.scheduleMeeting(john, meeting2);
        System.out.println("Second overlapping meeting scheduled: " + scheduled2);
        System.out.println("Expected: false, Actual: " + scheduled2);
        System.out.println();

        // Test 3: Viewing full day schedule
        System.out.println("Test 3: Viewing full day schedule");
        List<Meeting> daySchedule = planner.viewDaySchedule(john, tomorrow10AM.toLocalDate());
        System.out.println("John's schedule for tomorrow:");
        for (Meeting meeting : daySchedule) {
            System.out.println("- " + meeting);
        }
        System.out.println("Expected: 1 meeting, Actual: " + daySchedule.size());
        System.out.println();

        // Test 4: Checking Availability for Recurring Meetings
        System.out.println("Test 4: Checking Availability for Recurring Meetings");

        // Find next Monday
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDateTime mondayNineAM = LocalDateTime.of(nextMonday, LocalTime.of(9, 0));
        LocalDateTime mondayTenAM = LocalDateTime.of(nextMonday, LocalTime.of(10, 0));

        // Create a weekly recurring meeting on Mondays
        RecurrencePatterns weeklyPattern = new RecurrencePatterns(RecurrencePatterns.RecurrenceType.WEEKLY, 1);
        Meeting recurringMeeting = new Meeting("Weekly Team Meeting", mondayNineAM, mondayTenAM, weeklyPattern);

        boolean recurringScheduled = planner.scheduleMeeting(john, recurringMeeting);
        System.out.println("Recurring meeting scheduled: " + recurringScheduled);

        // Check availability for the next Monday
        LocalDate followingMonday = nextMonday.plusDays(7);
        LocalDateTime followingMondayNineAM = LocalDateTime.of(followingMonday, LocalTime.of(9, 0));
        LocalDateTime followingMondayTenAM = LocalDateTime.of(followingMonday, LocalTime.of(10, 0));

        boolean availableNextMonday = planner.checkEmployeeAvailability(john, followingMondayNineAM,
                followingMondayTenAM);
        System.out.println("Is John available on the following Monday 9-10 AM? " + availableNextMonday);
        System.out.println("Expected: false, Actual: " + availableNextMonday);
        System.out.println();

        // Test 5: Checking Availability for Multiple Meetings (finding optimal time
        // slots)
        System.out.println("Test 5: Finding optimal meeting times");

        // Define a range for tomorrow
        LocalDateTime startRange = LocalDateTime.of(tomorrow10AM.toLocalDate(), LocalTime.of(8, 0));
        LocalDateTime endRange = LocalDateTime.of(tomorrow10AM.toLocalDate(), LocalTime.of(17, 0));

        List<LocalDateTime> optimalTimes = planner.findOptimalMeetingTimes(john, startRange, endRange, 60);
        System.out.println("Optimal meeting times for tomorrow (60 min duration):");
        for (LocalDateTime time : optimalTimes) {
            System.out.println("- " + time);
        }
        System.out
                .println("Expected: Multiple time slots excluding 10-11 AM, Actual: " + optimalTimes.size() + " slots");
        System.out.println();

        // Test 6: Handling Vacation
        System.out.println("Test 6: Handling Vacation");

        // Schedule vacation from March 10-12
        LocalDate vacationStart = LocalDate.of(LocalDate.now().getYear(), 3, 10);
        LocalDate vacationEnd = LocalDate.of(LocalDate.now().getYear(), 3, 12);

        boolean vacationScheduled = planner.scheduleVacation(john, vacationStart, vacationEnd);
        System.out.println("Vacation scheduled: " + vacationScheduled);

        // Check availability during vacation
        LocalDateTime vacationDateTime = LocalDateTime.of(vacationStart, LocalTime.of(14, 0));
        boolean availableDuringVacation = planner.checkEmployeeAvailability(john, vacationDateTime,
                vacationDateTime.plusHours(1));
        System.out.println("Is John available during vacation? " + availableDuringVacation);
        System.out.println("Expected: false, Actual: " + availableDuringVacation);
    }
}