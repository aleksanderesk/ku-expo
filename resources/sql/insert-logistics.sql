INSERT IGNORE INTO logistics
    (id, teacher_id, num_cars, num_buses, attend_opening, leave_early, leave_time, num_lunches, xsmall_ts, small_ts, medium_ts, large_ts, xlarge_ts) VALUES (NULL, :user_id, :num_cars, :num_buses, :attend_opening, :leave_early, :leave_time, :num_lunches, :xsmall_ts, :small_ts, :medium_ts, :large_ts, :xlarge_ts);
