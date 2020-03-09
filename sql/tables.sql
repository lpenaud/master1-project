-- Movie
CREATE TABLE Movie ( 
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	title VARCHAR(255),
	releaseDate DATE,
	description VARCHAR(255) 
);

-- Picture
CREATE TABLE Picture (
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	pathname VARCHAR(255) 
);

-- MoviePicture
CREATE TABLE MoviePicture (
	idMovie INT NOT NULL, 
	idPicture INT NOT NULL,
	type VARCHAR(255),
	CONSTRAINT FK_idMovie FOREIGN KEY (idMovie) REFERENCES Movie(id), 
	CONSTRAINT FK_idPicture FOREIGN KEY (idPicture) REFERENCES Picture(id) 
);