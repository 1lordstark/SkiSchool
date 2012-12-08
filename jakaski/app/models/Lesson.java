package models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Lesson extends Model {

    @Required
    public Date startTime;
    @Required
    public Date endTime;
    public Location location;
    public LessonType lessonType;
    public boolean snowboard;
    //TODO this information is not required, this function is covered by attribute paidLessons in Student class
    //public boolean paid;
    public Lang language;
    public String note;
    public Integer studentsCount;

    @ManyToMany
    public Set<Instructor> instructors;

    @ManyToMany
    public Set<Student> students;

    public Lesson(Date startTime, Date endTime, Location location, LessonType lessonType, String note, Instructor instructor,
            Student student) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.lessonType = lessonType;
        this.note = note;
        
        // TODO Solve for multiple records 
        this.instructors = new HashSet<>();
        this.instructors.add(instructor);
        this.students = new HashSet<>();
        this.students.add(student);

        // TODO Will be used in future releases
        this.snowboard = false;
        //this.paid = false;
        //this.language = Lang;
    }

}
