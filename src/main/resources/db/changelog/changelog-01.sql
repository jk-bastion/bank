create table if not exists account
(
    id long  PRIMARY KEY,
    username varchar(40) NOT NULL,
    email    varchar(40) UNIQUE,
    currency_code  varchar(3),
    balance  varchar(240)
);

create table if not exists transaction
(
    id long PRIMARY KEY,
    fromAccountId long NOT NULL,
    toAccountId long NOT NULL,
    currencyCode  varchar(3),
    amount  varchar(240),
    status  varchar(20),
    message varchar(240),
    date timestamp  NOT NULL
);