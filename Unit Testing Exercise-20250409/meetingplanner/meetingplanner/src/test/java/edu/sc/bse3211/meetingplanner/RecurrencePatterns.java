public class RecurrencePatterns {
    public enum RecurrenceType {
        DAILY, WEEKLY, MONTHLY
    }

    private RecurrenceType type;
    private int interval; // Every X days/weeks/months

    public RecurrencePatterns(RecurrenceType type, int interval) {
        this.type = type;
        this.interval = interval;
    }

    public RecurrenceType getType() {
        return type;
    }

    public int getInterval() {
        return interval;
    }
}