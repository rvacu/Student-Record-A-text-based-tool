// GradesApp.java

import java.util.Scanner;
import java.io.File;

public class GradesApp {
    public static void main(String[] args){
        double[] gradeWeights = {0.05, 0.05, 0.15, 0.09, 0.21, 0.2, 0.25};
        try{
            String msg =
			"┌──────────────────────────────────────────────────────────────┐\n" +
			"│                       GRADE INPUT GUIDE                      │\n" +
			"├──────────────────────────────────────────────────────────────┤\n" +
			"│ RULES                                                        │\n" +
			"│ • Enter numbers 0–100 (decimals ok). Don’t type the % sign.  │\n" +
			"│ • Single score example: 48/50 → 96.0                         │\n" +
			"│ • List example (spaces allowed): 96.0, 85.1, 100.0           │\n" +
			"├──────────────────────────────────────────────────────────────┤\n" +
			"│ INTERACTIVE ORDER                                            │\n" +
			"│  1) Name   2) G#   3) Projects (list)                        │\n" +
			"│  4) Exercises (list)   5) Labs (list)   6) Readings (list)   │\n" +
			"│  7) Participation   8) Midterm   9) Final                    │\n" +
			"├──────────────────────────────────────────────────────────────┤\n" +
			"│ FILE INPUT (first CLI arg)                                   │\n" +
			"│ • Same order as above. One line per item; lists are comma-   │\n" +
			"│   separated on a single line.                                │\n" +
			"│ Example file:                                                │\n" +
			"│   Jane Doe                                                   │\n" +
			"│   G01234567                                                  │\n" +
			"│   100, 92.5, 88                                              │\n" +
			"│   97.0, 100, 95                                              │\n" +
			"│   90, 85.5, 100, 98                                          │\n" +
			"│   96.0, 85.1, 100.0                                          │\n" +
			"│   10                                                         │\n" +
			"│   89.5                                                       │\n" +
			"│   93.0                                                       │\n" +
			"└──────────────────────────────────────────────────────────────┘\n";
			System.out.println(msg);

            Scanner input;
            if (args.length > 0) {
                input = new Scanner(new File(args[0]));
            } else {
                input = new Scanner(System.in);
            }

            System.out.print("Enter a name (String): ");
            String learnerName = input.nextLine();

            System.out.print("Enter a G#: ");
            String studentId = input.nextLine();

            StudentRecord gradeBook = new StudentRecord(learnerName, studentId, gradeWeights);

            // -------------------- Projects --------------------
            String listLine;
            System.out.print("Enter a list of scores (out of 100) for the projects, separated by commas: ");
            listLine = input.nextLine();
            for (String token : listLine.split(",")) {
                gradeBook.addProject(Double.parseDouble(token.trim()));
            }

            // -------------------- Exercises -------------------
            System.out.print("Enter a list of scores (out of 100) for the exercises, separated by commas: ");
            listLine = input.nextLine();
            for (String token : listLine.split(",")) {
                gradeBook.addExercise(Double.parseDouble(token.trim()));
            }

            // -------------------- Labs ------------------------
            System.out.print("Enter a list of scores (out of 100) for the labs, separated by commas: ");
            listLine = input.nextLine();
            for (String token : listLine.split(",")) {
                gradeBook.addLab(Double.parseDouble(token.trim()));
            }

            // -------------------- Readings --------------------
            System.out.print("Enter a list of scores (out of 100) for the readings, separated by commas: ");
            listLine = input.nextLine();
            for (String token : listLine.split(",")) {
                gradeBook.addReading(Double.parseDouble(token.trim()));
            }

            // -------------------- Participation ---------------
            System.out.print("Enter participation: ");
            gradeBook.setParticipation(Double.parseDouble(input.nextLine()));

            // -------------------- Midterm ---------------------
            System.out.print("Enter a score for the midterm: ");
            gradeBook.setMidterm(Double.parseDouble(input.nextLine()));

            // -------------------- Final -----------------------
            System.out.print("Enter a score for the final exam: ");
            gradeBook.setFinalExam(Double.parseDouble(input.nextLine()));

            // Output
            System.out.println("Grades");
            System.out.println(gradeBook);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
