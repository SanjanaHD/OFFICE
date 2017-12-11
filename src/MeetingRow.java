import java.time.LocalDate;

public class MeetingRow{
    String id;
    LocalDate date;
    String topic;

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTopic() {
        return topic;
    }
}