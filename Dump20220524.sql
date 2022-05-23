-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: cinemaseminarski
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `auditorium`
--

DROP TABLE IF EXISTS `auditorium`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auditorium` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  `cinemaId` int NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`),
  KEY `FK_AuditCinema` (`cinemaId`),
  CONSTRAINT `FK_AuditCinema` FOREIGN KEY (`cinemaId`) REFERENCES `cinema` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditorium`
--

LOCK TABLES `auditorium` WRITE;
/*!40000 ALTER TABLE `auditorium` DISABLE KEYS */;
INSERT INTO `auditorium` VALUES (1,'Nikola Tesla',1),(2,'Mihajlo Pupin',1);
/*!40000 ALTER TABLE `auditorium` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `auditoriumsandcinemaview`
--

DROP TABLE IF EXISTS `auditoriumsandcinemaview`;
/*!50001 DROP VIEW IF EXISTS `auditoriumsandcinemaview`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `auditoriumsandcinemaview` AS SELECT 
 1 AS `Id`,
 1 AS `name`,
 1 AS `cinemaId`,
 1 AS `Bioskop`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `cinema`
--

DROP TABLE IF EXISTS `cinema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cinema` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cinema`
--

LOCK TABLES `cinema` WRITE;
/*!40000 ALTER TABLE `cinema` DISABLE KEYS */;
INSERT INTO `cinema` VALUES (1,'Zrenjanin');
/*!40000 ALTER TABLE `cinema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movie`
--

DROP TABLE IF EXISTS `movie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie` (
  `id` varchar(36) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  `year` int DEFAULT NULL,
  `rating` decimal(3,1) DEFAULT NULL,
  `current` bit(1) DEFAULT NULL,
  `hasOscar` bit(1) DEFAULT NULL,
  `bannerUrl` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  `trailerUrl` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movie`
--

LOCK TABLES `movie` WRITE;
/*!40000 ALTER TABLE `movie` DISABLE KEYS */;
INSERT INTO `movie` VALUES ('3e1cde5b-21e9-4109-bf40-a455c3e493c2','Juzni vetar 2',2022,9.7,_binary '\0',_binary '\0','','');
/*!40000 ALTER TABLE `movie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projection`
--

DROP TABLE IF EXISTS `projection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projection` (
  `id` varchar(36) NOT NULL,
  `movieId` varchar(36) DEFAULT NULL,
  `auditoriumId` int DEFAULT NULL,
  `projectionTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `movieId` (`movieId`),
  KEY `fk_projectionaudit` (`auditoriumId`),
  CONSTRAINT `fk_projectionaudit` FOREIGN KEY (`auditoriumId`) REFERENCES `auditorium` (`ID`),
  CONSTRAINT `projection_ibfk_1` FOREIGN KEY (`movieId`) REFERENCES `movie` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projection`
--

LOCK TABLES `projection` WRITE;
/*!40000 ALTER TABLE `projection` DISABLE KEYS */;
INSERT INTO `projection` VALUES ('85a5b9ff-d35e-4ee6-8fdd-806100b3250f','3e1cde5b-21e9-4109-bf40-a455c3e493c2',2,'2022-05-26 21:30:00');
/*!40000 ALTER TABLE `projection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `projekcije`
--

DROP TABLE IF EXISTS `projekcije`;
/*!50001 DROP VIEW IF EXISTS `projekcije`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `projekcije` AS SELECT 
 1 AS `id`,
 1 AS `movieId`,
 1 AS `auditoriumId`,
 1 AS `projectionTime`,
 1 AS `Bioskop`,
 1 AS `title`,
 1 AS `name`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat` (
  `id` varchar(36) NOT NULL,
  `auditoriumId` int NOT NULL,
  `columns` int DEFAULT NULL,
  `rows` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_seataudit` (`auditoriumId`),
  CONSTRAINT `fk_seataudit` FOREIGN KEY (`auditoriumId`) REFERENCES `auditorium` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat` DISABLE KEYS */;
INSERT INTO `seat` VALUES ('1db9f5bd-5429-4bc4-aa1c-184a8c3382af',2,1,2),('335a5333-9b5c-4687-8a0e-91697bf83a4b',2,2,2),('38ba3f47-6847-4418-be2a-864194568972',2,2,1),('4480e54f-17e7-4e50-ae9b-9222dc91db51',2,3,1),('4fa523e6-a9b3-4b37-878f-742d79ce578a',2,2,3),('60de3688-7fb7-4ec7-b250-5c7009db9649',2,1,3),('667c305c-9c57-439f-95f5-19f8c844da99',2,3,2),('a6341d48-4dcd-4f9c-b447-7a291b2f7de6',2,3,3),('e2238900-a05c-4c3e-8530-001fd4164356',2,1,1);
/*!40000 ALTER TABLE `seat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(36) NOT NULL,
  `firstname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  `lastname` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  `password` char(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('481d1b91-1eb2-4400-9fe4-07ff480ae0c5','Stefan','Djurdjevic','djurdjevics','admin','123456');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'cinemaseminarski'
--

--
-- Final view structure for view `auditoriumsandcinemaview`
--

/*!50001 DROP VIEW IF EXISTS `auditoriumsandcinemaview`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `auditoriumsandcinemaview` AS select `auditorium`.`ID` AS `Id`,`auditorium`.`name` AS `name`,`auditorium`.`cinemaId` AS `cinemaId`,`cinema`.`Name` AS `Bioskop` from (`auditorium` join `cinema`) where (`auditorium`.`cinemaId` = `cinema`.`ID`) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `projekcije`
--

/*!50001 DROP VIEW IF EXISTS `projekcije`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `projekcije` AS select `projection`.`id` AS `id`,`projection`.`movieId` AS `movieId`,`projection`.`auditoriumId` AS `auditoriumId`,`projection`.`projectionTime` AS `projectionTime`,`cinema`.`Name` AS `Bioskop`,`movie`.`title` AS `title`,`auditorium`.`name` AS `name` from (((`auditorium` join `cinema`) join `movie`) join `projection`) where ((`projection`.`movieId` = `movie`.`id`) and (`projection`.`auditoriumId` = `auditorium`.`ID`) and (`auditorium`.`cinemaId` = `cinema`.`ID`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-05-24  1:39:24
