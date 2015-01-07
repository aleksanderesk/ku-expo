SELECT U.id, U.fullname AS name, U.username as email, O.name AS organization FROM users U
    INNER JOIN user_to_org UO
        ON U.id = UO.user_id
    INNER JOIN orgs O
        ON O.id = UO.org_id
    WHERE U.id = :user_id;
