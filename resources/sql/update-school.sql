UPDATE IGNORE schools
    SET name = COALESCE(:name, name),
        address = COALESCE(:address, address)
    WHERE id = :school_id AND teacher_id = :user_id;

