create table users (
  id bigint not null primary key,
  name varchar(64) not null,
  age int,
  created_at timestamp not null,
  updated_at timestamp,
  deleted_at timestamp
);