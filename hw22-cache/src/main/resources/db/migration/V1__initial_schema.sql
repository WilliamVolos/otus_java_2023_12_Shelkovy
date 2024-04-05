create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50)
);

create sequence address_SEQ start with 1 increment by 1;

create table address
(
    id   bigint not null primary key,
    street varchar(500),
    client_id bigint not null unique
);

create sequence phones_SEQ start with 1 increment by 1;

create table phones
(
    id   bigint not null primary key,
    number varchar(15),
    client_id bigint not null
);

alter table address add constraint address_client_fk
    foreign key (client_id) references client;

alter table phones add constraint phones_client_fk
    foreign key (client_id) references client;
