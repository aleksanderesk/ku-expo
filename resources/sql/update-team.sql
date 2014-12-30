UPDATE IGNORE teams
    SET name = COALESCE(:name, name),
        division = COALESCE(:division, division)
    WHERE id = :team_id AND teacher_id = :user_id;
