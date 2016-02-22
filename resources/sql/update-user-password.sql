UPDATE IGNORE users
    SET password = :password
    WHERE username = :username
