CREATE TABLE abcbank.customer (
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

ALTER TABLE abcbank.customer AUTO_INCREMENT=100000; 

CREATE TABLE abcbank.customer_id (
	customer_id int NOT NULL,
	account_type_savings varchar(10) NOT NULL,
	account_type_current varchar(10) NOT NULL,
	acoount_type_loan varchar(10) NOT NULL,
	PRIMARY KEY (customer_id),
	FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

DELIMITER $$ 
CREATE TRIGGER abcbank.after_customer_insert
AFTER INSERT
ON abcbank.customer FOR EACH ROW
BEGIN
        INSERT INTO abcbank.customer_id(customer_id, account_type_savings, account_type_current, acoount_type_loan)
        VALUES(new.customer_id, 'Y', 'N', 'N');
END$$
DELIMITER ;

CREATE TABLE abcbank.accounts (
	account_no int(11) NOT NULL AUTO_INCREMENT,
	customer_id int NOT NULL,
	account_type varchar(10) NOT NULL,
    ifsc_code varchar(10) NOT NULL,
	balance int(11) NOT NULL,
	PRIMARY KEY (account_no),
	FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

ALTER TABLE abcbank.accounts AUTO_INCREMENT=10000000; 

DELIMITER $$ 
CREATE TRIGGER abcbank.after_customer_id_insert
AFTER INSERT
ON abcbank.customer FOR EACH ROW
BEGIN
        INSERT INTO abcbank.accounts(customer_id, account_type, balance, ifsc_code)
        VALUES(new.customer_id, 'S', 0, 'ABC00001');
END$$
DELIMITER ;	

CREATE TABLE abcbank.transactions (
	id int NOT NULL AUTO_INCREMENT,
	acctno varchar(50),
	amount varchar(50),
	comments varchar(100),
    txtype varchar(10),
    txdate datetime,
    txstatus varchar(5),
	PRIMARY KEY (id)
);

ALTER TABLE abcbank.transactions AUTO_INCREMENT=1; 

insert into abcbank.customer(CustomerName, gender, title) values('James Bond', 'M', 'Mr');
insert into abcbank.customer(CustomerName, gender, title) values('Tony Stark', 'M', 'Mr');
insert into abcbank.customer(CustomerName, gender, title) values('Bruce Banner', 'M', 'Mr');
insert into abcbank.customer(CustomerName, gender, title) values('Mary Poppins', 'F', 'Ms');
insert into abcbank.customer(CustomerName, gender, title) values('Diana Spencer', 'F', 'Ms');
insert into abcbank.customer(CustomerName, gender, title) values('Judy Dench', 'F', 'Ms');
insert into abcbank.customer(CustomerName, gender, title) values('Steve Rogers', 'M', 'Mr');
insert into abcbank.customer(CustomerName, gender, title) values('Chris Evans', 'M', 'Mr');
insert into abcbank.customer(CustomerName, gender, title) values('Steven Strange', 'M', 'Dr');
insert into abcbank.customer(CustomerName, gender, title) values('Ameer Khan', 'M', 'Mr');

select * from abcbank.customer;
select * from abcbank.customer_id;
select * from abcbank.accounts;
select * from abcbank.transactions;

select customer_id customerID  from abcbank.customer a where a.isInternet is null;
select CustomerName from abcbank.customer a where a.customer_id = '100004';
select concat(CustomerName,':',account_no,':',ifsc_code) Details from abcbank.customer a inner join abcbank.accounts b on a.customer_id = b.customer_id where a.customer_id = '100004';  
update abcbank.customer a set a.email = '', a.mobile = '', a.pwd = '', a.isInternet = 1 where customer_id = '100004';
select balance from abcbank.accounts where account_no = '';
select balance from abcbank.accounts where account_no = '100004';
insert into abcbank.transactions(acctno,amount,comments,txtype,txdate) values(,,,,curdate());
UPDATE abcbank.accounts SET balance=4900 WHERE account_number='10000000';
select id,txtype,amount,txdate,comments from abcbank.transactions where acctno = '';
select max(id) from abcbank.transactions;
update abcbank.transactions set txstatus = '1' where id = '';
select id,txtype,amount,txdate,comments from abcbank.customer a 
inner join abcbank.accounts b on a.customer_id = b.customer_id 
inner join abcbank.transactions c on b.account_no = c.acctno  
where a.customer_id = '100004';  
