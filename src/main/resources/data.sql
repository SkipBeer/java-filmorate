DELETE FROM genres_films;
DELETE FROM films;
DELETE FROM mpa;
DELETE FROM genres;


--MERGE INTO mpa m
--USING (VALUES (1, 'G')) AS S(id, name)
--ON m.id = S.id
--WHEN NOT MATCHED THEN
--INSERT (id, name)
--VALUES (1, 'G');

INSERT INTO mpa(id, name) values(1, 'G');
INSERT INTO mpa(id, name) values(2, 'PG');
INSERT INTO mpa(id, name) values(3, 'PG-13');
INSERT INTO mpa(id, name) values(4, 'R');
INSERT INTO mpa(id, name) values(5, 'NC-17');

INSERT INTO genres(id, name) values(1, 'Комедия');
INSERT INTO genres(id, name) values(2, 'Драма');
INSERT INTO genres(id, name) values(3, 'Мультфильм');
INSERT INTO genres(id, name) values(4, 'Триллер');
INSERT INTO genres(id, name) values(5, 'Документальный');
INSERT INTO genres(id, name) values(6, 'Боевик');

