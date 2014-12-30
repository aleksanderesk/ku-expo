SELECT * FROM students
    WHERE teacher_id = :user_id AND division = :division
    ORDER BY students.name ASC;
