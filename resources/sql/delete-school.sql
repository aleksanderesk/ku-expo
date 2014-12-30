DELETE IGNORE FROM schools
    WHERE id = :school_id AND teacher_id = :user_id;
