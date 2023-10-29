CREATE TABLE tb_doctor (
    id SERIAL PRIMARY KEY,
    crm VARCHAR(9) NOT NULL UNIQUE,
    speciality VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL,

    id_user INT REFERENCES tb_user(id) UNIQUE
);