CREATE TABLE laboratorio (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nome VARCHAR(100)
);

CREATE TABLE propriedade (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nome VARCHAR(100),
                             cnpj VARCHAR(20)
);

CREATE TABLE pessoa (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nome VARCHAR(100),
                        data_inicial TIMESTAMP,
                        data_final TIMESTAMP,
                        observacoes VARCHAR(255),
                        propriedade_id BIGINT,
                        laboratorio_id BIGINT,
                        CONSTRAINT fk_propriedade
                            FOREIGN KEY (propriedade_id)
                                REFERENCES propriedade(id)
                                ON DELETE CASCADE,
                        CONSTRAINT fk_laboratorio
                            FOREIGN KEY (laboratorio_id)
                                REFERENCES laboratorio(id)
                                ON DELETE CASCADE
);

INSERT INTO laboratorio(nome) VALUES ('Agro Skynet');
INSERT INTO laboratorio(nome) VALUES ('Umbrella Agro');
INSERT INTO laboratorio(nome) VALUES ('Osborn Agro');
INSERT INTO laboratorio(nome) VALUES ('Skyrim Agro');
INSERT INTO laboratorio(nome) VALUES ('Agro Brasil');
INSERT INTO laboratorio(nome) VALUES ('Agro EUA');

INSERT INTO propriedade(nome, cnpj) VALUES ('Agrotis 1', '04.909.987/0001-89');
INSERT INTO propriedade(nome, cnpj) VALUES ('Agrotis 2', '04.909.987/0001-88');
INSERT INTO propriedade(nome, cnpj) VALUES ('Agrotis 3', '04.909.987/0001-87');
INSERT INTO propriedade(nome, cnpj) VALUES ('Agrotis 4', '04.909.987/0001-86');
INSERT INTO propriedade(nome, cnpj) VALUES ('Agrotis 5', '04.909.987/0001-85');

INSERT INTO pessoa(nome, data_inicial, data_final, observacoes, propriedade_id, laboratorio_id)
    VALUES ('Oparador 1', '2025-02-01T13:16:01Z', '2025-02-05T13:16:01Z', 'Come aneies de cebola',1, 1),
           ('Oparador 2', '2025-03-01T13:16:01Z', '2025-03-05T13:16:01Z', null,2, 2),
           ('Oparador 3', '2025-04-01T13:16:01Z', '2025-04-05T13:16:01Z', null,2, 2),
           ('Oparador 4', '2025-05-01T13:16:01Z', '2025-05-05T13:16:01Z', 'operacional 4',3, 3),
           ('Oparador 5', '2025-06-01T13:16:01Z', '2025-06-05T13:16:01Z', '',1, 3),
           ('Oparador 6', '2025-07-01T13:16:01Z', '2025-07-05T13:16:01Z', null,4, 4),
           ('Oparador 7', '2025-08-01T13:16:01Z', '2025-08-05T13:16:01Z', 'Na data',5, 4);