import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Represents a University Course Management System for handling courses, students, and professors.
 */
public class UniversityCourseManagementSystem {

    /**
     * Loads initial data into the system with predefined instances of courses, students, and professors.
     */

    public static void fillInitialData() {
        // loading instances
        Course javaBeginner = new Course("java_beginner", CourseLevel.BACHELOR);
        Course javaIntermediate = new Course("java_intermediate", CourseLevel.BACHELOR);
        Course pythonBasics = new Course("python_basics", CourseLevel.BACHELOR);
        Course algorithms = new Course("algorithms", CourseLevel.MASTER);
        Course advancedProgramming = new Course("advanced_programming", CourseLevel.MASTER);
        Course mathematicalAnalysis = new Course("mathematical_analysis", CourseLevel.MASTER);
        Course computerVision = new Course("computer_vision", CourseLevel.MASTER);

        Student alice = new Student("Alice");
        Student bob = new Student("Bob");
        Student alex = new Student("Alex");

        Professor ali = new Professor("Ali");
        Professor ahmed = new Professor("Ahmed");
        Professor andrey = new Professor("Andrey");

        alice.enroll(javaBeginner);
        alice.enroll(javaIntermediate);
        alice.enroll(pythonBasics);
        bob.enroll(javaBeginner);
        bob.enroll(algorithms);
        alex.enroll(advancedProgramming);

        ali.teach(javaBeginner);
        ali.teach(javaIntermediate);
        ahmed.teach(pythonBasics);
        ahmed.teach(advancedProgramming);
        andrey.teach(mathematicalAnalysis);

    }
    public static void main(String[] args) {
        fillInitialData();
        Utility.execute();
    }
}

/**
 * Interface representing the behavior of enrolling and dropping courses for University Members.
 */
interface Enrollable {
    boolean drop(Course course);
    boolean enroll(Course course);
}

/**
 * Abstract class defining University Members and their attributes.
 */
abstract class UniversityMember {
    private static int numberOfMembers;
    private int memberId;
    private String memberName;
    public UniversityMember(int memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
    }
    public static int getNumberOfMembers() {
        return numberOfMembers;
    }
    public String getMemberName() {
        return memberName;
    }
    public int getMemberId() {
        return memberId;
    }

    public static void setNumberOfMembers(int numberOfMembers) {
        UniversityMember.numberOfMembers = numberOfMembers;
    }
}

/**
 * Enum representing different levels of courses: BACHELOR and MASTER.
 */
enum CourseLevel { BACHELOR, MASTER }

/**
 * Represents a Course in the university, handling its attributes and operations.
 */
class Course {
    // Course's attributes
    private static final int CAPACITY = 3;
    private static int numberOfCourses;
    private int courseId;
    private String courseName;
    private List<Student> enrolledStudents = new ArrayList<>();
    private CourseLevel courseLevel;
    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    // Initializing the Course
    public Course(String courseName, CourseLevel courseLevel) {
        numberOfCourses += 1;
        this.courseId = numberOfCourses;
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        Utility.getCourses().put(numberOfCourses, this);
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public static int getCAPACITY() {
        return CAPACITY;
    }
    public boolean isFull() {
        return enrolledStudents.size() >= CAPACITY; }
}

/**
 * Represents a Student in the university, inheriting from UniversityMember and implementing Enrollable.
 */
class Student extends UniversityMember implements Enrollable {
    // Student's attributes

    /**
     * Maximum number of courses a student can enroll in.
     */
    private static final int MAX_ENROLLMENT = 3;

    /**
     * List of courses in which the student is enrolled.
     */
    private List<Course> enrolledCourses = new ArrayList<Course>();

    /**
     * Initializes a new Student object with a specified member name.
     *
     * @param memberName The name of the student.
     */
    public Student(String memberName) {
        super(getNumberOfMembers() + 1, memberName);
        setNumberOfMembers(getNumberOfMembers() + 1);
        Utility.getStudents().put(getNumberOfMembers(), this);
    }

    /**
     * Drops a specified course for the student.
     *
     * @param course The course to be dropped.
     * @return True if the course is successfully dropped, false otherwise.
     */
    public boolean drop(Course course) {
        // Handling dropping problems
        if (!enrolledCourses.contains(course)) {
            System.out.println("Student is not enrolled in this course");
            return false;
        }
        // Dropping the student if possible
        this.enrolledCourses.remove(course);
        course.getEnrolledStudents().remove(this);
        return true;
    }

