import org.telegram.abilitybots.api.db.DBContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

// TODO: Реализовать список студентов

public class Student implements Serializable {
    private static final long serialVersionUID = 4L;

    private String name;                    // Имя студента
    private Integer idGroup;                // id группы
    private ArrayList<Integer> idCourses;    // Список с id курсов

}
