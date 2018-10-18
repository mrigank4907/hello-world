CREATE DATABASE IF NOT EXISTS Assign;
USE Assign;

Drop table if exists movie_images;
DROP TABLE IF EXISTS movie_details;


CREATE TABLE movie_details (
	id INTEGER AUTO_INCREMENT NOT NULL PRIMARY KEY,
	Movie_Name VARCHAR(15) NOT NULL,
	Genre VARCHAR(20) NOT NULL,
	Year_Release INTEGER NOT NULL,
	Dir_First_Name VARCHAR(30) NOT NULL,
	Dir_Last_Name VARCHAR(30) NOT NULL,
	Country VARCHAR(20) NOT NULL,
	Age INTEGER(3) NOT NULL,
	Rating Varchar(15) NOT NULL);

SELECT 'INSERTING DATA INTO DATABASE' as 'INFO';

INSERT INTO movie_details VALUES ( null, 'Sing Street', 'Drama/Romance', 2016, 'John', 'Carney', 'Ireland', 45, 'PG-13');
INSERT INTO movie_details VALUES ( null, 'Avengers', 'Science-fiction', 2012, 'Joss', 'Whedon', 'USA', 52, 'PG-13');
INSERT INTO movie_details VALUES ( null, 'Inception', 'Mystery/Science', 2010, 'Christopher','Nolan', 'England', 47, 'PG-13');
INSERT INTO movie_details VALUES ( null, 'Big Hero 6', 'Action', 2014, 'Chris', 'Williams', 'USA', 48, 'PG');
INSERT INTO movie_details VALUES ( null, 'OldBoy', 'Mystery/Crime', 2003, 'Park', 'Chan-wook', 'South-Korea', 53, 'R');
INSERT INTO movie_details VALUES ( null, 'The Island', 'Thriller/Drama', 2005, 'Michael', 'Bay', 'USA', 51, 'PG-13');
INSERT INTO movie_details VALUES ( null, 'King Kong', 'Drama', 2005, 'Peter', 'Jackson', 'New Zeland', 55, 'PG-13');
INSERT INTO movie_details VALUES ( null, 'Shutter Island', 'Thriller/Drama', 2010, 'Martin', 'Scorsese', 'USA', 74, 'R');
INSERT INTO movie_details VALUES ( null, 'The Prestige', 'Science-Fiction', 2006, 'Chrisopher', 'Nolan', 'England', 47, 'PG-13');
INSERT INTO movie_details VALUES ( null, 'Titanic', 'Drama/Disaster', 1997, 'James', 'Cameron', 'Canada', 62, 'PG-13');
INSERT INTO movie_details VALUES ( null, 'Once', 'Drama/Romance', 2007, 'John', 'Carney', 'Ireland', 45, 'R');
INSERT INTO movie_details VALUES ( null, 'The Raid', 'Crime/Thriller', 2011, 'Gareth', 'Evans', 'UK', 37, 'PG-13');
INSERT INTO movie_details VALUES ( null, 'About Time', 'Drama/Fanctacy', 2013, 'Richard', 'Curtis', 'New Zealand', 60, 'R');
INSERT INTO movie_details VALUES ( null, 'Prisoners', 'Crime/Thriller', 2013, 'Denis', 'Villeneuve', 'Canada', 49, 'R');
INSERT INTO movie_details VALUES ( null, 'Avatar', 'Science-Fiction', 2009, 'James', 'Cameron', 'Canada', 62, 'PG-13');
INSERT INTO movie_details VALUES ( null, 'Nightcrawler', 'Drama/Thriller', 2014, 'Dan', 'Gilroy', 'USA', 57, 'R');



select * from movie_details;



Create Table movie_images
(
	ImageNo int(11) not null auto_increment primary Key,
    Image longblob,
    Movie_id integer(3)
);

insert into movie_images values(null,load_file
('E:/MoviePics/SingStreet.jpg'),1);
insert into movie_images values(null,load_file
('E:/MoviePics/Avengers.jpg'),2);
insert into movie_images values(null,load_file
('E:/MoviePics/ThePrestige.jpg'),9);
insert into movie_images values(null,load_file
('E:/MoviePics/Oldboy.jpg'),5);
insert into movie_images values(null,load_file
('E:/MoviePics/TheIsland.jpg'),6);
insert into movie_images values(null,load_file
('E:/MoviePics/ShutterIsland.jpg'),8);
insert into movie_images values(null,load_file
('E:/MoviePics/Inception.jpg'),3);
insert into movie_images values(null,load_file
('E:/MoviePics/KingKong.jpg'),7);
insert into movie_images values(null,load_file
('E:/MoviePics/AboutTime.jpg'),13);
insert into movie_images values(null,load_file
('E:/MoviePics/Titanic.jpg'),10);
insert into movie_images values(null,load_file
('E:/MoviePics/Prisoners.jpg'),14);
insert into movie_images values(null,load_file
('E:/MoviePics/Nightcrawler.jpg'),16);
insert into movie_images values(null,load_file
('E:/MoviePics/Once.jpg'),11);
insert into movie_images values(null,load_file
('E:/MoviePics/Avatar.jpg'),15);
insert into movie_images values(null,load_file
('E:/MoviePics/TheRaid.jpg'),12);


select * from movie_images;


DROP TABLE IF EXISTS movies_audit;

CREATE TABLE movies_audit
(
    movie_id        INT     NOT NULL,
    action_type     VARCHAR(50),
    action_date     DATETIME NOT NULL
);


DELIMITER //
DROP TRIGGER IF EXISTS movies_after_insert;
//
CREATE TRIGGER movies_after_insert
    AFTER INSERT on movie_details
    FOR EACH ROW
BEGIN
    INSERT INTO movies_audit VALUES
    (NEW.id,"INSERTED", NOW());
    
END//


DELIMITER //
DROP TRIGGER IF EXISTS movies_after_update;
//
CREATE TRIGGER movies_after_update
    AFTER UPDATE on movie_details
    FOR EACH ROW
BEGIN
    INSERT INTO movies_audit VALUES
    (OLD.id,"UPDATED", NOW());
    
END//


DELIMITER //
DROP TRIGGER IF EXISTS movies_after_delete;
//
CREATE TRIGGER movies_after_delete
    AFTER DELETE on movie_details
    FOR EACH ROW
BEGIN
    INSERT INTO movies_audit VALUES
    (OLD.id,"DELETED", NOW());
    
END//


select * from movies_audit;