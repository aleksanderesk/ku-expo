SELECT id, fullname AS name, username AS email, phone
    FROM users
    WHERE roles = '#{:ku-expo.handler/teacher}'
    ORDER BY fullname ASC;
