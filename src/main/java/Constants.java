import java.util.Calendar;

public interface  Constants {

    enum answerKeyboard {
        SELECT("Select");

        String select;
        answerKeyboard(String select) {
            this.select = select;
        }
        @Override
        public  String toString(){
            return select;
        }
    }

    //initialization
    String BOT_USERNAME = "YourDiaryBot";
    String TOKEN = System.getenv("BOT_TOKEN");

    //reserved buttons
    String[] keyboardWords = new String[]{
            "/start",
            "Teacher",
            "add course",
            "view course",
            "edit course",
            "delete course",
            "add task",
            "view tasks",
            "edit task",
            "delete task",
            "view group",
            "Student",
            "repositories",
            "timetable",
            "today",
            "Tomorrow",
            "week",
            "add repositories",
            "course",
            "sign up",
            "my course",
            "mark",
            "unmark",
            "Today",
            "For a week",
            "Today",
            "back"
    };

    String[] courses = new String[]{
            "course1",
            "course2",
            "course3",
            "course4",
            "course5",
            "course6",
            "course7",
            "course8",
    };
    String[] tasks = new String[]{
            "task1",
            "task2",
            "task3",
            "task4",
            "task5",
            "task6",
            "task7",
            "task8",
    };
    String[] group = new String[]{
            "group1",
            "group2",
            "group3",
            "group4",
            "group5",
            "group6",
            "group7",
            "group8",
    };
    String[] student = new String[]{
            "student1",
            "student2",
            "student3",
            "student4",
            "student5",
            "student6",
            "student7",
            "student8",
    };
    String[] repositories = new String[]{
            "repositories1",
            "repositories2",
            "repositories3",
            "repositories4",
    };

    //calendar
    Calendar c = Calendar.getInstance();
    int day = c.get(Calendar.DAY_OF_WEEK);
}
