-- V1.0.018__popular_50_livros.sql

-- =========================================
-- AUTORES
-- =========================================

INSERT INTO tb_autores (nome) VALUES
                                  ('Machado de Assis'),
                                  ('Clarice Lispector'),
                                  ('George Orwell'),
                                  ('J. K. Rowling'),
                                  ('J. R. R. Tolkien'),
                                  ('Stephen King'),
                                  ('Agatha Christie'),
                                  ('Rick Riordan'),
                                  ('Dan Brown'),
                                  ('C. S. Lewis'),
                                  ('Yuval Noah Harari'),
                                  ('Sun Tzu'),
                                  ('Arthur Conan Doyle'),
                                  ('Antoine de Saint-Exupéry'),
                                  ('José de Alencar');

-- =========================================
-- LIVROS
-- =========================================

INSERT INTO tb_livros (
    titulo,
    isbn,
    editora,
    ano_de_publicacao,
    numero_de_paginas,
    idioma,
    sinopse,
    capa
)
VALUES

    ('Dom Casmurro','9788535914849','Companhia das Letras','1899',256,'PORTUGUES','Clássico da literatura brasileira.',NULL),
    ('Memórias Póstumas de Brás Cubas','9788535902778','Companhia das Letras','1881',288,'PORTUGUES','Narrativa inovadora e irônica.',NULL),
    ('A Hora da Estrela','9788532505675','Rocco','1977',96,'PORTUGUES','História marcante de Macabéa.',NULL),
    ('1984','9780451524935','Penguin','1949',328,'INGLES','Distopia sobre vigilância totalitária.',NULL),
    ('A Revolução dos Bichos','9788535909555','Companhia das Letras','1945',152,'PORTUGUES','Sátira política em forma de fábula.',NULL),

    ('Harry Potter e a Pedra Filosofal','9788532530783','Rocco','1997',264,'PORTUGUES','O início da jornada de Harry Potter.',NULL),
    ('Harry Potter e a Câmara Secreta','9788532511669','Rocco','1998',224,'PORTUGUES','Mistérios em Hogwarts.',NULL),
    ('Harry Potter e o Prisioneiro de Azkaban','9788532512062','Rocco','1999',288,'PORTUGUES','A fuga de Sirius Black.',NULL),

    ('O Hobbit','9788595084742','HarperCollins','1937',336,'PORTUGUES','Aventura de Bilbo Bolseiro.',NULL),
    ('O Senhor dos Anéis: A Sociedade do Anel','9788533613379','Martins Fontes','1954',576,'PORTUGUES','A jornada do Um Anel.',NULL),
    ('O Senhor dos Anéis: As Duas Torres','9788533613386','Martins Fontes','1954',464,'PORTUGUES','Continuação da guerra pelo anel.',NULL),
    ('O Senhor dos Anéis: O Retorno do Rei','9788533613393','Martins Fontes','1955',544,'PORTUGUES','Conclusão épica da trilogia.',NULL),

    ('It: A Coisa','9788581052212','Suma','1986',1104,'PORTUGUES','Terror envolvendo um palhaço maligno.',NULL),
    ('O Iluminado','9788556510457','Suma','1977',464,'PORTUGUES','Hotel assombrado e isolamento.',NULL),
    ('Misery','9788560280940','Suma','1987',320,'PORTUGUES','Um escritor preso por fã obsessiva.',NULL),

    ('Assassinato no Expresso do Oriente','9788525430724','L&PM','1934',240,'PORTUGUES','Mistério investigado por Poirot.',NULL),
    ('E Não Sobrou Nenhum','9788525432964','L&PM','1939',400,'PORTUGUES','Convidados presos em ilha misteriosa.',NULL),

    ('Percy Jackson e o Ladrão de Raios','9788598078397','Intrínseca','2005',400,'PORTUGUES','Mitologia grega no mundo moderno.',NULL),
    ('Percy Jackson e o Mar de Monstros','9788598078403','Intrínseca','2006',304,'PORTUGUES','Nova missão para Percy.',NULL),

    ('O Código Da Vinci','9788575421130','Arqueiro','2003',480,'PORTUGUES','Mistério envolvendo símbolos religiosos.',NULL),
    ('Anjos e Demônios','9788575420652','Arqueiro','2000',448,'PORTUGUES','Thriller envolvendo sociedade secreta.',NULL),

    ('As Crônicas de Nárnia','9788578270698','WMF Martins Fontes','1956',752,'PORTUGUES','Fantasia clássica de Nárnia.',NULL),

    ('Sapiens','9788535928198','Companhia das Letras','2011',464,'PORTUGUES','História da humanidade.',NULL),
    ('Homo Deus','9788535930269','Companhia das Letras','2015',448,'PORTUGUES','Reflexões sobre o futuro humano.',NULL),

