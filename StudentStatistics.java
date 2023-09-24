import java.io.*;
import java.util.Arrays;

class Student {
    String lastName;
    String firstName;
    String studentID;
    double a1Mark;
    double a2Mark;
    double a3Mark;
    double totalMark;

    public Student(String lastName, String firstName, 
    String studentID, double a1Mark, double a2Mark, double a3Mark) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.studentID = studentID;
        this.a1Mark = a1Mark;
        this.a2Mark = a2Mark;
        this.a3Mark = a3Mark;
        calculateTotalMark();

    }

    private void calculateTotalMark() {
        totalMark = a1Mark + a2Mark + a3Mark;
    }

}
public class StudentStatistics {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the input file: ");
        String fileName = scanner.nextLine();

        Student[] students = readStudentDataFromFile(fileName);

        if (students != null) {
            System.out.println("Student data from the file.");
        } else {
            System.out.println("Error reading student data from the file.");
        }
    }

    private static Student[] readStudentDataFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;
            Student[] students = new Student[70]; 

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    // Skip the first line (unit name)
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) { // Check for at least 5 columns (A1, A2, A3)
                    String lastName = parts[0].trim();
                    String firstName = parts[1].trim();
                    String studentID = parts[2].trim();
                    double a1Mark = parseDouble(parts[3]);
                    double a2Mark = parseDouble(parts[4]);
                    double a3Mark = 0.0; // Default value if A3 is missing

                    if (parts.length >= 6) { // Check if A3 is present
                        a3Mark = parseDouble(parts[5]);
                    }

                    students[lineNumber - 2] = new Student(lastName, firstName, studentID, a1Mark, a2Mark, a3Mark);
                }
            }
            return students;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    
    private static double parseDouble(String value) {
        try {
            if (!value.isEmpty() && value.matches("[0-9.]+")) {
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            // Handle the exception (e.g., log or display an error message)
        }
        return 0.0; // Default value if parsing fails or value is empty
    }

    private static void calculateTotalMarksAndPrint(Student[] students) {
        System.out.println("List of Students with Assessment Marks and Total Marks:");
        for (Student student : students) {
            if (student != null) { // Check if the student is not null
                double totalMark = student.a1Mark + student.a2Mark + student.a3Mark;
                System.out.println("Name: " + student.firstName + " " + student.lastName +
                    ", Student ID: " + student.studentID +
                    ", A1 Mark: " + student.a1Mark +
                    ", A2 Mark: " + student.a2Mark +
                    ", A3 Mark: " + student.a3Mark +
                    ", Total Mark: " + totalMark);
            }
        }
    }
    
    private static void printStudentsBelowThreshold(Student[] students, double threshold) {
        System.out.println("Students with total marks below " + threshold + ":");
        for (Student student : students) {
            if (student != null) { // Check if the student is not null
                double totalMark = student.a1Mark + student.a2Mark + student.a3Mark;
                if (totalMark < threshold) {
                    System.out.println("Name: " + student.firstName + " " + student.lastName +
                        ", Student ID: " + student.studentID +
                        ", Total Mark: " + totalMark);
                }
            }
        }
    }
    
    private static void bubbleSortStudents(Student[] students, boolean ascending) {
        int n = students.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (students[j] != null && students[j + 1] != null) {
                    double mark1 = students[j].totalMark;
                    double mark2 = students[j + 1].totalMark;

                    if ((ascending && mark1 > mark2) || (!ascending && mark1 < mark2)) {
                        Student temp = students[j];
                        students[j] = students[j + 1];
                        students[j + 1] = temp;
                    }
                }
            }
        }
    }

}

