drop table if exists customer;
create table customer (
    _id bigint not null primary key,
    name varchar(128) not null,
    surname varchar(128) not null,
    tckn varchar(11) not null unique
);


-- assumptions
-- + a customer may have more than one wallet with the same currency
-- + If destination_type is IBAN; withdraw_active should be checked; when it's PAYMENT; not only withdraw_active but also shopping_active should be checked
drop table if exists wallet;
create table wallet (
    _id bigint auto_increment primary key,
    customer_id bigint not null,
    wallet_name varchar(512) not null,
    currency varchar(3), -- think about a check constraint here
    shopping_active boolean,
    withdraw_active boolean,
    balance numeric(20, 2),
    usable_balance numeric(20, 2),
    foreign key (customer_id) references customer(_id) on delete cascade
);
create index idx_wallet_customer_id ON wallet(customer_id);

drop table if exists transaction;
create table transaction (
    _id bigint auto_increment primary key,
    wallet_id bigint not null,
    amount numeric(20,2) not null,
    type varchar(16) not null,
    opposite_party varchar(32) not null,
    status varchar(16) not null,
    foreign key (wallet_id) references wallet(_id) on delete cascade
);

create index idx_transaction_wallet_id ON transaction(wallet_id);