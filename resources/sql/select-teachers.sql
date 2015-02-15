SELECT id, fullname AS name FROM users
WHERE roles = '#{:ku-expo.handler/teacher}'
ORDER BY fullname ASC;
