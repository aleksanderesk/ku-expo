INSERT IGNORE INTO users
   (id, fullname, username, phone_number, password, roles)
    VALUES (NULL, :fullname, :username, :phone_number, :password, :roles);
