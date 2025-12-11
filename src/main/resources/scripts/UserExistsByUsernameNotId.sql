SELECT COUNT(*) > 0
FROM users
WHERE username = ?
  AND user_id <> COALESCE(?, -1);

