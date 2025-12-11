SELECT COUNT(*) > 0
FROM users
WHERE email = ?
  AND user_id <> COALESCE(?, -1);

