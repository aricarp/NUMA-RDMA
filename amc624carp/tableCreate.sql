create table property (
    property_id varchar(8),
    property_name varchar(20),
    street_name varchar(20),
    street_number varchar(6),
    zip_code varchar(6),
    primary key (property_id)
);

create table appartments(
    property_id varchar(8),
    appartment_number varchar(4),
    lease_id varchar(8),
    number_bathrooms varchar(5),
    number_bedrooms varchar(5),
    for_sale numeric (1,0) check (for_sale>-1 and for_sale<2),
    primary key(property_id, appartment_number),
    foreign key (property_id) references property /*Added back in manualy*/
);

create table lease(
    lease_id varchar(8),
    tenant_id varchar(8),
    num_months varchar(3),
    security_deposit varchar(5),
    notice_period varchar(6),
    primary key(lease_id)
    foreign key(lease_id) references appartments
);

create table tenant (
    tenant_id varchar(8),
    tenant_first_name varchar(15),
    tenant_last_name varchar(15),
    primary key(tenant_id)
    /*Added foriegn key to lease table for tenant id manualy*/
);

create table payment (
    payment_id varchar(8),
    tenant_id varchar(8),
    amount varchar(8),
    primary key(payment_id)
);

create table credit_card (
    payment_id varchar(8),
    cc_number varchar (12),
    cc_exp_date date,
    security_code varchar(3),
    pay_time timestamp,
    primary key (cc_number, pay_time),
    foreign key (payment_id) references payment
);

create table venmo (
    payment_id varchar(8),
    sender_first_name varchar (10),
    sender_last_name varchar(10),
    reciever_first_name varchar(10),
    reciever_last_name varchar(10),
    message varchar(20),
    request_payment numeric(1,0) check (request_payment>-1 and request_payment<2),
    public_private  numeric(1,0) check (public_private>-1 and public_private<2),
    pay_time timestamp,
    primary key (payment_id, sender_first_name, pay_time),
    foreign key (payment_id) references payment
);

create table otherservices (
    payment_id varchar(8),
    tenant_ID varchar(8),
    property_id varchar(8),
    primary key (payment_id),
    foreign key (payment_id) references payment,
    foreign key (property_id) references property
); /*This table needs to relate to the other services somehow*/

create table visits(
    property_id varchar(8),
    appartment_number varchar (4),
    date_of_visit date,
    first_name_visitor varchar(10),
    last_name_visitor varchar(15),
    primary key (property_id, appartment_number, date_of_visit),
    foreign key (property_id) references property
);