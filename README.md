# 🎓 GradesApp - CLI Grade Calculator

> A simple, professional, and user-friendly command-line tool to compute course grades from assessment scores. Enter scores interactively or from a text file; the app applies category averages, a **reading-drops policy**, weighted totals, and returns the **letter grade**.

```
┌──────────────────────────────────────────────────────────────────┐
│                       G R A D E S A P P                           │
│     StudentRecord  •  GradeLedger  •  GradesApp (CLI driver)      │
└──────────────────────────────────────────────────────────────────┘
```

## ✨ What it does
- Stores per-student scores across **Readings, Labs, Exercises, Projects, Participation, Midterm, Final**.
- Applies category averages and **drops the lowest 15 reading subsection scores**.
- Replaces Midterm with Final if the Final is higher (policy enabled in code).
- Computes **weighted total** and **letter grade**.
- Provides class stats (min, max, median, average) via `GradeLedger` (for programmatic use).

---

## 📦 Prerequisites

| Tool | Version (recommended) | Notes |
|---|---:|---|
| Java JDK | **17+** (tested on **21**) | Standard `javac`/`java` only-no external libs |
| OS | Windows / macOS / Linux | Any system with a JDK installed |

> Tip: On Windows, install via [winget] `winget install EclipseAdoptium.Temurin.21.JDK` or Oracle/Microsoft builds. On macOS, `brew install openjdk` is fine.

---

## 🛠️ Install / Build

Put the three source files in **one folder**:
```
GradesApp.java
StudentRecord.java
GradeLedger.java
```

Compile from that folder:
```bash
javac GradesApp.java StudentRecord.java GradeLedger.java
```

This will produce `.class` files in the same directory.

---

## ▶️ Run

### Option A - Interactive (recommended)
```bash
java GradesApp
```
You'll be guided through each field (name, G#, lists of scores, etc). Enter **numbers 0-100** (decimals allowed). For lists, use commas.

### Option B - From a file
```bash
java GradesApp grades.txt
```
Where `grades.txt` contains **one item per line in this exact order**:

1) Name  
2) G#  
3) Projects (comma-separated)  
4) Exercises (comma-separated)  
5) Labs (comma-separated)  
6) Readings (comma-separated)  
7) Participation (single value)  
8) Midterm (single value)  
9) Final (single value)

**Minimal example `grades.txt`:**
```
Jane Doe
G01234567
95, 88
100, 90, 80
100, 100, 0, 100, 100
90, 92.5, 88, 100, 95, 86, 91, 89, 90, 93, 88, 90, 91, 94, 85, 100, 98
10
84
93
```

> Input rules: enter percentages as plain numbers (e.g., `48/50 -> 96.0`). Do **not** type `%`.

---

## ⚖️ Weights & Policies

Default category weights (modifiable in code) follow the spec order:  
**Participation, Readings, Labs, Exercises, Projects, Midterm, Final**

```
double[] gradeWeights = {0.05, 0.05, 0.15, 0.09, 0.21, 0.20, 0.25};
//           5%     5%     15%    9%     21%     20%     25%
```

- **Readings drop 15**: sort reading subsection scores ascending, **ignore the lowest 15**, average the rest. If < 16 items, readings count as **100** by policy.
- **Empty category rule** (Labs/Exercises/Projects): if no scores, that category counts as **100** (full credit).
- **Final replaces Midterm** if Final >= Midterm.
- **Letter grade** is derived from the computed total; if **Final < 60**, the letter grade is `F` regardless of total.

> You can change the weights and policies by editing `StudentRecord.java` and/or the `gradeWeights` array in `GradesApp.java` before compiling.

---

## 💻 Example Session (interactive)

```
$ java GradesApp
Enter a name (String): Jane Doe
Enter a G#: G01234567
Enter a list of scores (out of 100) for the projects, separated by commas: 95, 88
Enter a list of scores (out of 100) for the exercises, separated by commas: 100, 90, 80
Enter a list of scores (out of 100) for the labs, separated by commas: 100, 100, 0, 100, 100
Enter a list of scores (out of 100) for the readings, separated by commas: 90, 92.5, 88, 100, 95, ...
Enter participation: 10
Enter a score for the midterm: 84
Enter a score for the final exam: 93

Grades
┌─────────────────────────────────────────────────────────┐
│ Name: Jane Doe        G#: G01234567                     │
│ Participation: 10.00                                    │
│ Readings:     95.00  [ ... ]                            │
│ Labs:         80.00  [100.0, 100.0, 0.0, 100.0, 100.0]  │
│ Exercises:    90.00  [100.0, 90.0, 80.0]                │
│ Projects:     91.50  [95.0, 88.0]                       │
│ Midterm:      84.00                                     │
│ Final Exam:   93.00                                     │
│ Total:   89.42   Letter: B+                             │
└─────────────────────────────────────────────────────────┘
```

*(Your exact numbers will vary with your inputs.)*

---

## 🔍 Project Structure

- **`StudentRecord`** - holds all fields per student, adders for lists, computes unweighted category scores, total, and letter grade; implements the **reading drop** and **final-replaces-midterm** rules; pretty `toString()` output.
- **`GradeLedger`** - holds multiple `StudentRecord`s and computes **classAverage**, **classMin**, **classMax**, **classMedian**; comparator by total score.
- **`GradesApp`** - CLI driver: prompts interactively or reads a file; sets default weights; prints a formatted report.

---

## 🧭 Usage Tips

- Put **lists** on a single line and separate with commas: `96.0, 85.1, 100.0`  
- Enter **0-100** values; decimals ok.  
- If you typo a line in file mode, fix the file and rerun.
- For repeated runs, keep several `.txt` profiles and pass the one you want on the command line.

---

## 🚧 Limits

- **No persistent storage**: data lives only for the current run; there's no save/load of multiple students from files beyond the single input case.
- **Single-student CLI**: the provided CLI operates on one student at a time (the ledger API supports many in code, but there's no multi-student batch CLI yet).
- **No GUI/Web**: command-line only.
- **Minimal input validation**: expects well-formed numeric input (0-100). Out-of-range values are not corrected.
- **Fixed policies**: "drop 15 readings", "empty category -> 100", "final replaces midterm", and the weight array are coded defaults-you must edit & recompile to change them.
- **No grading curve**: straight weighted average + letter mapping only.

---

## 🧪 Quick Test

After compiling, run:
```bash
java GradesApp <<EOF
Jane Doe
G01234567
95, 88
100, 90, 80
100, 100, 0, 100, 100
90, 92.5, 88, 100, 95, 86, 91, 89, 90, 93, 88, 90, 91, 94, 85, 100, 98
10
84
93
EOF
```

You should see a formatted summary with a Total and Letter grade.

---
