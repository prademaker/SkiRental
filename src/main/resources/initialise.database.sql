DROP DATABASE IF EXISTS skirental;
CREATE DATABASE skirental;
CREATE USER IF NOT EXISTS 'userSki'@'localhost'
IDENTIFIED BY 'userSkiPW';
GRANT ALL ON skirental.*
    TO 'userSki'@'localhost';
FLUSH PRIVILEGES