import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PlannerImplementation implements PlannerInterface {

    @Override
    public boolean checkEmployeeAvailability(Person person, LocalDateTime start, LocalDateTime end) {
        return person.isAvailable(start, end);
    }

    @Override
    public boolean scheduleMeeting(Person person, Meeting meeting) {
        return person.addMeeting(meeting);
    }

    @Override
    public List<Meeting> viewDaySchedule(Person person, LocalDate date) {
        return person.getDaySchedule(date);
    }

    @Override
    public boolean scheduleVacation(Person person, LocalDate startDate, LocalDate endDate) {
        return person.addVacation(startDate, endDate);
    }

    @Override
    public List<LocalDateTime> findOptimalMeetingTimes(Person person, LocalDateTime startRange, LocalDateTime endRange,
            int durationMinutes) {
        return person.getCalendar().findOptimalTimeSlots(startRange, endRange, durationMinutes);
    }
}