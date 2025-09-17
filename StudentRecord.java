import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Teacher-grade toolkit for a single student.
 * Tracks inputs, computes weighted totals, and derives a letter grade.
 *
 * @version 1.0
 */
public class StudentRecord {

    /* ----------------------- identity & exams ----------------------- */
    private String name;
    private String studentId;

    private double participationScore;
    private double midtermScore;
    private double finalExamScore;

    /* ----------------------- coursework buckets -------------------- */
    private List<Double> labScores      = new ArrayList<>();
    private List<Double> exerciseScores = new ArrayList<>();
    private List<Double> projectScores  = new ArrayList<>();
    private List<Double> readingScores  = new ArrayList<>();

    /* --------------------------- weights --------------------------- */
    private double wParticipation;
    private double wReadings;
    private double wLabs;
    private double wExercises;
    private double wProjects;
    private double wMidterm;
    private double wFinal;

    /**
     * Create a record with identity and category weights.
     *
     * @param studentName name of the student
     * @param gNumber     institutional id (e.g., G#)
     * @param categoryWeight weights array in order:
     *                       [participation, readings, labs, exercises, projects, midterm, final]
     */
    public StudentRecord(String studentName, String gNumber, double[] categoryWeight) {
        setStudentName(studentName);
        setGNumber(gNumber);
        setWeights(categoryWeight);
    }

    /* ========================== adders ========================== */

    /**
     * Add one reading score.
     *
     * @param readingScore score out of 100
     */
    public void addReading(double readingScore) {
        readingScores.add(readingScore);
    }

    /**
     * Add one lab score.
     *
     * @param labScore score out of 100
     */
    public void addLab(double labScore) {
        labScores.add(labScore);
    }

    /**
     * Add one exercise score.
     *
     * @param exerciseScore score out of 100
     */
    public void addExercise(double exerciseScore) {
        exerciseScores.add(exerciseScore);
    }

    /**
     * Add one project score.
     *
     * @param projectScore score out of 100
     */
    public void addProject(double projectScore) {
        projectScores.add(projectScore);
    }

    /* ======================= unweighted means ===================== */

    /**
     * Average of readings after dropping the lowest 15.
     * If fewer than 16 readings, returns 100.
     *
     * @return unweighted readings average
     */
    public double unweightedReadingsScore() {
        if (readingScores.size() < 16) return 100.0;

        Collections.sort(readingScores);
        double sum = 0.0;
        for (int i = 15; i < readingScores.size(); i++) sum += readingScores.get(i);
        return sum / (readingScores.size() - 15);
    }

    /**
     * Average of labs; if none, returns 100.
     *
     * @return unweighted labs average
     */
    public double unweightedLabsScore() {
        if (labScores.isEmpty()) return 100.0;
        double sum = 0.0;
        for (double s : labScores) sum += s;
        return sum / labScores.size();
    }

    /**
     * Average of exercises; if none, returns 100.
     *
     * @return unweighted exercises average
     */
    public double unweightedExercisesScore() {
        if (exerciseScores.isEmpty()) return 100.0;
        double sum = 0.0;
        for (double s : exerciseScores) sum += s;
        return sum / exerciseScores.size();
    }

    /**
     * Average of projects; if none, returns 100.
     *
     * @return unweighted projects average
     */
    public double unweightedProjectsScore() {
        if (projectScores.isEmpty()) return 100.0;
        double sum = 0.0;
        for (double s : projectScores) sum += s;
        return sum / projectScores.size();
    }

    /* ======================== rules & totals ====================== */

    /**
     * True if the final should replace the midterm (final > midterm).
     *
     * @return whether final substitutes midterm
     */
    public boolean finalReplacesMidterm() {
        return finalExamScore > midtermScore;
    }

    /**
     * True if the final exam is a passing score (>= 60).
     *
     * @return pass/fail for final exam
     */
    public boolean finalIsPassing() {
        return finalExamScore >= 60.0;
    }

    /**
     * Weighted total across all components using current weights.
     * If final beats midterm, the midterm weight is applied to the final instead.
     *
     * @return overall numeric score
     */
    public double totalScore() {
        double total = 0.0;

        total += (participationScore * wParticipation);
        total += (unweightedExercisesScore() * wExercises);
        total += (unweightedProjectsScore()  * wProjects);
        total += (unweightedLabsScore()      * wLabs);
        total += (unweightedReadingsScore()  * wReadings);

        if (finalReplacesMidterm()) {
            total += (finalExamScore * wFinal) + (finalExamScore * wMidterm);
        } else {
            total += (finalExamScore * wFinal) + (midtermScore   * wMidterm);
        }
        return total;
    }

