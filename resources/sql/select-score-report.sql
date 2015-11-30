SELECT T.id, T.name AS team_name, K.name AS student_name, K.division, U.fullname AS teacher_name, S.name AS school_name, CT.score
    FROM competition_to_team CT
    INNER JOIN teams T
        ON CT.team_id = T.id
    LEFT JOIN student_to_team ST
        ON ST.team_id = T.id
    LEFT JOIN students K
        ON ST.student_id = K.id
    INNER JOIN users U
        ON U.id = T.teacher_id
    LEFT JOIN schools S
        ON S.id = T.school_id
    WHERE CT.comp_id = :comp_id
    ORDER BY CT.score DESC;
