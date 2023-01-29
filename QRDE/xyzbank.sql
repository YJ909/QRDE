CREATE TABLE xyzbank.customer (
	customer_id int NOT NULL AUTO_INCREMENT,
	title varchar(20),
	CustomerName VARCHAR(255), 
	gender varchar(50),
	address VARCHAR(255),
	email varchar(255),
	mobile varchar(15), 
	dob varchar(50),  
	aadhar_no varchar(15),
    isInternet varchar(5),
    pwd varchar(255),
	PRIMARY KEY (customer_id)
);

ALTER TABLE xyzbank.customer AUTO_INCREMENT=200000; 

CREATE TABLE xyzbank.customer_id (
	customer_id int NOT NULL,
	account_type_savings varchar(10) NOT NULL,
	account_type_current varchar(10) NOT NULL,
	acoount_type_loan varchar(10) NOT NULL,
	PRIMARY KEY (customer_id),
	FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

DELIMITER $$ 
CREATE TRIGGER xyzbank.after_customer_insert
AFTER INSERT
ON xyzbank.customer FOR EACH ROW
BEGIN
        INSERT INTO xyzbank.customer_id(customer_id, account_type_savings, account_type_current, acoount_type_loan)
        VALUES(new.customer_id, 'Y', 'N', 'N');
END$$
DELIMITER ;

CREATE TABLE xyzbank.accounts (
	account_no int(11) NOT NULL AUTO_INCREMENT,
	customer_id int NOT NULL,
	account_type varchar(10) NOT NULL,
    ifsc_code varchar(10) NOT NULL,
	balance int(11) NOT NULL,
	PRIMARY KEY (account_no),
	FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

ALTER TABLE xyzbank.accounts AUTO_INCREMENT=20000000; 

DELIMITER $$ 
CREATE TRIGGER xyzbank.after_customer_id_insert
AFTER INSERT
ON xyzbank.customer FOR EACH ROW
BEGIN
        INSERT INTO xyzbank.accounts(customer_id, account_type, balance, ifsc_code)
        VALUES(new.customer_id, 'S', 0, 'XYZ00001');
END$$
DELIMITER ;	

CREATE TABLE xyzbank.transactions (
	id int NOT NULL AUTO_INCREMENT,
	acctno varchar(50),
	amount varchar(50),
	comments varchar(100),
    txtype varchar(10),
    txdate datetime,
    txstatus varchar(5),
	PRIMARY KEY (id)
);

ALTER TABLE xyzbank.transactions AUTO_INCREMENT=1; 

insert into xyzbank.customer(CustomerName, gender, title) values('James Bond', 'M', 'Mr');
insert into xyzbank.customer(CustomerName, gender, title) values('Tony Stark', 'M', 'Mr');
insert into xyzbank.customer(CustomerName, gender, title) values('Bruce Banner', 'M', 'Mr');
insert into xyzbank.customer(CustomerName, gender, title) values('Mary Poppins', 'F', 'Ms');
insert into xyzbank.customer(CustomerName, gender, title) values('Diana Spencer', 'F', 'Ms');
insert into xyzbank.customer(CustomerName, gender, title) values('Judy Dench', 'F', 'Ms');
insert into xyzbank.customer(CustomerName, gender, title) values('Steve Rogers', 'M', 'Mr');
insert into xyzbank.customer(CustomerName, gender, title) values('Chris Evans', 'M', 'Mr');
insert into xyzbank.customer(CustomerName, gender, title) values('Steven Strange', 'M', 'Dr');
insert into xyzbank.customer(CustomerName, gender, title) values('Ameer Khan', 'M', 'Mr');

select * from xyzbank.customer;
select * from xyzbank.customer_id;
select * from xyzbank.accounts;
select * from xyzbank.transactions;

select customer_id customerID  from xyzbank.customer a where a.isInternet is null;
select CustomerName from xyzbank.customer a where a.customer_id = '100004';
select concat(CustomerName,':',account_no,':',ifsc_code) Details from xyzbank.customer a inner join xyzbank.accounts b on a.customer_id = b.customer_id where a.customer_id = '100004';  
update xyzbank.customer a set a.email = '', a.mobile = '', a.pwd = '', a.isInternet = 1 where customer_id = '100004';
select balance from xyzbank.accounts where account_no = '';
select balance from xyzbank.accounts where account_no = '100004';
insert into xyzbank.transactions(acctno,amount,comments,txtype,txdate) values(,,,,curdate());
UPDATE xyzbank.accounts SET balance=4900 WHERE account_number='10000000';
select id,txtype,amount,txdate,comments from xyzbank.transactions where acctno = '';
select max(id) from xyzbank.transactions;
update xyzbank.transactions set txstatus = '1' where id = '';
select id,txtype,amount,txdate,comments from xyzbank.customer a 
inner join xyzbank.accounts b on a.customer_id = b.customer_id 
inner join xyzbank.transactions c on b.account_no = c.acctno  
where a.customer_id = '100004';  
