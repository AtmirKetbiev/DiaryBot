public enum Timetable {
    MONDAY("�����������\n" +
            "_________________\n" +
            "2 ���� 10:45-12:15\n ���� �������(�)\n 205C-3\n" +
            "_________________\n" +
            "3 ���� 12:30-14:00\n ���� �������(��)\n 205C-2\n\n"),
    TUESDAY("�������\n" +
            "_________________\n" +
            "2 ���� 10:45-12:15\n ���� �������(�)\n 205C-3\n" +
            "_________________\n" +
            "3 ���� 12:30-14:00\n ���� �������(��)\n 205C-2\n\n"),
    WEDNESDAY("�����\n" +
            "_________________\n" +
            "2 ���� 10:45-12:15\n ���� �������(�)\n 205C-3\n" +
            "_________________\n" +
            "3 ���� 12:30-14:00\n ���� �������(��)\n 205C-2\n\n"),
    THURSDAY("�������\n" +
            "_________________\n" +
            "2 ���� 10:45-12:15\n ���� �������(�)\n 205C-3\n" +
            "_________________\n" +
            "3 ���� 12:30-14:00\n ���� �������(��)\n 205C-2\n\n"),
    FRIDAY("�������\n" +
            "_________________\n" +
            "2 ���� 10:45-12:15\n ���� �������(�)\n 205C-3\n" +
            "_________________\n" +
            "3 ���� 12:30-14:00\n ���� �������(��)\n 205C-2\n\n");

    String timetable;

    Timetable(String timetable) {
        this.timetable = timetable;
    }

    public static String timetable(int day) {
        switch (day) {
            case (2):
                return MONDAY.toString();
            case (3):
                return TUESDAY.toString();
            case (4):
                return WEDNESDAY.toString();
            case (5):
                return THURSDAY.toString();
            case (6):
                return FRIDAY.toString();
            case (1):
            case (7):
                return "today you can relax";
            default:
                return MONDAY.toString() + TUESDAY.toString() +
                        WEDNESDAY.toString() + THURSDAY.toString() +
                        FRIDAY.toString();
        }
    }

    @Override
    public String toString() {
        return timetable;
    }
}
