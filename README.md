# tmalljpa

# database version :  mysql-8.0.13

# tables:

DROP DATABASE IF EXISTS tmail_springboot;
CREATE DATABASE tmail_springboot DEFAULT CHARACTER SET utf8;

show databases;

use tmail_springboot;

show tables;

create table user_ (
id int(11) not null auto_increment,
name varchar(255) default null,
password varchar(255) default null,
salt varchar(255) default null,
primary key(id)
)engine = InnoDB default charset=utf8;


create table category_(
id int(11) not null auto_increment,
name varchar(255) default null,
primary key(id)
)engine = InnoDB default charset=utf8;

create table property_(
id int(11) not null auto_increment,
name varchar(255) default null,
cid int(11) default null,
primary key(id),
constraint fk_property_category foreign key(cid) references category_(id)
)engine = InnoDB default charset=utf8;

CREATE TABLE product_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  sub_title varchar(255) DEFAULT NULL,
  original_price decimal(12,2) DEFAULT NULL,
  promote_price decimal(12,2) DEFAULT NULL,
  stock int(11) DEFAULT NULL,
  cid int(11) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_product_category FOREIGN KEY (cid) REFERENCES category_ (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE property_value_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  pid int(11) DEFAULT NULL,
  ptid int(11) DEFAULT NULL,
  value varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_propertyvalue_property FOREIGN KEY (ptid) REFERENCES property_ (id),
  CONSTRAINT fk_propertyvalue_product FOREIGN KEY (pid) REFERENCES product_ (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE product_image_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  pid int(11) DEFAULT NULL,
  type varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_productimage_product FOREIGN KEY (pid) REFERENCES product_ (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE review_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  content varchar(4000) DEFAULT NULL,
  uid int(11) DEFAULT NULL,
  pid int(11) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_review_product FOREIGN KEY (pid) REFERENCES product_ (id),
  CONSTRAINT fk_review_user FOREIGN KEY (uid) REFERENCES user_ (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE order_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  order_code varchar(255) DEFAULT NULL,
  address varchar(255) DEFAULT NULL,
  post varchar(255) DEFAULT NULL,
  receiver varchar(255) DEFAULT NULL,
  mobile varchar(255) DEFAULT NULL,
  user_message varchar(255) DEFAULT NULL,
  create_date datetime DEFAULT NULL,
  pay_date datetime DEFAULT NULL,
  delivery_date datetime DEFAULT NULL,
  confirm_date datetime DEFAULT NULL,
  uid int(11) DEFAULT NULL,
  status varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_order_user FOREIGN KEY (uid) REFERENCES user_ (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE order_item_ (
  id int(11) NOT NULL AUTO_INCREMENT,
  pid int(11) DEFAULT NULL,
  oid int(11) DEFAULT NULL,
  uid int(11) DEFAULT NULL,
  number int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_orderitem_user FOREIGN KEY (uid) REFERENCES user_ (id),
  CONSTRAINT fk_orderitem_product FOREIGN KEY (pid) REFERENCES product_ (id),
  CONSTRAINT fk_orderitem_order FOREIGN KEY (oid) REFERENCES order_ (id)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
