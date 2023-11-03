CREATE TABLE tb_appointment (
    id SERIAL PRIMARY KEY,
    appointment_date DATE NOT NULL,
    status VARCHAR(15) NOT NULL,

    id_doctor INT REFERENCES tb_doctor(id),
    id_user INT REFERENCES tb_user(id)
);