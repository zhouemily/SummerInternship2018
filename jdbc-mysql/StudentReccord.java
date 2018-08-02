
/**
 * Student reccords is a student profile that included their name and letter
 * grade.
 *
 * @author Emily Zhou
 * @version 7/6/18
 */
public class StudentReccord
{
    String name = "";
    String grade = "";
    /**
     * Constructor for objects of class StudentReccord.
     * 
     * @param name the student's name
     * @param grade the student's letter grade
     */
    public StudentReccord(String name, String grade)
    {
        this.name= name;
        this.grade = grade;
    }

    /**
     * Retrives the student's name.
     * 
     * @return the name of the student.
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Retrives the student's letter grade.
     * 
     * @return the letter grade of the student.
     */
    public String getGrade()
    {
        return grade;
    }
}
