import org.telegram.abilitybots.api.db.DBContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

// TODO: ����������� ������ ���������

public class Student implements Serializable {
    private static final long serialVersionUID = 4L;

    private String name;                    // ��� ��������
    private Integer idGroup;                // id ������
    private ArrayList<Integer> idCourses;    // ������ � id ������

}
