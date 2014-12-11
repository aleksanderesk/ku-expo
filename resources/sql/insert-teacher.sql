INSERT IGNORE INTO teachers
   (id, name, email, phone_number, password, salt)
    VALUES (NULL, :name, :email, :phone_number, :password, :salt);
