SELECT K.id, K.name, K.division, T.name AS team_name, U.fullname AS teacher_name, C.name AS comp_name 
    FROM students K 
    INNER JOIN users U 
        ON U.id = K.teacher_id 
    LEFT JOIN student_to_team ST 
        ON ST.student_id = K.id
    LEFT JOIN teams T 
        ON T.id = ST.team_id 
    LEFT JOIN competition_to_team CT 
        ON CT.team_id = T.id 
    LEFT JOIN competitions C 
        ON C.id = CT.comp_id 
    WHERE K.division = :division;
