create table if not exists account
(
    id long primary key
    username varchar
    email    varchar
    currencyCode  varchar(3)
    balance  varchar(240)
);

create table if not exists transaction
(
    id long primary key
    fromAccountId long not null
    toAccountId long not null
    currencyCode  varchar(3)
    amount  varchar(240)
    status  varchar(20)
    message varchar(240)
    date timestamp  not null
    );

-- todo: verify sieze , add foreng key, add index
-- create index idx_user_id on account (id);
