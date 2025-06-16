create user boardtest2 identified by boardtest2
grant resource, connect to boardtest2
grant dba, connect to boardtest2
alter user boardtest2 default tablespace users
alter user boardtest2 temporary tablespace temp