-- AJUSTADO AQUI
    ('A Arte da Guerra','9788572839044','Edipro','0500',160,'PORTUGUES','Estratégia militar clássica.',NULL),

    ('Sherlock Holmes: Um Estudo em Vermelho','9788594318602','Principis','1887',224,'PORTUGUES','Primeiro caso de Sherlock Holmes.',NULL),

    ('O Pequeno Príncipe','9788595081512','HarperCollins','1943',96,'PORTUGUES','Fábula poética e filosófica.',NULL),

    ('Iracema','9788525406958','L&PM','1865',160,'PORTUGUES','Romance indianista brasileiro.',NULL),

    ('Drácula','9788544001820','Martin Claret','1897',592,'PORTUGUES','Clássico do horror vampiresco.',NULL),
    ('Frankenstein','9788544001691','Martin Claret','1818',288,'PORTUGUES','Criação científica monstruosa.',NULL),

    ('Orgulho e Preconceito','9788544001821','Martin Claret','1813',424,'PORTUGUES','Romance clássico inglês.',NULL),
    ('Jane Eyre','9788544001913','Martin Claret','1847',560,'PORTUGUES','Drama romântico e social.',NULL),

    ('O Morro dos Ventos Uivantes','9788544001180','Martin Claret','1847',368,'PORTUGUES','Paixão intensa e destrutiva.',NULL),

    ('A Metamorfose','9788573266269','Editora 34','1915',96,'PORTUGUES','Transformação absurda de Gregor Samsa.',NULL),

    ('Crime e Castigo','9788573266467','Editora 34','1866',592,'PORTUGUES','Drama psicológico e moral.',NULL),

    ('Os Miseráveis','9788544001487','Martin Claret','1862',1488,'PORTUGUES','Clássico francês sobre injustiça social.',NULL),

    ('O Conde de Monte Cristo','9788537815663','Zahar','1844',1312,'PORTUGUES','Vingança e redenção.',NULL),

    ('Fahrenheit 451','9788525052247','Biblioteca Azul','1953',216,'PORTUGUES','Sociedade onde livros são proibidos.',NULL),

    ('Admirável Mundo Novo','9788525056009','Biblioteca Azul','1932',312,'PORTUGUES','Distopia tecnológica.',NULL),

    ('Neuromancer','9788576573005','Aleph','1984',320,'PORTUGUES','Marco do cyberpunk.',NULL),

    ('Duna','9788576573135','Aleph','1965',680,'PORTUGUES','Ficção científica épica.',NULL),

    ('Fundação','9788576572008','Aleph','1951',296,'PORTUGUES','Saga sobre queda de império galáctico.',NULL),

    ('Jogos Vorazes','9788579800245','Rocco','2008',400,'PORTUGUES','Competição mortal televisionada.',NULL),

    ('Em Chamas','9788579800863','Rocco','2009',432,'PORTUGUES','Continuação da revolução.',NULL),

    ('A Esperança','9788579801051','Rocco','2010',424,'PORTUGUES','Desfecho da trilogia.',NULL),

    ('O Nome do Vento','9788599296493','Arqueiro','2007',656,'PORTUGUES','Fantasia sobre Kvothe.',NULL),

    ('O Temor do Sábio','9788580410327','Arqueiro','2011',960,'PORTUGUES','Continuação da Crônica do Matador do Rei.',NULL),

    ('A Menina que Roubava Livros','9788598078175','Intrínseca','2005',480,'PORTUGUES','Narrativa ambientada na Segunda Guerra.',NULL),

    ('O Alquimista','9788576653721','Paralela','1988',208,'PORTUGUES','Jornada espiritual e filosófica.',NULL),

    ('Verity','9788501117847','Galera','2018',320,'PORTUGUES','Thriller psicológico intenso.',NULL),

    ('É Assim que Acaba','9788501112514','Galera','2016',368,'PORTUGUES','Drama sobre relacionamentos abusivos.',NULL);

-- =========================================
-- RELACIONAMENTOS LIVRO/AUTOR
-- =========================================
-- =========================================
-- RELACIONAMENTOS LIVRO/AUTOR
-- =========================================

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'Dom Casmurro'
  AND a.nome = 'Machado de Assis';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'Memórias Póstumas de Brás Cubas'
  AND a.nome = 'Machado de Assis';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'A Hora da Estrela'
  AND a.nome = 'Clarice Lispector';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = '1984'
  AND a.nome = 'George Orwell';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'A Revolução dos Bichos'
  AND a.nome = 'George Orwell';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'Harry Potter e a Pedra Filosofal'
  AND a.nome = 'J. K. Rowling';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'Harry Potter e a Câmara Secreta'
  AND a.nome = 'J. K. Rowling';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'Harry Potter e o Prisioneiro de Azkaban'
  AND a.nome = 'J. K. Rowling';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'O Hobbit'
  AND a.nome = 'J. R. R. Tolkien';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'O Senhor dos Anéis: A Sociedade do Anel'
  AND a.nome = 'J. R. R. Tolkien';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'O Senhor dos Anéis: As Duas Torres'
  AND a.nome = 'J. R. R. Tolkien';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'O Senhor dos Anéis: O Retorno do Rei'
  AND a.nome = 'J. R. R. Tolkien';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'It: A Coisa'
  AND a.nome = 'Stephen King';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'O Iluminado'
  AND a.nome = 'Stephen King';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'Misery'
  AND a.nome = 'Stephen King';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'Assassinato no Expresso do Oriente'
  AND a.nome = 'Agatha Christie';

INSERT INTO tb_livro_autor (livro_id, autor_id)
SELECT l.id, a.id
FROM tb_livros l, tb_autores a
WHERE l.titulo = 'E Não Sobrou Nenhum'
  AND a.nome = 'Agatha Christie';
-- =========================================
-- AJUSTAR SEQUENCES
-- =========================================

SELECT setval(
               'tb_autores_id_seq',
               (SELECT MAX(id) FROM tb_autores)
       );

SELECT setval(
               'tb_livros_id_seq',
               (SELECT MAX(id) FROM tb_livros)
       );