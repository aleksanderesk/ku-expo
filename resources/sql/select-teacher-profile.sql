SELECT id, fullname AS name, username AS email, phone from users
    WHERE id = :user_id;
