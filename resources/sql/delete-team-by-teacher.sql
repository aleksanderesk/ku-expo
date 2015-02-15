DELETE IGNORE FROM teams
    WHERE id = :team_id AND teacher_id = :user_id;
