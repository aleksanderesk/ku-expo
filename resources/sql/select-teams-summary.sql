SELECT F.id, F.name, U.id AS teacher_id, U.fullname AS teacher_name, F.division, F.student_id, F.comp_id, S.name AS student_name, O.name AS comp_name FROM 
    (SELECT T.id, T.teacher_id, T.name, T.division, S.student_id, C.comp_id FROM teams T
        LEFT JOIN student_to_team S
            ON S.team_id = T.id
        LEFT JOIN competition_to_team C
            ON C.team_id = T.id) AS F
    LEFT JOIN students S
        ON S.id = F.student_id
    LEFT JOIN competitions O
        ON O.id = F.comp_id
    INNER JOIN users U
        ON U.id = F.teacher_id;
