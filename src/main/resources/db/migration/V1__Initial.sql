
     create table comment
     (
         id  bigserial not null,
         created_date timestamp,
         text varchar(255),
         post_id bigint,
         user_id bigint,
         primary key (id)
     );


     create table post (
         post_id  bigserial not null,
         created_date timestamp,
         description oid,
         post_name varchar(255),
         url varchar(255),
         vote_count int,
         id bigint, user_id bigint,
         primary key (post_id)
    );


     create table refresh_token
     (
         id  bigserial not null,
         created_date timestamp,
         token varchar(255),
         primary key (id)
    );



     create table theme
     (
        id  bigserial not null,
        created_date timestamp,
        description varchar(255),
        name varchar(255),
        user_user_id bigint,
        primary key (id)
     );


     create table theme_posts
     (
         theme_id bigint not null,
         posts_post_id bigint not null
     );


     create table token
     (
         id bigserial not null,
         expiry_date timestamp,
         token varchar(255),
         user_user_id bigint,
         primary key (id)
     );


     create table users
     (
         user_id  bigserial not null,
         created timestamp,
         email varchar(255),
         enabled boolean not null,
         password varchar(255),
         username varchar(255),
         primary key (user_id)
     );


     create table vote
     (
         vote_id  bigserial not null,
         vote_type int,
         post_id bigint not null,
         user_id bigint,
         primary key (vote_id)
     );


     alter table if exists theme_posts add constraint theme_c unique (posts_post_id);

     alter table if exists comment add constraint com_c foreign key (post_id) references post;

     alter table if exists comment add constraint com2_c foreign key (user_id) references users;

     alter table if exists post add constraint post_c foreign key (id) references theme;

     alter table if exists post add constraint post2_c foreign key (user_id) references users;

     alter table if exists theme add constraint theme2_c foreign key (user_user_id) references users;

     alter table if exists theme_posts add constraint themep_c foreign key (posts_post_id) references post;

     alter table if exists theme_posts add constraint themep2_c foreign key (theme_id) references theme;

     alter table if exists token add constraint token_c foreign key (user_user_id) references users;

     alter table if exists vote add constraint vote_c foreign key (post_id) references post;

     alter table if exists vote add constraint vote2_C foreign key (user_id) references users;
