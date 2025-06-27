CREATE TABLE autor (
  codigo DECIMAL(10,0) NOT NULL,
  nome VARCHAR(35) NOT NULL,
  PRIMARY KEY (codigo)
);

CREATE TABLE livros (
  codigo DECIMAL(10,0) NOT NULL,
  titulo VARCHAR(45) NOT NULL,
  PRIMARY KEY (codigo)
);

CREATE TABLE edicao (
  codigolivro DECIMAL(10,0) NOT NULL,
  numero CHAR(1) NOT NULL,
  ano INT NOT NULL,
  PRIMARY KEY (codigolivro, numero),
  CONSTRAINT edicao_codigolivro_fkey FOREIGN KEY (codigolivro) REFERENCES livros (codigo),
  CONSTRAINT fk_edicao FOREIGN KEY (codigolivro) REFERENCES livros (codigo)
);

CREATE TABLE livroautor (
  codigolivro DECIMAL(10,0) NOT NULL,
  codigoautor DECIMAL(10,0) NOT NULL,
  PRIMARY KEY (codigolivro, codigoautor),
  CONSTRAINT fk_livroautor FOREIGN KEY (codigolivro) REFERENCES livros (codigo),
  CONSTRAINT fk_livroautor1 FOREIGN KEY (codigoautor) REFERENCES autor (codigo)
);

CREATE TABLE livrostemp (
  codigo DECIMAL(10,0) NOT NULL,
  titulo VARCHAR(45) NOT NULL,
  autor VARCHAR(30) NOT NULL,
  edicao VARCHAR(1) NOT NULL,
  ano INT NOT NULL,
  PRIMARY KEY (codigo)
);