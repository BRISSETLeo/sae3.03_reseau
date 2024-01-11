INSERT INTO `comptes`(`pseudo`) VALUES ('NoCros');
INSERT INTO `comptes`(`pseudo`) VALUES ('Maxirito');
INSERT INTO `comptes`(`pseudo`) VALUES ('MisterP');

INSERT INTO `publications`(`pseudo`,`content`,`date`) VALUES ('NoCros','Salut tout le monde !',"2020-04-01 12:00:00");
INSERT INTO `publications`(`pseudo`,`content`,`date`) VALUES ('NoCros',"Aujourd'hui c'est chacun pour sois !","2020-05-01 12:00:00");

INSERT INTO `publications`(`pseudo`,`content`,`date`) VALUES ('Maxirito',"Je suis un peu perdu...","2020-06-01 12:00:00");
INSERT INTO `publications`(`pseudo`,`content`,`date`) VALUES ('Maxirito',"Peux-tu m'expliquer ce qu'il fait ici ?","2020-01-01 12:00:00");

INSERT INTO `publications`(`pseudo`,`content`,`date`) VALUES ('MisterP',"Hey bonjour SysX comment ça va today ?","2021-06-01 12:00:00");
INSERT INTO `publications`(`pseudo`,`content`,`date`) VALUES ('MisterP',"La vidéo est sortie. Foncez la voir !","2021-06-01 15:30:48");

INSERT INTO `likes`(`pseudo`,`id_publication`) VALUES ('NoCros',1);
INSERT INTO `likes`(`pseudo`,`id_publication`) VALUES ('NoCros',2);
INSERT INTO `likes`(`pseudo`,`id_publication`) VALUES ('NoCros',3);

INSERT INTO `likes`(`pseudo`,`id_publication`) VALUES ('Maxirito',1);
INSERT INTO `likes`(`pseudo`,`id_publication`) VALUES ('Maxirito',2);

INSERT INTO `likes`(`pseudo`,`id_publication`) VALUES ('MisterP',1);

INSERT INTO `commentaires`(`pseudo`,`id_publication`,`content`,`date`) VALUES ('Maxirito',1,"Salut NoCros !","2020-04-01 12:00:00");
INSERT INTO `commentaires`(`pseudo`,`id_publication`,`content`,`date`) VALUES ('Maxirito',2,"Salut NoCros !","2020-05-01 12:00:00");
INSERT INTO `commentaires`(`pseudo`,`id_publication`,`content`,`date`) VALUES ('MisterP',2,"Salut NoCros !","2020-01-01 12:00:00");

INSERT INTO `follows` (`pseudo`,`pseudo_follow`) VALUES ('NoCros','Maxirito');
INSERT INTO `follows` (`pseudo`,`pseudo_follow`) VALUES ('MisterP','Maxirito');

INSERT INTO `messages`(`pseudo`,`pseudo_dest`,`content`,`date`) VALUES ('NoCros','Maxirito',"Salut Maxirito !","2020-04-01 12:00:00");

INSERT INTO `typeNotif`(`type`) VALUES ('like');
INSERT INTO `typeNotif`(`type`) VALUES ('commentaire');
INSERT INTO `typeNotif`(`type`) VALUES ('follow');
INSERT INTO `typeNotif`(`type`) VALUES ('message');
INSERT INTO `typeNotif`(`type`) VALUES ('publication');

INSERT INTO `notifications`(`pseudo`, `pseudo_notif`,`id`,`type`,`date`) 
VALUES ('NoCros','Maxirito',1,'like',"2020-04-01 12:00:00");
INSERT INTO `notifications`(`pseudo`, `pseudo_notif`,`id`,`type`,`date`)
VALUES ('NoCros','MisterP',1,'commentaire',"2020-04-01 12:00:00");