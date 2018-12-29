
-- From this URL
--  https://blogs.oracle.com/sql/how-to-create-users-grant-them-privileges-and-remove-them-in-oracle-database

-- Grant create table role to jemm2
grant create table to jemm2;

-- Need to grant rights to tablespace
-- Find users table space
select default_tablespace from dba_users
where  username = 'jemm2';

-- Actually alter jemm2 user
alter user jemm2 quota unlimited on jemm2;


-- More needed grants for user
grant create view, create procedure, create sequence to jemm2;


