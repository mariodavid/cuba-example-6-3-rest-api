alter table CESDRA_CUSTOMER add column CUSTOMER_TYPE varchar(50) ^
update CESDRA_CUSTOMER set CUSTOMER_TYPE = 'NEW' where CUSTOMER_TYPE is null ;
alter table CESDRA_CUSTOMER alter column CUSTOMER_TYPE set not null ;
