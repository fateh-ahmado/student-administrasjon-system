# Student Administration System

Java program for managing students, courses, and grades at a department, controlled through a terminal menu.

## Features

The program displays a menu with the following options:

1. **Print all courses** – lists the course code and course name for all registered courses.
2. **Search for a student by name** – looks up and prints information about a student (name search is case-insensitive).
3. **Find all students in a course with a given grade** – shows all students in a given course who received a specific letter grade (A–F).
4. **Find a student's grade in a course** – shows which grade a given student received in a given course.
5. **0 to exit** – exits the program.

## Class structure

- `Person` – base class with name, address, and phone number.
- `Student` / `Ansatt` (Employee) – inherit from `Person`.
- `Emne` (Course) – represents a course, with lists of enrolled students and grades.
- `Karakter` (Grade) – links a student, a course, and a letter grade.
- `Institutt` (Department) – keeps track of all students and courses, and has methods for adding courses and enrolling students in courses.
- `SokeFunksjoner` (Search functions) – contains the search functions used from the menu.
- `FeilInntastingValg` (Invalid input exception) – custom exception thrown on an invalid menu choice.

Menu selections are run on a separate thread (`StudentAdministrasjonSystem extends Thread`).

## Running the program

```bash
javac StudentAdministrasjonSystem.java
java StudentAdministrasjonSystem
```

The program is automatically populated with some test data (students, courses, and grades) at startup, so the menu options have something to display right away.
