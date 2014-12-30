UPDATE IGNORE logistics
    SET num_cars = COALESCE(:num_cars, num_cars),
        num_buses = COALESCE(:num_buses, num_buses),
        attend_opening = COALESCE(:attend_opening, attend_opening),
        leave_early = COALESCE(:leave_early, leave_early),
        leave_time = COALESCE(:leave_time, leave_time),
        num_lunches = COALESCE(:num_lunches, num_lunches),
        xsmall_ts = COALESCE(:xsmall_ts, xsmall_ts),
        small_ts = COALESCE(:small_ts, xsmall_ts),
        medium_ts = COALESCE(:medium_ts, medium_ts),
        large_ts = COALESCE(:large_ts, large_ts),
        xlarge_ts = COALESCE(:xlarge_ts, xlarge_ts)
    WHERE id = :logistics_id AND teacher_id = :user_id;