    /**
     * Enrolls the student in a specified course.
     *
     * @param course The course to be enrolled in.
     * @return True if the student is successfully enrolled, false otherwise.
     */
    public boolean enroll(Course course) {
        // Handling enrolling problems
        if (enrolledCourses.contains(course)) {
            System.out.println("Student is already enrolled in this course");
            return false;
        } else if (enrolledCourses.size() >= MAX_ENROLLMENT) {
            System.out.println("Maximum enrollment is reached for the student");
            return false;
        } else if (course.getEnrolledStudents().size() >= Course.getCAPACITY()) {
            System.out.println("Course is full");
            return false;
        }
        // Enrolling the student if possible
        this.enrolledCourses.add(course);
        course.getEnrolledStudents().add(this);
        return true;
    }
}

/**
 * Represents a Professor in the university, inheriting from UniversityMember.
 */
class Professor extends UniversityMember {
    /**
     * Maximum number of courses a professor can teach.
     */
    private static final int MAX_LOAD = 2;
    /**
     * List of courses assigned to the professor.
     */
    private List<Course> assignedCourses = new ArrayList<Course>();

    /**
     * Initializes a new Professor object with a specified member name.
     *
     * @param memberName The name of the professor.
     */
    public Professor(String memberName) {
        super(getNumberOfMembers() + 1, memberName);
        setNumberOfMembers(getNumberOfMembers() + 1);
        Utility.getProfessors().put(getNumberOfMembers(), this);
    }

    /**
     * Retrieves the list of courses assigned to the professor.
     *
     * @return The list of assigned courses.
     */
    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }
    /**
            * Exempts the professor from teaching a specific course.
            *
            * @param course The course from which the professor is to be exempted.
     * @return True if the professor is successfully exempted, false otherwise.
     */
    public boolean exempt(Course course) {
        // Handling exempting problems
        if (!assignedCourses.contains(course)) {
            System.out.println("Professor is not teaching this course");
            return false;
        }
        // Exempting the professor if possible
        this.assignedCourses.remove(course);
        return true;
    }

    /**
            * Assigns the professor to teach a specific course.
            *
            * @param course The course to be assigned for teaching.
     * @return True if the professor is successfully assigned, false otherwise.
     */
    public boolean teach(Course course) {
        // Handling teaching problems
        if (assignedCourses.size() >= MAX_LOAD) {
            System.out.println("Professor's load is complete");
            return false;
        } else if (assignedCourses.contains(course)) {
            System.out.println("Professor is already teaching this course");
            return false;
        }
        // Assigning the professor if possible
        this.assignedCourses.add(course);
        return true;
    }
}

/**
 * Utility class providing functionalities to execute the University Course Management System.
 */
final class Utility {
    /**
     * Scanner instance to read user inputs.
     */
    private static Scanner scan = new Scanner(System.in);
    public static Scanner getScan() {
        return scan;
    }

    /**
     * Executes the commands based on user inputs.
     * Reads user input continuously and executes corresponding actions.
     */
    public static void execute() {
        String command;
        while (scan.hasNextLine()) {
            command = scan.nextLine();
            if (command.isEmpty()) {
                System.exit(0);
            } else if (!Utility.contains(command)) {
                System.out.println("Wrong inputs");
                System.exit(0);
            } else {
                switch (command) {
                    case "course":
                        toCourse();
                        System.out.println("Added successfully");
                        break;
                    case "student":
                        toStudent();
                        System.out.println("Added successfully");
                        break;
                    case "professor":
                        toProfessor();
                        System.out.println("Added successfully");
                        break;
                    case "enroll":
                        toEnroll();
                        System.out.println("Enrolled successfully");
                        break;
                    case "drop":
                        toDrop();
                        System.out.println("Dropped successfully");
                        break;
                    case "teach":
                        toTeach();
                        System.out.println("Professor is successfully assigned to teach this course");
                        break;
                    case "exempt":
                        toExempt();
                        System.out.println("Professor is exempted");
                        break;
                    default:
                        System.out.println("Wrong inputs");
                }
            }
        }
    }

    /**
     * List of available commands.
     */
    private static String[] commandsList = {"course", "student", "professor", "enroll", "drop", "teach", "exempt"};

    public static String[] getCommandsList() {
        return commandsList;
    }

