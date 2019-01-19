--
-- Create Synonyms for J2C user so MB_* objects in the customers schema can be accessed without
-- the need to supply a schema identifier.
--
create or replace  synonym mb_assets for customers.mb_assets; 
create or replace  synonym mb_metadata for customers.mb_metadata; 
create or replace synonym mb_actions for customers.mb_actions; 
create or replace synonym MB_ACTIONS_SEQUENCE for customers.MB_ACTIONS_SEQUENCE; 
create or replace synonym MB_ASSETS_SEQUENCE for customers.MB_ASSETS_SEQUENCE; 
