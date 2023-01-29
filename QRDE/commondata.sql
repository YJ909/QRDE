CREATE TABLE commondata.ifsccodes (
	id int NOT NULL AUTO_INCREMENT,
	bankname VARCHAR(255), 
	ifsccode varchar(50),
    bankkey varchar(50),
	address VARCHAR(255),
	email varchar(255),
	phone varchar(15), 
	PRIMARY KEY (id)
);

ALTER TABLE commondata.ifsccodes AUTO_INCREMENT=1; 

CREATE TABLE commondata.transactions (
	id int NOT NULL AUTO_INCREMENT,
	ifsccode varchar(50),
    acctno varchar(50),
	amount varchar(50),
	txtype varchar(10),
    txdate datetime,
	PRIMARY KEY (id)
);

ALTER TABLE commondata.transactions AUTO_INCREMENT=1; 
  
select * from commondata.ifsccodes;
select * from commondata.transactions;

insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('ABC Bank','abc','ABC00001','INDIA','abc1@abc.com','');
insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('ABC Bank','abc''ABC00002','INDIA','abc2@abc.com','');
insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('ABC Bank','abc','ABC00003','INDIA','abc3@abc.com','');
insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('ABC Bank','abc','ABC00004','INDIA','abc4@abc.com','');
insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('ABC Bank','abc','ABC00005','INDIA','abc5@abc.com','');
 
insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('XYZ Bank','xyz','XYZ00001','INDIA','xyz1@abc.com','');
insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('XYZ Bank','xyz','XYZ00002','INDIA','xyz2@abc.com','');
insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('XYZ Bank','xyz','XYZ00003','INDIA','xyz3@abc.com','');
insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('XYZ Bank','xyz','XYZ00004','INDIA','xyz4@abc.com','');
insert into commondata.ifsccodes(bankname,bankkey,ifsccode,address,email,phone) values('XYZ Bank','xyz','XYZ00005','INDIA','xyz5@abc.com','');

select bankkey from commondata.ifsccodes where ifsccode = ''; 
insert into commondata.transactions(ifsccode,acctno,amount,txtype,txdate)
values('','','','',curdate());
