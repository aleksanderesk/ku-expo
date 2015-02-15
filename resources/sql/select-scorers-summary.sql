SELECT  U.id, U.fullname AS name, U.username AS email, U.phone, 
        O.id AS org_id, O.name AS org_name, 
        C.id AS comp_id, C.name AS comp_name 
        FROM users U 
        INNER JOIN user_to_org UO 
            ON U.id = UO.user_id 
        INNER JOIN orgs O 
            ON O.id = UO.org_id 
        INNER JOIN competitions C 
            ON UO.org_id = C.org_id 
        WHERE U.roles = '#{:ku-expo.handler/group}';
