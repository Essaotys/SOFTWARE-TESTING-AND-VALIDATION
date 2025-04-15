import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PlannerInterface {
    boolean checkEmployeeAvailability(Person person, LocalDateTime start, LocalDateTime end);

    boolean scheduleMeeting(Person person, Meeting meeting);

    List<Meeting> viewDaySchedule(Person person, LocalDate date);

    boolean scheduleVacation(Person person, LocalDate startDate, LocalDate endDate);

    List<LocalDateTime> findOptimalMeetingTimes(Person person, LocalDateTime startRange, LocalDateTime endRange,
            int durationMinutes);
}