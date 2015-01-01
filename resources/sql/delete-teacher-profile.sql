DELETE IGNORE FROM 
    (SELECT * FROM student_to_team ST
        INNER JOIN teams T
            ON ST.team_id = T.id)
    WHERE teacher_id = :user_id;

DELETE IGNORE FROM
    (SELECT * FROM competition_to_team CT
        INNER JOIN teams T
            ON CT.team_id = T.id)
    WHERE teacher_id = :user_id;

DELETE IGNORE FROM teams
    WHERE teacher_id = :user_id;

DELETE IGNORE FROM students
    WHERE teacher_id = :user_id;

DELETE IGNORE FROM logistics
    WHERE teacher_id = :user_id;

DELETE IGNORE FROM users
    WHERE id = :user_id;
