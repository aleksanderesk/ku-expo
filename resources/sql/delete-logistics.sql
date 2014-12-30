DELETE IGNORE FROM logistics
    WHERE id = :logistics_id AND teacher_id = :user_id;
