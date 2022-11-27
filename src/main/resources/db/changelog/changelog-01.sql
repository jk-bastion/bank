create table if not exists account
(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username varchar(40) NOT NULL,
    email    varchar(40) NOT NULL UNIQUE,
    currencyCode  varchar(3),
    balance  varchar(240)
);

create table if not exists transaction
(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fromAccountId INT NOT NULL,
    toAccountId INT NOT NULL,
    currencyCode  varchar(3),
    amount  varchar(240),
    status  varchar(20),
    message varchar(240),
    date timestamp  NOT NULL
);

create index idx_transaction_from_account_id on transaction (fromAccountId);