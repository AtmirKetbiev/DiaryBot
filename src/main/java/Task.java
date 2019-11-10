import java.util.Date;

class Task {
    private String nameTask;
    private String description;
    private Double assessment;
    private Date softDeadline;
    private Date hardDeadline;

    Task (String nameTask, String description, Double assessment, Date softDeadline, Date hardDeadline) {
        this.nameTask = nameTask;
        this.description = description;
        this.assessment = assessment;
        this.softDeadline = softDeadline;
        this.hardDeadline = hardDeadline;
    }

}
