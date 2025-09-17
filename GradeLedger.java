import java.util.*;
import static java.util.Collections.sort;

/**
 * Comparator-backed grade ledger for {@code StudentRecord}.
 * Provides add, stats (min/max/median/avg), and pretty printing.
 */
public class GradeLedger implements Comparator<StudentRecord> {

    /**
     * All student grade records in this gradebook.
     */
    private Collection<StudentRecord> records;

    /**
     * Build an empty gradebook.
     */
    public GradeLedger() {
        this.records = new ArrayList<>();
    }

    /**
     * Pretty summary of all records and basic statistics.
     *
     * @return multi-line summary string.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┌───────────────────── GRADEBOOK ─────────────────────┐\n");
        if (records.isEmpty()) {
            sb.append("│ No records yet.                                      │\n");
        } else {
            sb.append("│ Students                                             │\n");
            for (StudentRecord r : records) {
                sb.append(String.format(
                        "│ • %-20s | Letter: %-2s | Total: %6.2f │\n",
                        r.getStudentName(), r.letterGrade(), r.totalScore()
                ));
            }
        }
        sb.append("├──────────────────────── STATS ───────────────────────┤\n");
        StudentRecord hi = highestRecord();
        StudentRecord lo = lowestRecord();
        StudentRecord med = medianRecord();
        double avg = classAverage();

        sb.append(String.format("│ Max:    %s\n", describe(hi)));
        sb.append(String.format("│ Median: %s\n", describe(med)));
        sb.append(String.format("│ Avg:    %s\n",
                records.isEmpty() ? "—" : String.format("%.2f", avg)));
        sb.append(String.format("│ Min:    %s\n", describe(lo)));
        sb.append("└──────────────────────────────────────────────────────┘");
        return sb.toString();
    }

    /**
     * Add a student's grades to the ledger.
     *
     * @param entry grades for one student.
     */
    public void insertRecord(StudentRecord entry) {
        records.add(entry);
    }

    /**
     * Highest-scoring student.
     *
     * @return record with the greatest total, or {@code null} if none.
     */
    public StudentRecord highestRecord() {
        StudentRecord best = null;
        boolean first = true;
        for (StudentRecord rec : records) {
            if (first) {
                best = rec;
                first = false;
            } else if (rec.totalScore() > best.totalScore()) {
                best = rec;
            }
        }
        return best;
    }

    /**
     * Lowest-scoring student.
     *
     * @return record with the smallest total, or {@code null} if none.
     */
    public StudentRecord lowestRecord() {
        StudentRecord worst = null;
        boolean first = true;
        for (StudentRecord rec : records) {
            if (first) {
                worst = rec;
                first = false;
            } else if (rec.totalScore() < worst.totalScore()) {
                worst = rec;
            }
        }
        return worst;
    }

    /**
     * Median student by total (0-based floor of sorted middle).
     *
     * @return median record, or {@code null} if empty.
     */
    public StudentRecord medianRecord() {
        if (records.isEmpty()) return null;
        ArrayList<StudentRecord> sorted = new ArrayList<>(records);
        sorted.sort(this); // uses compare(...) below
        return sorted.get(sorted.size() / 2);
    }

    /**
     * Mean of all totals.
     *
     * @return average total, or 0.0 if empty.
     */
    public double classAverage() {
        if (records.isEmpty()) return 0.0;
        double sum = 0.0;
        for (StudentRecord rec : records) sum += rec.totalScore();
        return sum / records.size();
    }

    /**
     * Comparator: order by total score ascending.
     *
     * @param left  first record
     * @param right second record
     * @return negative if left<right, zero if equal, positive if left>right.
     */
    @Override
    public int compare(StudentRecord left, StudentRecord right) {
        return Double.compare(left.totalScore(), right.totalScore());
    }

    /* ------------------------- helpers ------------------------- */

    private String describe(StudentRecord r) {
        if (r == null) return "—";
        return String.format("%s (Letter: %s, Total: %.2f)",
                r.getStudentName(), r.letterGrade(), r.totalScore());
    }
}