    /**
     * Letter from totalScore(), with final-exam fail => F.
     *
     * @return letter grade
     */
    public String letterGrade() {
        if (!finalIsPassing()) return "F";

        double t = totalScore();
        if      (t < 60) return "F";
        else if (t < 70) return "D";
        else if (t < 72) return "C-";
        else if (t < 78) return "C";
        else if (t < 80) return "C+";
        else if (t < 82) return "B-";
        else if (t < 88) return "B";
        else if (t < 90) return "B+";
        else if (t < 92) return "A-";
        else if (t < 98) return "A";
        else if (t <= 100) return "A+";
        return "Not Within Range";
    }

    /* ===================== getters (public API) ==================== */

    /**
     * Participation score.
     *
     * @return participation
     */
    public double getParticipation() {
        return this.participationScore;
    }

    /**
     * Midterm numeric score.
     *
     * @return midterm
     */
    public double getMidterm() {
        return this.midtermScore;
    }

    /**
     * Final-exam numeric score.
     *
     * @return final exam
     */
    public double getFinalExam() {
        return this.finalExamScore;
    }

    /**
     * Student name.
     *
     * @return name
     */
    public String getStudentName() {
        return this.name;
    }

    /**
     * Student identifier (G#).
     *
     * @return id string
     */
    public String getGNumber() {
        return this.studentId;
    }

    /* ===================== setters (public API) ==================== */

    /**
     * Set participation score.
     *
     * @param participation score out of 100
     */
    public void setParticipation(double participation) {
        this.participationScore = participation;
    }

    /**
     * Set midterm score.
     *
     * @param midterm score out of 100
     */
    public void setMidterm(double midterm) {
        this.midtermScore = midterm;
    }

    /**
     * Set final exam score.
     *
     * @param finalExam score out of 100
     */
    public void setFinalExam(double finalExam) {
        this.finalExamScore = finalExam;
    }

    /**
     * Set student name.
     *
     * @param studentName name string
     */
    public void setStudentName(String studentName) {
        this.name = studentName;
    }

    /**
     * Set student id (G#).
     *
     * @param gNumber id string
     */
    public void setGNumber(String gNumber) {
        this.studentId = gNumber;
    }

    /**
     * Set all category weights in order.
     *
     * @param weights [participation, readings, labs, exercises, projects, midterm, final]
     */
    public void setWeights(double[] weights) {
        this.wParticipation = weights[0];
        this.wReadings      = weights[1];
        this.wLabs          = weights[2];
        this.wExercises     = weights[3];
        this.wProjects      = weights[4];
        this.wMidterm       = weights[5];
        this.wFinal         = weights[6];
    }

    /* ======================= presentation ========================= */

    @Override
    public String toString() {
        String nl = System.lineSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append("┌──────────────────────── STUDENT ────────────────────────┐").append(nl);
        sb.append(String.format("│ Name : %-46s │%n", getStudentName()));
        sb.append(String.format("│ G#   : %-46s │%n", getGNumber()));
        sb.append("├────────────────────── RAW SCORES ───────────────────────┤").append(nl);
        sb.append(String.format("│ Participation : %6.2f                                 │%n", getParticipation()));
        sb.append(String.format("│ Readings      : %6.2f  %s│%n", unweightedReadingsScore(),
                padRight(readingScores.toString(), 23)));
        sb.append(String.format("│ Labs          : %6.2f  %s│%n", unweightedLabsScore(),
                padRight(labScores.toString(), 23)));
        sb.append(String.format("│ Exercises     : %6.2f  %s│%n", unweightedExercisesScore(),
                padRight(exerciseScores.toString(), 23)));
        sb.append(String.format("│ Projects      : %6.2f  %s│%n", unweightedProjectsScore(),
                padRight(projectScores.toString(), 23)));
        sb.append(String.format("│ Midterm       : %6.2f                                 │%n", getMidterm()));
        sb.append(String.format("│ Final Exam    : %6.2f                                 │%n", getFinalExam()));
        sb.append("├─────────────────────── SUMMARY ─────────────────────────┤").append(nl);
        sb.append(String.format("│ Total: %6.2f   Letter: %-3s                         │%n", totalScore(), letterGrade()));
        sb.append("└─────────────────────────────────────────────────────────┘");
        return sb.toString();
    }

    /* --------------------------- helpers --------------------------- */

    private static String padRight(String s, int width) {
        if (s.length() >= width) return s.substring(0, width);
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < width) sb.append(' ');
        return sb.toString();
    }
}
