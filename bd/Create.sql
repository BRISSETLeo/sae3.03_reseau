CREATE TABLE IF NOT EXISTS `Compte` (
  `pseudo` VARCHAR(25) NOT NULL,
  `photo_profil` LONGBLOB,
  PRIMARY KEY (`pseudo`)
);

CREATE TABLE IF NOT EXISTS `Follow`(
  `pseudo` VARCHAR(25) NOT NULL,
  `pseudo_follow` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`pseudo`, `pseudo_follow`),
  FOREIGN KEY (`pseudo`) REFERENCES `Compte`(`pseudo`),
  FOREIGN KEY (`pseudo_follow`) REFERENCES `Compte`(`pseudo`)
);

CREATE TABLE IF NOT EXISTS `Publication`(
  `id_publication` INT NOT NULL AUTO_INCREMENT,
  `pseudo` VARCHAR(25) NOT NULL,
  `contenu` VARCHAR(500) NOT NULL,
  `date_publication` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `image` LONGBLOB,
  PRIMARY KEY (`id_publication`),
  FOREIGN KEY (`pseudo`) REFERENCES `Compte`(`pseudo`)
);

CREATE TABLE IF NOT EXISTS `Like`(
  `id_publication` INT NOT NULL,
  `pseudo` VARCHAR(25) NOT NULL,
  PRIMARY KEY (`id_publication`, `pseudo`),
  FOREIGN KEY (`id_publication`) REFERENCES `Publication`(`id_publication`),
  FOREIGN KEY (`pseudo`) REFERENCES `Compte`(`pseudo`)
);

CREATE TABLE IF NOT EXISTS `Message`(
  `id_message` INT NOT NULL AUTO_INCREMENT,
  `pseudo` VARCHAR(25) NOT NULL,
  `pseudo_destinataire` VARCHAR(25) NOT NULL,
  `contenu` VARCHAR(500),
  `vocal` LONGBLOB,
  `image` LONGBLOB,
  `date_message` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `lu` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`id_message`),
  FOREIGN KEY (`pseudo`) REFERENCES `Compte`(`pseudo`),
  FOREIGN KEY (`pseudo_destinataire`) REFERENCES `Compte`(`pseudo`)
);

INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("NoCros", "Bienvenue sur le réseau social de l'IUT d'Orléans !", NULL);
INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("MisterP", "Vous pouvez créer un compte, vous connecter, publier des messages, aimer des messages, suivre des personnes, et bien plus encore !", NULL);
INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("Maxirito", "Vous pouvez aussi modifier votre profil, changer votre photo de profil !", NULL);

INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("NoCros", "TEST", NULL);
INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("MisterP", "TEST 3", NULL);
INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("Maxirito", "TEST 10", NULL);

INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("NoCros", "BONJOUR", NULL);
INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("MisterP", "JURE", NULL);
INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("Maxirito", "Jeu de test insertion", NULL);

INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("NoCros", "5111811", NULL);
INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("MisterP", "T16168816111", NULL);
INSERT INTO `Publication` (`pseudo`, `contenu`, `image`) VALUES ("Maxirito", "rigfjurghur egubgeru buergb ebr g ubreug b", NULL);