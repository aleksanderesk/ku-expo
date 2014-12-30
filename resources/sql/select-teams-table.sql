SELECT F.id, F.teacher_id, F.name, F.division, F.student_id, F.comp_id, U.name AS student_name, O.name AS comp_name FROM
    (SELECT T.id, T.teacher_id, T.name, T.division, S.student_id, C.comp_id FROM teams T
        LEFT JOIN student_to_team S 
            ON S.team_id = T.id
        LEFT JOIN competition_to_team C 
            on C.team_id = T.id
        WHERE T.teacher_id = :user_id) AS F
    LEFT JOIN students U
        ON U.id = F.student_id
    LEFT JOIN competitions O
        ON O.id = F.comp_id
    WHERE F.teacher_id = :user_id;
