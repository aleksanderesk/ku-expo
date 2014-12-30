INSERT IGNORE INTO student_to_team
    (id, student_id, team_id) VALUES (NULL, :student_id, :team_id);