    /**
     * Retrieves the level of a course based on a given string.
     *
     * @param level The level string ("bachelor" or "master").
     * @return The CourseLevel corresponding to the given string.
     */
    public static CourseLevel getLevel(String level) {
        if (level.equalsIgnoreCase("master")) {
            return CourseLevel.MASTER;
        } else if (level.equalsIgnoreCase("bachelor")) {
            return CourseLevel.BACHELOR;
        }
        return null;
    }
    /**
     * Checks if a command is present in the commands list.
     *
     * @param key The command to check.
     * @return True if the command exists in the list, false otherwise.
     */
    public static boolean contains(String key) {
        for (String str : Utility.getCommandsList()) {
            if (str.equals(key)) {
                return true;
            }
        }
        return false;
    }
    public static boolean checkCourseName(String courseName) {
        return Pattern.matches("^[a-zA-Z]+_?[a-zA-Z]+$", courseName);
    }
    public static boolean checkMemberName(String memberName) {
        return Pattern.matches("^[a-zA-Z]+$", memberName);
    }
    public static boolean checkId(String id) {
        return Pattern.matches("^[0-9]+$", id);
    }
    private static Map<Integer, Student> students = new HashMap<>();
    public static Map<Integer, Student> getStudents() {
        return students;
    }
    private static Map<Integer, Professor> professors = new HashMap<>();
    public static Map<Integer, Professor> getProfessors() {
        return professors;
    }
    private static Map<Integer, Course> courses = new HashMap<>();

    public static Map<Integer, Course> getCourses() {
        return courses;
    }

    public static void toCourse() {
        String courseName;
        String courseLevel;
        courseName = scan.nextLine();
        // Checking course name
        if (!checkCourseName(courseName) || contains(courseName)) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        for (Course course : courses.values()) {
            if (course.getCourseName().equals(courseName)) {
                System.out.println("Course exists");
                System.exit(0);
            }
        }
        courseLevel = scan.nextLine();
        CourseLevel level = getLevel(courseLevel);
        // Checking course level
        if (level == null) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        Course newCourse = new Course(courseName, level);

    }
    public static void toStudent() {
        String memberName;
        memberName = scan.nextLine();
        if (!checkMemberName(memberName) || contains(memberName)) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        Student newStudent = new Student(memberName);
    }
    public static void toProfessor() {
        String memberName;
        memberName = scan.nextLine();
        if (!checkMemberName(memberName) || contains(memberName)) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        Professor newProfessor = new Professor(memberName);
    }
    public static void toEnroll() {
        String memberId;
        memberId = scan.nextLine();
        // correct input?
        if (!checkId(memberId) || !students.containsKey(Integer.valueOf(memberId))) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        String courseId;
        courseId = scan.nextLine();
        // does exist?
        if (!courses.containsKey(Integer.valueOf(courseId))) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        if (!students.get(Integer.valueOf(memberId)).enroll(courses.get(Integer.valueOf(courseId)))) {
            System.exit(0);
        }
    }

    public static void toDrop() {
        String memberId;
        memberId = scan.nextLine();
        // correct input?
        if (!checkId(memberId) || !students.containsKey(Integer.valueOf(memberId))) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        String courseId;
        courseId = scan.nextLine();
        // does exist?
        if (!courses.containsKey(Integer.valueOf(courseId))) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        if (!students.get(Integer.valueOf(memberId)).drop(courses.get(Integer.valueOf(courseId)))) {
            System.exit(0);
        }
    }
    public static void toTeach() {
        String memberId;
        memberId = scan.nextLine();
        // correct input?
        if (!checkId(memberId) || !professors.containsKey(Integer.valueOf(memberId))) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        String courseId;
        courseId = scan.nextLine();
        // does exist?
        if (!courses.containsKey(Integer.valueOf(courseId))) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        //time to govno code
        if (!professors.get(Integer.valueOf(memberId)).teach(courses.get(Integer.valueOf(courseId)))) {
            System.exit(0);
        }
    }
    public static void toExempt() {
        String memberId;
        memberId = scan.nextLine();
        // correct input?
        if (!checkId(memberId) || !professors.containsKey(Integer.valueOf(memberId))) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        String courseId;
        courseId = scan.nextLine();
        // does exist?
        if (!courses.containsKey(Integer.valueOf(courseId))) {
            System.out.println("Wrong inputs");
            System.exit(0);
        }
        if (!professors.get(Integer.valueOf(memberId)).exempt(courses.get(Integer.valueOf(courseId)))) {
            System.exit(0);
        }
    }
}


