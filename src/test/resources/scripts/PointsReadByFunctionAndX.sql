SELECT * FROM computed_points WHERE function_id = ? AND ABS(x_value - ?) < 0.0001 LIMIT 1;

