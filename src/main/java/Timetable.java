public enum Timetable {
    MONDAY("ПОНЕДЕЛЬНИК\n" +
            "_________________\n" +
            "2 пара 10:45-12:15\n Комп графика(л)\n 205C-3\n" +
            "_________________\n" +
            "3 пара 12:30-14:00\n Комп графика(пр)\n 205C-2\n\n"),
    TUESDAY("ВТОРНИК\n" +
            "_________________\n" +
            "2 пара 10:45-12:15\n Комп графика(л)\n 205C-3\n" +
            "_________________\n" +
            "3 пара 12:30-14:00\n Комп графика(пр)\n 205C-2\n\n"),
    WEDNESDAY("СРЕДА\n" +
            "_________________\n" +
            "2 пара 10:45-12:15\n Комп графика(л)\n 205C-3\n" +
            "_________________\n" +
            "3 пара 12:30-14:00\n Комп графика(пр)\n 205C-2\n\n"),
    THURSDAY("ЧЕТВЕРГ\n" +
            "_________________\n" +
            "2 пара 10:45-12:15\n Комп графика(л)\n 205C-3\n" +
            "_________________\n" +
            "3 пара 12:30-14:00\n Комп графика(пр)\n 205C-2\n\n"),
    FRIDAY("ПЯТНИЦА\n" +
            "_________________\n" +
            "2 пара 10:45-12:15\n Комп графика(л)\n 205C-3\n" +
            "_________________\n" +
            "3 пара 12:30-14:00\n Комп графика(пр)\n 205C-2\n\n");

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
