-- we don't know how to generate root <with-no-name> (class Root) :(
# grant alter, alter routine, application_password_admin, audit_admin, authentication_policy_admin, backup_admin, binlog_admin, binlog_encryption_admin, clone_admin, connection_admin, create, create role, create routine, create tablespace, create temporary tables, create user, create view, delete, drop, drop role, encryption_key_admin, event, execute, file, flush_optimizer_costs, flush_status, flush_tables, flush_user_resources, group_replication_admin, group_replication_stream, index, innodb_redo_log_archive, innodb_redo_log_enable, insert, lock tables, passwordless_user_admin, persist_ro_variables_admin, process, references, reload, replication client, replication slave, replication_applier, replication_slave_admin, resource_group_admin, resource_group_user, role_admin, select, service_connection_admin, session_variables_admin, set_user_id, show databases, show view, show_routine, shutdown, super, system_user, system_variables_admin, table_encryption_admin, trigger, update, xa_recover_admin, grant option on *.* to 'debian-sys-maint'@localhost;
#
# grant select, system_user on *.* to 'mysql.infoschema'@localhost;
#
# grant backup_admin, clone_admin, connection_admin, persist_ro_variables_admin, session_variables_admin, shutdown, super, system_user, system_variables_admin on *.* to 'mysql.session'@localhost;
#
# grant system_user on *.* to 'mysql.sys'@localhost;
#
# grant alter, alter routine, application_password_admin, audit_admin, authentication_policy_admin, backup_admin, binlog_admin, binlog_encryption_admin, clone_admin, connection_admin, create, create role, create routine, create tablespace, create temporary tables, create user, create view, delete, drop, drop role, encryption_key_admin, event, execute, file, flush_optimizer_costs, flush_status, flush_tables, flush_user_resources, group_replication_admin, group_replication_stream, index, innodb_redo_log_archive, innodb_redo_log_enable, insert, lock tables, passwordless_user_admin, persist_ro_variables_admin, process, references, reload, replication client, replication slave, replication_applier, replication_slave_admin, resource_group_admin, resource_group_user, role_admin, select, service_connection_admin, session_variables_admin, set_user_id, show databases, show view, show_routine, shutdown, super, system_user, system_variables_admin, table_encryption_admin, trigger, update, xa_recover_admin on *.* to root;
#
# grant alter, alter routine, application_password_admin, audit_admin, authentication_policy_admin, backup_admin, binlog_admin, binlog_encryption_admin, clone_admin, connection_admin, create, create role, create routine, create tablespace, create temporary tables, create user, create view, delete, drop, drop role, encryption_key_admin, event, execute, file, flush_optimizer_costs, flush_status, flush_tables, flush_user_resources, group_replication_admin, group_replication_stream, index, innodb_redo_log_archive, innodb_redo_log_enable, insert, lock tables, passwordless_user_admin, persist_ro_variables_admin, process, references, reload, replication client, replication slave, replication_applier, replication_slave_admin, resource_group_admin, resource_group_user, role_admin, select, service_connection_admin, session_variables_admin, set_user_id, show databases, show view, show_routine, shutdown, super, system_user, system_variables_admin, table_encryption_admin, trigger, update, xa_recover_admin, grant option on *.* to root@localhost;

create table assistanceRequest
(
    assistanceID   varchar(50)    not null
        primary key,
    userID         varchar(50)    null,
    accountID      varchar(50)    null,
    payeeAccountID varchar(50)    null,
    functionID     int            null,
    amount         decimal(10, 2) null,
    assistanceDate datetime       null,
    isFinished     int            null
);

create table bankAccount
(
    accountID       varchar(50)    not null
        primary key,
    userID          varchar(50)    null,
    openDate        datetime       null,
    openMoney       decimal(10, 2) null,
    accountPassword varchar(50)    null,
    balance         decimal(10, 2) null,
    isReportLoss    int            null
);

create table conciergeInfo
(
    conciergeID       varchar(50) not null
        primary key,
    bookTime          datetime    null,
    conciergeName     varchar(50) null,
    gender            int         null,
    age               int         null,
    conciergeLanguage varchar(50) null,
    personalStatement varchar(50) null
);

create table systemFunction
(
    functionID          int         not null
        primary key,
    functionName        varchar(50) null,
    functionDescription varchar(50) null,
    isUtility           int         null
);

create table tradeRecord
(
    recordID       varchar(50)    not null
        primary key,
    accountID      varchar(50)    null,
    functionID     int            null,
    tradeAmount    decimal(10, 2) null,
    tradeTime      datetime       null,
    payeeAccountID varchar(50)    null
);

create table user
(
    userID       varchar(50) not null
        primary key,
    userPassword varchar(50) null,
    userName     varchar(50) null,
    gender       varchar(50) null,
    age          int         null,
    isValid      int         null
);

create table userConcierge
(
    userID      varchar(50) not null,
    conciergeID varchar(50) not null,
    primary key (userID, conciergeID)
);

create table userUnity
(
    userID            varchar(50) not null
        primary key,
    unityID           varchar(50) null,
    authorization     int         null,
    unityRelationship varchar(50) null
);

create table utilityAccount
(
    accountID      varchar(50)    not null,
    functionID     int            not null,
    userID         varchar(50)    null,
    utilityBalance decimal(10, 2) null,
    primary key (accountID, functionID)
);

