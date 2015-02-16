INSERT IGNORE INTO user_to_org
    (id, user_id, org_id) VALUES (NULL, :user_id, :org_id);
