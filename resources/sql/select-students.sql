SELECT * FROM students
    WHERE teacher_id = :user_id
    ORDER BY name ASC;
