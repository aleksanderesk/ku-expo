INSERT IGNORE INTO teams
    (id, teacher_id, name, division, school_id) VALUES (NULL, :user_id, :name, :division, :school_id);
