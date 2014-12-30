INSERT IGNORE INTO users
   (id, fullname, username, phone, password, roles)
    VALUES (NULL, :fullname, :username, :phone, :password, :roles);
