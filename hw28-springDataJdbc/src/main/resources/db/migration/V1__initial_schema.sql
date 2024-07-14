create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table address
(
    client_id bigint not null unique,
    street varchar(500)
);

create table phones
(
    id     bigserial not null primary key,
    number varchar(15),
    client_id bigint not null
);

alter table address add constraint address_client_fk
    foreign key (client_id) references client;

alter table phones add constraint phones_client_fk
    foreign key (client_id) references client;
