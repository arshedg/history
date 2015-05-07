
CREATE TABLE `ticker` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `equity` varchar(45) DEFAULT NULL,
  `openPrice` float DEFAULT NULL,
  `closePrice` float DEFAULT NULL,
  `highPrice` float DEFAULT NULL,
  `lowPrice` float DEFAULT NULL,
  `adjustedClose` float DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `divident` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) 
