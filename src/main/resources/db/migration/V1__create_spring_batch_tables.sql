-- src/main/resources/db/migration/V1__create_spring_batch_tables.sql

DROP TABLE IF EXISTS batch_job_execution_context;
DROP TABLE IF EXISTS batch_step_execution_context;
DROP TABLE IF EXISTS batch_step_execution;
DROP TABLE IF EXISTS batch_job_execution_params;
DROP TABLE IF EXISTS batch_job_execution;
DROP TABLE IF EXISTS batch_job_instance;

DROP SEQUENCE IF EXISTS batch_job_seq;
DROP SEQUENCE IF EXISTS batch_job_execution_seq;
DROP SEQUENCE IF EXISTS batch_step_execution_seq;

-- src/main/resources/db/migration/V1__create_spring_batch_tables.sql

-- Tabela BATCH_JOB_INSTANCE
CREATE TABLE IF NOT EXISTS batch_job_instance  (
    job_instance_id BIGINT  PRIMARY KEY ,
    version BIGINT,
    job_name VARCHAR(100) NOT NULL,
    job_key VARCHAR(32) NOT NULL,
    CONSTRAINT job_inst_un UNIQUE (job_name, job_key)
);

-- Tabela BATCH_JOB_EXECUTION
CREATE TABLE batch_job_execution  (
    job_execution_id BIGINT  PRIMARY KEY ,
    version BIGINT,
    job_instance_id BIGINT NOT NULL,
    create_time TIMESTAMP NOT NULL,
    start_time TIMESTAMP DEFAULT NULL,
    end_time TIMESTAMP DEFAULT NULL,
    status VARCHAR(10),
    exit_code VARCHAR(2500),
    exit_message VARCHAR(2500),
    last_updated TIMESTAMP,
    job_configuration_location VARCHAR(2500) NULL,
    CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id)
    REFERENCES batch_job_instance(job_instance_id)
);

-- Tabela BATCH_JOB_EXECUTION_PARAMS
CREATE TABLE batch_job_execution_params  (
    job_execution_id BIGINT NOT NULL,
    parameter_name VARCHAR(100) NOT NULL, -- Coluna corrigida
    parameter_type VARCHAR(100) NOT NULL,
    parameter_value VARCHAR(2500),
    identifying CHAR(1) NOT NULL,
    CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id)
    REFERENCES batch_job_execution(job_execution_id)
);

-- Tabela BATCH_STEP_EXECUTION
CREATE TABLE batch_step_execution  (
    STEP_EXECUTION_ID BIGINT  PRIMARY KEY ,
    VERSION BIGINT NOT NULL,
    STEP_NAME VARCHAR(100) NOT NULL,
    JOB_EXECUTION_ID BIGINT NOT NULL,
    CREATE_TIME TIMESTAMP NOT NULL,
    START_TIME TIMESTAMP DEFAULT NULL,
    END_TIME TIMESTAMP DEFAULT NULL,
    STATUS VARCHAR(10),
    COMMIT_COUNT BIGINT,
    READ_COUNT BIGINT,
    FILTER_COUNT BIGINT,
    WRITE_COUNT BIGINT,
    READ_SKIP_COUNT BIGINT,
    WRITE_SKIP_COUNT BIGINT,
    PROCESS_SKIP_COUNT BIGINT,
    ROLLBACK_COUNT BIGINT,
    EXIT_CODE VARCHAR(2500),
    EXIT_MESSAGE VARCHAR(2500),
    LAST_UPDATED TIMESTAMP,
    CONSTRAINT JOB_EXEC_STEP_FK FOREIGN KEY (JOB_EXECUTION_ID)
    REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- Tabela BATCH_STEP_EXECUTION_CONTEXT
CREATE TABLE batch_step_execution_context  (
    STEP_EXECUTION_ID BIGINT PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT,
    CONSTRAINT STEP_EXEC_CTX_FK FOREIGN KEY (STEP_EXECUTION_ID)
    REFERENCES BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
);

-- Tabela BATCH_JOB_EXECUTION_CONTEXT
CREATE TABLE batch_job_execution_context  (
    JOB_EXECUTION_ID BIGINT PRIMARY KEY,
    SHORT_CONTEXT VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT,
    CONSTRAINT JOB_EXEC_CTX_FK FOREIGN KEY (JOB_EXECUTION_ID)
    REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- Sequências para geração de IDs
CREATE SEQUENCE batch_job_seq;
CREATE SEQUENCE batch_job_execution_seq;
CREATE SEQUENCE batch_step_execution_seq;