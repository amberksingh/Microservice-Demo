-- check_users_table_exists.sql
SELECT COUNT(*)
FROM information_schema.tables
WHERE table_schema = 'ecommerce_user'
  AND table_name = 'users';
