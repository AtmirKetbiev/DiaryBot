import org.telegram.abilitybots.api.db.DBContext;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable {
    private static final long serialVersionUID = 3L;

    private int idCourse;
    private String name;
    private String description;
    private Date soft;
    private Date hard;
    private String comment;
    private String mark;

    public Task() {
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSoft() {
        return soft;
    }

    public void setSoft(String soft) throws ParseException {
        this.soft = new SimpleDateFormat("dd/MM/yyyy").parse(soft);
    }

    public Date getHard() {
        return hard;
    }

    public void setHard(String hard) throws ParseException {
        this.hard = new SimpleDateFormat("dd/MM/yyyy").parse(hard);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public boolean check() {
        if (name == null)  return false;
        if (description == null)  return false;
        if (soft == null)  return false;
        if (hard == null)  return false;
        return true;
    }
}
