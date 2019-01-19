--
-- These statements should only be used in a situation where the customer profile is to be fully 'Started Over'
-- or installed from scratch.
--

drop table MB_ACTIONS cascade constraints purge;

drop table MB_ASSETS cascade constraints purge;

drop table MB_METADATA cascade constraints purge;

-- Next delete sequences

drop sequence MB_ACTIONS_SEQUENCE;

drop sequence MB_ASSETS_SEQUENCE;




