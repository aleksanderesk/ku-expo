SELECT K.id, K.name, K.division, S.name AS school, T.name AS team_name, U.fullname AS teacher_name, C.name AS comp_name 
    FROM students K 
    INNER JOIN users U 
        ON U.id = K.teacher_id 
    INNER JOIN student_to_team ST 
        ON ST.student_id = K.id
    INNER JOIN teams T 
        ON T.id = ST.team_id 
    INNER JOIN competition_to_team CT 
        ON CT.team_id = T.id 
    INNER JOIN competitions C 
        ON C.id = CT.comp_id 
    INNER JOIN schools S 
        ON S.teacher_id = U.id
    WHERE U.id = 138
    ORDER BY S.name, K.division, K.name 
    LIMIT 100;
