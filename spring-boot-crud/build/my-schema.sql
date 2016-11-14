create sequence hibernate_sequence start with 1 increment by 1
create table auction (id bigint not null, created_date timestamp, creator varchar(255), current_price decimal(19,2), current_winer varchar(255), description varchar(255) not null, expire_date timestamp, last_modified_date timestamp, min_price decimal(19,2) not null, modifier varchar(255), name varchar(255) not null, owner varchar(255), primary key (id))
create table product (id bigint not null, description varchar(255), name varchar(255), price decimal(19,2), quantity integer, primary key (id))
create table sale_document (id bigint not null, number varchar(255), total_price decimal(19,2), primary key (id))
create table sale_document_item (id bigint not null, lp integer, quantity integer, product_id bigint, sale_document_id bigint, primary key (id))
alter table sale_document_item add constraint FKpqnctwtitm0tmjc2o460mlrqd foreign key (product_id) references product
alter table sale_document_item add constraint FKl0et557hvo709wv3bnmnjt3jc foreign key (sale_document_id) references sale_document
