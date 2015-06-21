create table user (
  id int primary key auto_increment,
  name varchar(64) not null,
  age int,
  created_at timestamp not null,
  updated_at timestamp
);