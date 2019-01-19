--
-- These statements should only be used in a situation where the customer profile is to have the database
-- tables cleared and sequences reset back to 1.
--
-- An example could be in DEVN or INTN after a production copy back has been performed from PRDN
--
truncate table MB_ACTIONS drop storage;
truncate table MB_METADATA drop storage;
alter table MB_METADATA DISABLE constraint fk_ID;	-- Before truncating MB_ASSETS, need to disable foreign key constraint
truncate table MB_ASSETS drop storage;
alter table MB_METADATA ENABLE constraint fk_ID;	-- Enable again the foreign key constraint

drop sequence MB_ACTIONS_SEQUENCE;
drop sequence MB_ASSETS_SEQUENCE;

create sequence MB_ACTIONS_SEQUENCE 
start with 1 
increment by 1 
nocache
nomaxvalue;

create sequence MB_ASSETS_SEQUENCE 
start with 1 
increment by 1 
nocache
nomaxvalue;

grant select on MB_ACTIONS_SEQUENCE to J2C;
grant select on MB_ASSETS_SEQUENCE to J2C;

