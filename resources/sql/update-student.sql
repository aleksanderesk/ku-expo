UPDATE IGNORE students
    SET name = COALESCE(:name, name),
        division = COALESCE(:division, division)
    WHERE id = :student_id AND teacher_id = :user_id;
