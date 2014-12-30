INSERT IGNORE INTO schools
    (id, teacher_id, name, address) VALUES (NULL, :user_id, :name, :address);
