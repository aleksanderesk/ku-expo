SELECT T.name AS team_name, T.division, CT.id, CT.score, C.name AS comp_name, O.name AS org_name FROM teams T 
    INNER JOIN competition_to_team CT 
        ON T.id = CT.team_id 
    INNER JOIN competitions C 
        on C.id = CT.comp_id 
    INNER JOIN orgs O 
        ON O.id = C.org_id 
    INNER JOIN user_to_org UO 
        ON O.id = UO.org_id 
    WHERE UO.user_id = :user_id AND division = :division
    ORDER BY T.name ASC;
