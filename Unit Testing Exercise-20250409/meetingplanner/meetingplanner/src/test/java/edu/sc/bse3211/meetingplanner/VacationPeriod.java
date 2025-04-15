import java.time.LocalDate;

public class VacationPeriod {
    private LocalDate startDate;
    private LocalDate endDate;

    public VacationPeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean includes(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
                (date.isEqual(endDate) || date.isBefore(endDate));
    }
}
