--
-- This file contains synonyms that are put in place so reporting or governence queries don't reference a specific database schema
--
create public synonym assetrefs for mediabin_8_2.assetrefs; 
create public synonym assets for mediabin_8_2.assets; 
create public synonym revisions for mediabin_8_2.revisions; 
create public synonym containers for mediabin_8_2.containers; 
create public synonym searchablestrings for mediabin_8_2.searchablestrings; 
create public synonym searchablelongs for mediabin_8_2.searchablelongs; 
create public synonym jobhistorylog for mediabin_8_2.jobhistorylog; 
create public synonym auditlog for mediabin_8_2.auditlog; 

