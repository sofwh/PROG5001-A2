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
                    double a1Mark = Double.parseDouble(parts[3]);
                    double a2Mark = Double.parseDouble(parts[4]);
                    double a3Mark = 0.0; // Default value if A3 is missing

                    if (parts.length >= 6) { // Check if A3 is present
                        a3Mark = Double.parseDouble(parts[5]);
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

}

