-- DROP USER customer_360;

CREATE USER customer_360
IDENTIFIED BY WELcome123##
DEFAULT TABLESPACE data
TEMPORARY TABLESPACE temp
QUOTA UNLIMITED ON data;

GRANT create session, create table, create view TO customer_360;
GRANT graph_developer TO customer_360;

EXIT
