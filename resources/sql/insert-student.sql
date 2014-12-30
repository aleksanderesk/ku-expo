INSERT IGNORE INTO students
    (id, teacher_id, name, division) VALUES (NULL, :user_id, :name, :division);
