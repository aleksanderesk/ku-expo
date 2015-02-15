SELECT S.id, S.name, U.id as teacher_id, U.fullname AS teacher_name, S.address FROM schools S 
    INNER JOIN users U
        ON S.teacher_id = U.id
    ORDER BY S.name ASC;
