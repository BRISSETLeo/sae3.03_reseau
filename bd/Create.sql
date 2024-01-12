CREATE TABLE IF NOT EXISTS `comptes`(
    `pseudo` VARCHAR(30) NOT NULL,
    `image` LONGBLOB DEFAULT NULL,
    PRIMARY KEY (`pseudo`)
);

CREATE TABLE IF NOT EXISTS `publications`(
    `id_publication` INT NOT NULL AUTO_INCREMENT,
    `pseudo` VARCHAR(30) NOT NULL,
    `content` TEXT DEFAULT NULL,
    `vocal` LONGBLOB DEFAULT NULL,
    `date` TIMESTAMP NOT NULL,
    `photo` LONGBLOB DEFAULT NULL,
    PRIMARY KEY (`id_publication`),
    FOREIGN KEY (`pseudo`) REFERENCES `comptes`(`pseudo`)
);

CREATE TABLE IF NOT EXISTS `likes`(
    `pseudo` VARCHAR(30) NOT NULL,
    `id_publication` INT NOT NULL,
    PRIMARY KEY (`pseudo`,`id_publication`),
    FOREIGN KEY (`pseudo`) REFERENCES `comptes`(`pseudo`),
    FOREIGN KEY (`id_publication`) REFERENCES `publications`(`id_publication`)
);

CREATE TABLE IF NOT EXISTS `commentaires`(
    `id_commentaire` INT NOT NULL AUTO_INCREMENT,
    `pseudo` VARCHAR(30) NOT NULL,
    `id_publication` INT NOT NULL,
    `content` TEXT DEFAULT NULL,
    `vocal` LONGBLOB DEFAULT NULL,
    `date` TIMESTAMP NOT NULL,
    `photo` LONGBLOB DEFAULT NULL,
    PRIMARY KEY (`id_commentaire`),
    FOREIGN KEY (`pseudo`) REFERENCES `comptes`(`pseudo`),
    FOREIGN KEY (`id_publication`) REFERENCES `publications`(`id_publication`)
);

CREATE TABLE IF NOT EXISTS `follows`(
    `pseudo` VARCHAR(30) NOT NULL,
    `pseudo_follow` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`pseudo`,`pseudo_follow`),
    FOREIGN KEY (`pseudo`) REFERENCES `comptes`(`pseudo`),
    FOREIGN KEY (`pseudo_follow`) REFERENCES `comptes`(`pseudo`)
);

CREATE TABLE IF NOT EXISTS `messages`(
    `id_message` INT NOT NULL AUTO_INCREMENT,
    `pseudo` VARCHAR(30) NOT NULL,
    `pseudo_dest` VARCHAR(30) NOT NULL,
    `content` TEXT DEFAULT NULL,
    `vocal` LONGBLOB DEFAULT NULL,
    `date` TIMESTAMP NOT NULL,
    `photo` LONGBLOB DEFAULT NULL,
    `lu` BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (`id_message`),
    FOREIGN KEY (`pseudo`) REFERENCES `comptes`(`pseudo`),
    FOREIGN KEY (`pseudo_dest`) REFERENCES `comptes`(`pseudo`)
);

CREATE TABLE IF NOT EXISTS `typeNotif`(
    `type` VARCHAR(30) NOT NULL,
    PRIMARY KEY (`type`)
);

CREATE TABLE IF NOT EXISTS `notifications`(
    `id_notification` INT NOT NULL AUTO_INCREMENT,
    `pseudo` VARCHAR(30) NOT NULL,
    `pseudo_notif` VARCHAR(30) NOT NULL,
    `type` VARCHAR(30) NOT NULL,
    `id` INT DEFAULT NULL,
    `date` TIMESTAMP NOT NULL,
    `lu` BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (`id_notification`),
    FOREIGN KEY (`pseudo`) REFERENCES `comptes`(`pseudo`),
    FOREIGN KEY (`pseudo_notif`) REFERENCES `comptes`(`pseudo`),
    FOREIGN KEY (`id`) REFERENCES `publications`(`id_publication`),
    FOREIGN KEY (`id`) REFERENCES `commentaires`(`id_commentaire`),
    FOREIGN KEY (`id`) REFERENCES `messages`(`id_message`),
    FOREIGN KEY (`type`) REFERENCES `typeNotif`(`type`)
);