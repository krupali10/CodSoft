import java.util.*;

class Course {
    private String courseCode;
    private String title;
    private String description;
    private int capacity;
    private List<String> schedule;
    private List<String> registeredStudents;

    public Course(String courseCode, String title, String description, int capacity, List<String> schedule) {
        this.courseCode = courseCode;
        this.title = title;
        this.description = description;
        this.capacity = capacity;
        this.schedule = schedule;
        this.registeredStudents = new ArrayList<>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getSchedule() {
        return schedule;
    }

    public List<String> getRegisteredStudents() {
        return registeredStudents;
    }

    public int getAvailableSlots() {
        return capacity - registeredStudents.size();
    }

    public boolean addStudent(String studentId) {
        if (registeredStudents.size() < capacity) {
            registeredStudents.add(studentId);
            return true;
        }
        return false;
    }

    public boolean removeStudent(String studentId) {
        return registeredStudents.remove(studentId);
    }
}

class Student {
    private String studentId;
    private String name;
    private List<String> registeredCourses;

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
        this.registeredCourses = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public List<String> getRegisteredCourses() {
        return registeredCourses;
    }

    public void registerCourse(String courseCode) {
        registeredCourses.add(courseCode);
    }

    public void dropCourse(String courseCode) {
        registeredCourses.remove(courseCode);
    }
}

class CourseDatabase {
    private Map<String, Course> courses;

    public CourseDatabase() {
        courses = new HashMap<>();
    }

    public void addCourse(Course course) {
        courses.put(course.getCourseCode(), course);
    }

    public Course getCourse(String courseCode) {
        return courses.get(courseCode);
    }

    public Collection<Course> getAllCourses() {
        return courses.values();
    }
}

class StudentDatabase {
    private Map<String, Student> students;

    public StudentDatabase() {
        students = new HashMap<>();
    }

    public void addStudent(Student student) {
        students.put(student.getStudentId(), student);
    }

    public Student getStudent(String studentId) {
        return students.get(studentId);
    }
}

public class CourseRegistrationSystem {
    private static CourseDatabase courseDatabase = new CourseDatabase();
    private static StudentDatabase studentDatabase = new StudentDatabase();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeData();

        while (true) {
            System.out.println("\nCourse Registration System");
            System.out.println("1. List all courses");
            System.out.println("2. Register for a course");
            System.out.println("3. Drop a course");
            System.out.println("4. View registered courses");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    listAllCourses();
                    break;
                case 2:
                    registerForCourse();
                    break;
                case 3:
                    dropCourse();
                    break;
                case 4:
                    viewRegisteredCourses();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void initializeData() {
        // Adding some sample courses
        courseDatabase.addCourse(new Course("1", "Intro to Computer Science", "Basic concepts of computer science", 3, Arrays.asList("Mon 9-11", "Wed 9-11")));
        courseDatabase.addCourse(new Course("2", "Calculus I", "Introduction to calculus", 2, Arrays.asList("Tue 10-12", "Thu 10-12")));
        courseDatabase.addCourse(new Course("3", "Physics I", "Basic principles of physics", 2, Arrays.asList("Mon 2-4", "Wed 2-4")));

        // Adding some sample students
        studentDatabase.addStudent(new Student("101", "krupali"));
        studentDatabase.addStudent(new Student("102", "keval"));
    }

    private static void listAllCourses() {
        System.out.println("\nAvailable Courses:");
        for (Course course : courseDatabase.getAllCourses()) {
            System.out.println("Course Code: " + course.getCourseCode());
            System.out.println("Title: " + course.getTitle());
            System.out.println("Description: " + course.getDescription());
            System.out.println("Capacity: " + course.getCapacity());
            System.out.println("Available Slots: " + course.getAvailableSlots());
            System.out.println("Schedule: " + course.getSchedule());
            System.out.println();
        }
    }

    private static void registerForCourse() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        Student student = studentDatabase.getStudent(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine();
        Course course = courseDatabase.getCourse(courseCode);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        if (course.addStudent(studentId)) {
            student.registerCourse(courseCode);
            System.out.println("Successfully registered for the course.");
        } else {
            System.out.println("Course is full. Cannot register.");
        }
    }

    private static void dropCourse() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        Student student = studentDatabase.getStudent(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter Course Code to Drop: ");
        String courseCode = scanner.nextLine();
        Course course = courseDatabase.getCourse(courseCode);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        if (course.removeStudent(studentId)) {
            student.dropCourse(courseCode);
            System.out.println("Successfully dropped the course.");
        } else {
            System.out.println("You are not registered for this course.");
        }
    }

    private static void viewRegisteredCourses() {
        System.out.print("Enter Student ID: ");
        String studentId = scanner.nextLine();
        Student student = studentDatabase.getStudent(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Registered Courses:");
        for (String courseCode : student.getRegisteredCourses()) {
            Course course = courseDatabase.getCourse(courseCode);
            System.out.println("Course Code: " + course.getCourseCode() + ", Title: " + course.getTitle());
        }
    }
}