UPDATE IGNORE competition_to_team
    SET score = COALESCE(:score, score)
    WHERE id = :comp_to_team_id;
