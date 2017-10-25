CREATE TABLE public.uf
(
    id SERIAL NOT NULL ,
    nome character varying(2) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT uf_pkey PRIMARY KEY (id)
)


CREATE TABLE public.arquivo
(
    id serial NOT NULL ,
    nome character varying(50) COLLATE pg_catalog."default" NOT NULL,
    extensao character varying(10) COLLATE pg_catalog."default" NOT NULL,
    arquivo bytea NOT NULL,
    CONSTRAINT arquivo_pkey PRIMARY KEY (id)
)


CREATE TABLE public.cidade
(
    id serial NOT NULL ,
    nome character varying(50) COLLATE pg_catalog."default" NOT NULL,
    uf_id integer NOT NULL,
    CONSTRAINT cidade_pkey PRIMARY KEY (id),
    CONSTRAINT uf_fk FOREIGN KEY (uf_id)
        REFERENCES public.uf (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
	


CREATE TABLE public.pessoaexemplo
(
    id serial NOT NULL ,
    nome character varying(50) COLLATE pg_catalog."default" NOT NULL,
    endereco character varying(50) COLLATE pg_catalog."default" NOT NULL,
    numero character varying(10) COLLATE pg_catalog."default" NOT NULL,
    complemento character varying(10) COLLATE pg_catalog."default",
    sexo character varying(1) COLLATE pg_catalog."default" NOT NULL,
    datanascimento date NOT NULL,
    datacadastro date NOT NULL,
    email character varying(50) COLLATE pg_catalog."default" NOT NULL,
    administrador boolean NOT NULL,
    ativo boolean NOT NULL,
    senha bytea NOT NULL,
    CONSTRAINT pessoaexemplo_pkey PRIMARY KEY (id)
)