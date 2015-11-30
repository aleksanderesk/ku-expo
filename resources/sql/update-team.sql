UPDATE IGNORE teams
    SET name = COALESCE(:name, name),
        division = COALESCE(:division, division),
        school_id = COALESCE(:school_id, school_id)
    WHERE id = :team_id AND teacher_id = :user_id;
