CREATE DATABASE `esports`;

USE `esports`;

CREATE TABLE `users` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `email` varchar(100) NOT NULL,
                         `password` varchar(100) NOT NULL,
                         `first_name` varchar(100) NOT NULL,
                         `last_name` varchar(100) NOT NULL,
                         PRIMARY KEY (`id`),
                         UNIQUE KEY `users_UN` (`email`)
) ;

