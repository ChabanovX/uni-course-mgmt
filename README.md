# Uni Course Mgmt

Minimal Java CLI for managing **courses**, **students**, and **professors**. Ships with seeded demo data and strict input validation.

## Build & Run
```bash
javac UniversityCourseManagementSystem.java
java UniversityCourseManagementSystem
```

Commands
	•	course → add course (<name>\n<level: bachelor|master>)
	•	student → add student (<name>)
	•	professor → add professor (<name>)
	•	enroll → student to course (<studentId>\n<courseId>)
	•	drop → student from course (<studentId>\n<courseId>)
	•	teach → assign professor (<professorId>\n<courseId>)
	•	exempt → unassign professor (<professorId>\n<courseId>)

