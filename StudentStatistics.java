import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class Student {
    String lastName;
    String firstName;
    String studentID;
    double a1Mark;
    double a2Mark;
    double a3Mark;
    double totalMark;

    public Student(String lastName, String firstName, String studentID, double a1Mark, double a2Mark, double a3Mark) {
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

        ArrayList<Student> students = readStudentDataFromFile(fileName);

        if (students != null) {
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Calculate and Print Total Marks");
                System.out.println("2. Print Students Below a Threshold");
                System.out.println("3. Print Top 5 Students with Highest Total Marks");
                System.out.println("4. Print Top 5 Students with Lowest Total Marks");
                System.out.println("5. Exit");
                System.out.print("Select an option (1/2/3/4/5): ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        calculateTotalMarksAndPrint(students);
                        break;
                    case 2:
                        System.out.print("Enter the threshold: ");
                        double threshold = scanner.nextDouble();
                        printStudentsBelowThreshold(students, threshold);
                        break;
                    case 3:
                        printTopNStudents(students, 5, false);
                        break;
                    case 4:
                        printTopNStudents(students, 5, true);
                        break;
                    case 5:
                        System.out.println("Exiting the program.");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid option. Please select again.");
                }
            }
        } else {
            System.out.println("Error reading student data from the file.");
        }
    }

    private static ArrayList<Student> readStudentDataFromFile(String fileName) {
        ArrayList<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 0;

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

                    // Check if studentID is a valid integer and all marks are valid (non-negative)
                    if (isInteger(studentID) && a1Mark >= 0 && a2Mark >= 0 && a3Mark >= 0) {
                        students.add(new Student(lastName, firstName, studentID, a1Mark, a2Mark, a3Mark));
                    }
                }
            }
            return students;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
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

    private static void calculateTotalMarksAndPrint(ArrayList<Student> students) {
        System.out.println("List of Students with Assessment Marks and Total Marks:");
        for (Student student : students) {
            double totalMark = student.a1Mark + student.a2Mark + student.a3Mark;
            System.out.println("Name: " + student.firstName + " " + student.lastName +
                ", Student ID: " + student.studentID +
                ", A1 Mark: " + student.a1Mark +
                ", A2 Mark: " + student.a2Mark +
                ", A3 Mark: " + student.a3Mark +
                ", Total Mark: " + totalMark);
        }
    }

    private static void printStudentsBelowThreshold(ArrayList<Student> students, double threshold) {
        System.out.println("Students with total marks below " + threshold + ":");
        for (Student student : students) {
            double totalMark = student.a1Mark + student.a2Mark + student.a3Mark;
            if (totalMark < threshold) {
                System.out.println("Name: " + student.firstName + " " + student.lastName +
                    ", Student ID: " + student.studentID +
                    ", Total Mark: " + totalMark);
            }
        }
    }

    private static void bubbleSortStudents(ArrayList<Student> students, boolean ascending) {
        int n = students.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                double mark1 = students.get(j).totalMark;
                double mark2 = students.get(j + 1).totalMark;

                // Compare based on sorting order (ascending or descending)
                boolean shouldSwap = ascending ? mark1 > mark2 : mark1 < mark2;

                if (shouldSwap) {
                    Collections.swap(students, j, j + 1);
                }
            }
        }
    }

    private static void printTopNStudents(ArrayList<Student> students, int n, boolean highest) {
        // Create a copy of the students ArrayList to avoid modifying the original list
        ArrayList<Student> copyStudents = new ArrayList<>(students);

        bubbleSortStudents(copyStudents, highest); // Sort in the specified order

        String order = highest ? "highest" : "lowest";
        System.out.println("Top " + n + " students with " + order + " total marks:");

        // Find the actual number of students to print (up to n)
        int count = 0;
        for (int i = 0; i < copyStudents.size() && count < n; i++) {
            Student student = copyStudents.get(i);
            double totalMark = student.a1Mark + student.a2Mark + student.a3Mark;
            if (totalMark > 0.0) { // Skip students with total marks of 0.0
                System.out.println("Name: " + student.firstName + " " + student.lastName +
                    ", Student ID: " + student.studentID +
                    ", Total Mark: " + totalMark);
                count++;
            }
        }
    }
}
