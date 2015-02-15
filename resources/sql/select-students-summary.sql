SELECT S.id, S.name, U.id AS teacher_id, U.fullname AS teacher_name, S.division FROM students S
    INNER JOIN users U
        ON S.teacher_id = U.id
    ORDER BY S.name ASC;
