SELECT
    (SELECT COUNT(*) FROM schools) AS num_schools,
    (SELECT COUNT(*) FROM students) AS num_students,
    (SELECT COUNT(*) FROM teams) AS num_teams,
    SUM(attend_opening) AS attending_opening,
    SUM(leave_early) AS leaving_early, 
    SUM(num_cars) AS num_cars, 
    SUM(num_buses) AS num_buses, 
    SUM(num_lunches) AS num_lunches,
    SUM(xsmall_ts) AS xsmall_ts,
    SUM(small_ts) AS small_ts,
    SUM(medium_ts) AS medium_ts,
    SUM(large_ts) AS large_ts,
    SUM(xlarge_ts) AS xlarge_ts
    FROM logistics;
