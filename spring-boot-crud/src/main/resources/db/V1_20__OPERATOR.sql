create table sale_document (id bigint not null, number varchar(255), total_price decimal(19,2), primary key (id))
create table sale_document_item (id bigint not null, lp integer, quantity integer, product_id bigint, sale_document_id bigint, primary key (id))
alter table sale_document_item add constraint FKpqnctwtitm0tmjc2o460mlrqd foreign key (product_id) references product
alter table sale_document_item add constraint FKl0et557hvo709wv3bnmnjt3jc foreign key (sale_document_id) references sale_document
