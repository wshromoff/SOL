
--
-- Change length of asset_name from 100 to 300 for MB_ACTIONS, MB_ASSETS
--
Alter table MB_ACTIONS MODIFY ASSET_NAME varchar2(300);

Alter table MB_ASSETS MODIFY ASSET_NAME varchar2(300);

--
-- Add rights management columns to MB_METADATA table
--
alter table MB_METADATA
add
(
    RIGHTS_MANAGEMENT VARCHAR2(100 BYTE),  
    RIGHTS_MANAGEMENT_STATUS VARCHAR2(100 BYTE)
);
