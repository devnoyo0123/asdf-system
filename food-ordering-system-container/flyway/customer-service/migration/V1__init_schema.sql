create table if not exists customer.customer_tbl
(
    id       uuid not null
        primary key,
    details  varchar(255),
    name     varchar(255),
    phone    varchar(255),
    street   varchar(255),
    zip_code varchar(255)
);