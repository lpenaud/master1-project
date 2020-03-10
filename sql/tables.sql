
-- Administrator
DROP TABLE IF EXISTS Administrator;
CREATE TABLE Administrator(
    `id` INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `lastname` VARCHAR(255) NOT NULL,
    `mail` VARCHAR(255) NOT NULL,
    `login` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL
)
