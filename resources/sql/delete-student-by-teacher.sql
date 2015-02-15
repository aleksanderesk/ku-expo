DELETE IGNORE FROM students
    WHERE id = :student_id AND teacher_id = :user_id;
