create table users (
    id bigserial primary key,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    nickname varchar(255) not null,
    role varchar(20) not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table product_post (
    id bigserial primary key,
    owner_user_id bigint not null references users(id),
    title varchar(255) not null,
    category varchar(255),
    description text,
    listed_price integer,
    condition_description text,
    defect_description text,
    refund_policy_text text,
    trade_location_text varchar(255),
    delivery_available boolean not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table conversation (
    id bigserial primary key,
    product_post_id bigint not null references product_post(id),
    owner_user_id bigint not null references users(id),
    title varchar(255) not null,
    status varchar(20) not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table chat_message (
    id bigserial primary key,
    conversation_id bigint not null references conversation(id),
    sender_role varchar(20) not null,
    content text not null,
    sent_at timestamp not null,
    message_sequence integer not null,
    created_at timestamp not null
);

create table trade_condition_snapshot (
    id bigserial primary key,
    conversation_id bigint not null references conversation(id),
    price integer,
    place varchar(255),
    trade_time_text varchar(255),
    trade_method varchar(30),
    delivery_fee_policy varchar(255),
    payment_method varchar(30),
    product_condition text,
    defect_details text,
    refund_policy text,
    negotiation_policy varchar(255),
    included_items varchar(255),
    confidence_score double precision,
    source_message_ids varchar(255),
    created_at timestamp not null
);

create table analysis_alert (
    id bigserial primary key,
    conversation_id bigint not null references conversation(id),
    alert_type varchar(50) not null,
    severity varchar(20) not null,
    field_name varchar(255),
    message text not null,
    before_value text,
    after_value text,
    source_message_ids varchar(255),
    resolved boolean not null default false,
    created_at timestamp not null
);

create table evidence_package (
    id bigserial primary key,
    conversation_id bigint not null references conversation(id),
    file_name varchar(255) not null,
    file_path varchar(500) not null,
    sha256_hash varchar(64) not null,
    generated_at timestamp not null
);

create table ai_analysis_log (
    id bigserial primary key,
    conversation_id bigint not null references conversation(id),
    provider varchar(50),
    request_summary text,
    response_summary text,
    status varchar(50),
    created_at timestamp not null
);
