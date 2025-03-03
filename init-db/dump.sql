-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: localhost    Database: tracking_visiting_db
-- ------------------------------------------------------
-- Server version	8.4.4

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `tracking_visiting_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `tracking_visiting_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `tracking_visiting_db`;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `timezone` varchar(50) NOT NULL,
  `total_patients` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,'Dr. Lily','Evans','America/New_York',4),(2,'Dr. Miles','Johnson','America/Chicago',4),(3,'Dr. Nora','Davis','America/Denver',2),(4,'Dr. Owen','Martin','America/Los_Angeles',3),(5,'Dr. Peter','Lee','America/Chicago',1),(6,'Dr. Quinn','Taylor','Europe/London',5),(7,'Dr. Rachel','Clark','Europe/Paris',6),(8,'Dr. Samuel','King','Asia/Hong_Kong',1),(9,'Dr. Tanya','Walker','Asia/Kolkata',4),(10,'Dr. Ursula','Anderson','Australia/Melbourne',3),(11,'Dr. Victor','Smith','Europe/Rome',2),(12,'Dr. Wendy','Jones','Europe/Madrid',8),(13,'Dr. Xavier','White','America/Sao_Paulo',2),(14,'Dr. Yolanda','Taylor','Asia/Shanghai',7),(15,'Dr. Zachary','Brown','Asia/Seoul',1),(16,'Dr. Ava','Lopez','Asia/Tokyo',3),(17,'Dr. Brian','Parker','Africa/Johannesburg',5),(18,'Dr. Chloe','Evans','America/New_York',6),(19,'Dr. Derek','Adams','Asia/Manila',1),(20,'Dr. Ellie','Scott','America/New_York',1),(21,'Dr. Amelia','Taylor','Europe/London',6),(22,'Dr. Mason','Williams','Asia/Kolkata',2),(23,'Dr. Sophie','Clark','Europe/Paris',1),(24,'Dr. Lucas','Johnson','Asia/Hong_Kong',2),(25,'Dr. Natalie','Lee','America/Chicago',3),(26,'Dr. Ian','Brown','Australia/Melbourne',4),(27,'Dr. Logan','Davis','Africa/Johannesburg',1),(28,'Dr. Ethan','Walker','Asia/Seoul',2),(29,'Dr. Iris','Lopez','Europe/Madrid',4),(30,'Dr. Riley','White','America/Denver',6);
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,'Zara','Nguyen'),(2,'Yasmin','Martinez'),(3,'Xander','Clark'),(4,'Wendy','Hall'),(5,'Victor','Young'),(6,'Ursula','King'),(7,'Tara','Lopez'),(8,'Samuel','Gonzalez'),(9,'Rory','Perez'),(10,'Quincy','Adams'),(11,'Paul','Morris'),(12,'Olivia','Johnson'),(13,'Nina','Lee'),(14,'Maya','Davis'),(15,'Liam','Brown'),(16,'Katie','Miller'),(17,'Jackson','Taylor'),(18,'Ivy','Moore'),(19,'Hannah','Jackson'),(20,'Gabriel','Martinez'),(21,'Felix','Wilson'),(22,'Eva','Smith'),(23,'Daniel','Anderson'),(24,'Cathy','Thomas'),(25,'Ben','White'),(26,'Alice','Hall'),(27,'Aidan','Rodriguez'),(28,'Zoe','Walker'),(29,'Yvonne','Lopez'),(30,'Xena','Hernandez'),(31,'Wesley','Davis'),(32,'Vera','King'),(33,'Ulysses','Hernandez'),(34,'Tyler','Walker'),(35,'Sophie','Allen'),(36,'Riley','Baker'),(37,'Quentin','Miller'),(38,'Penny','Wright'),(39,'Olga','Adams'),(40,'Nadine','Nelson'),(41,'Mason','White'),(42,'Lilly','Evans'),(43,'Kyle','Taylor'),(44,'Jason','Moore'),(45,'Isla','Parker'),(46,'Hugo','Cook'),(47,'Grace','Taylor'),(48,'Fiona','Clark'),(49,'Ethan','King'),(50,'Diana','Hughes'),(51,'Sienna','Wright'),(52,'Maya','Martinez'),(53,'Jared','Lopez'),(54,'Nicole','Wilson'),(55,'Oliver','Rodriguez'),(56,'Charlotte','Brown'),(57,'Mason','Taylor'),(58,'Leo','Adams'),(59,'Bella','Scott'),(60,'Lucas','Lee'),(61,'Amelia','Morris'),(62,'Grayson','Young'),(63,'Kai','Smith'),(64,'Ryder','Thomas'),(65,'Isaac','Hernandez'),(66,'Zane','King'),(67,'Piper','Taylor'),(68,'Luca','Davis'),(69,'Olivia','White'),(70,'Evelyn','Johnson');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visits`
--

DROP TABLE IF EXISTS `visits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visits` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `patient_id` bigint DEFAULT NULL,
  `doctor_id` bigint DEFAULT NULL,
  `start_date_time` datetime NOT NULL,
  `end_date_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_visits_patient_id_id` (`patient_id`),
  KEY `fk_visits_doctor_id_id` (`doctor_id`),
  CONSTRAINT `fk_visits_doctor_id_id` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_visits_patient_id_id` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visits`
--

LOCK TABLES `visits` WRITE;
/*!40000 ALTER TABLE `visits` DISABLE KEYS */;
INSERT INTO `visits` VALUES (1,55,17,'2025-04-12 12:53:00','2025-04-12 13:53:00'),(2,21,23,'2025-01-24 09:54:10','2025-01-24 10:54:10'),(3,28,30,'2025-06-11 17:35:35','2025-06-11 18:35:35'),(4,57,12,'2025-12-18 13:22:46','2025-12-18 14:22:46'),(5,35,7,'2025-01-18 19:08:45','2025-01-18 20:08:45'),(6,24,12,'2025-12-06 16:11:03','2025-12-06 17:11:03'),(7,3,1,'2025-03-10 19:43:39','2025-03-10 20:43:39'),(8,28,10,'2025-06-25 10:13:40','2025-06-25 11:13:40'),(9,63,12,'2025-02-08 15:25:07','2025-02-08 16:25:07'),(10,42,16,'2025-05-13 12:02:17','2025-05-13 13:02:17'),(11,36,1,'2025-02-11 10:16:24','2025-02-11 11:16:24'),(12,35,4,'2025-12-09 11:50:45','2025-12-09 12:50:45'),(13,52,27,'2025-04-18 18:34:51','2025-04-18 19:34:51'),(14,15,9,'2025-07-05 10:35:53','2025-07-05 11:35:53'),(15,63,29,'2025-08-09 14:27:08','2025-08-09 15:27:08'),(16,64,17,'2025-06-26 08:35:07','2025-06-26 09:35:07'),(17,37,30,'2025-07-28 10:05:15','2025-07-28 11:05:15'),(18,55,21,'2025-04-08 17:45:02','2025-04-08 18:45:02'),(19,54,18,'2025-02-26 16:41:20','2025-02-26 17:41:20'),(20,48,12,'2025-07-03 19:40:43','2025-07-03 20:40:43'),(21,14,18,'2025-10-08 14:59:15','2025-10-08 15:59:15'),(22,25,20,'2025-11-27 09:58:36','2025-11-27 10:58:36'),(23,6,9,'2025-09-19 17:38:16','2025-09-19 18:38:16'),(24,25,4,'2025-05-12 11:45:57','2025-05-12 12:45:57'),(25,35,7,'2025-05-07 12:48:08','2025-05-07 13:48:08'),(26,27,18,'2025-06-07 10:36:33','2025-06-07 11:36:33'),(27,30,7,'2025-04-23 17:53:51','2025-04-23 18:53:51'),(28,40,29,'2025-09-17 10:55:08','2025-09-17 11:55:08'),(29,18,6,'2025-11-02 13:00:37','2025-11-02 14:00:37'),(30,49,7,'2025-11-10 16:10:43','2025-11-10 17:10:43'),(31,12,14,'2025-04-08 11:24:43','2025-04-08 12:24:43'),(32,42,15,'2025-02-19 19:35:32','2025-02-19 20:35:32'),(33,35,18,'2025-09-08 19:47:04','2025-09-08 20:47:04'),(34,8,30,'2025-02-12 08:55:42','2025-02-12 09:55:42'),(35,37,6,'2025-09-10 18:06:50','2025-09-10 19:06:50'),(36,36,19,'2025-05-04 12:33:45','2025-05-04 13:33:45'),(37,10,22,'2025-05-15 08:58:57','2025-05-15 09:58:57'),(38,38,30,'2025-01-12 15:18:33','2025-01-12 16:18:33'),(39,53,6,'2025-06-08 09:37:57','2025-06-08 10:37:57'),(40,31,17,'2025-12-09 13:08:25','2025-12-09 14:08:25'),(41,70,6,'2025-01-21 12:14:17','2025-01-21 13:14:17'),(42,56,13,'2025-04-11 10:52:13','2025-04-11 11:52:13'),(43,50,1,'2025-08-13 13:47:26','2025-08-13 14:47:26'),(44,61,2,'2025-11-09 12:00:54','2025-11-09 13:00:54'),(45,60,11,'2025-07-20 10:27:17','2025-07-20 11:27:17'),(46,51,30,'2025-04-23 18:59:26','2025-04-23 19:59:26'),(47,21,3,'2025-12-24 08:08:57','2025-12-24 09:08:57'),(48,66,26,'2025-01-06 13:39:12','2025-01-06 14:39:12'),(49,42,28,'2025-01-25 08:21:15','2025-01-25 09:21:15'),(50,48,16,'2025-06-14 15:46:43','2025-06-14 16:46:43'),(51,1,24,'2025-02-16 15:14:36','2025-02-16 16:14:36'),(52,18,14,'2025-11-14 18:10:22','2025-11-14 19:10:22'),(53,18,26,'2025-11-10 13:19:18','2025-11-10 14:19:18'),(54,68,12,'2025-04-03 08:55:05','2025-04-03 09:55:05'),(55,59,14,'2025-07-20 16:29:44','2025-07-20 17:29:44'),(56,63,2,'2025-03-03 15:27:03','2025-03-03 16:27:03'),(57,60,4,'2025-05-26 16:21:02','2025-05-26 17:21:02'),(58,58,18,'2025-09-11 17:49:43','2025-09-11 18:49:43'),(59,16,21,'2025-10-10 10:10:57','2025-10-10 11:10:57'),(60,13,14,'2025-04-08 15:31:47','2025-04-08 16:31:47'),(61,38,21,'2025-07-15 14:43:46','2025-07-15 15:43:46'),(62,12,22,'2025-07-12 17:24:29','2025-07-12 18:24:29'),(63,44,26,'2025-07-15 10:20:34','2025-07-15 11:20:34'),(64,47,21,'2025-02-09 08:08:55','2025-02-09 09:08:55'),(65,22,8,'2025-12-16 09:53:30','2025-12-16 10:53:30'),(66,27,21,'2025-06-03 15:59:39','2025-06-03 16:59:39'),(67,33,2,'2025-09-15 10:47:06','2025-09-15 11:47:06'),(68,54,11,'2025-02-16 17:17:07','2025-02-16 18:17:07'),(69,62,29,'2025-02-13 16:22:18','2025-02-13 17:22:18'),(70,13,7,'2025-06-03 13:32:49','2025-06-03 14:32:49'),(71,57,9,'2025-01-27 18:45:46','2025-01-27 19:45:46'),(72,70,12,'2025-08-22 10:23:23','2025-08-22 11:23:23'),(73,28,21,'2025-12-12 15:59:29','2025-12-12 16:59:29'),(74,36,9,'2025-05-06 16:28:29','2025-05-06 17:28:29'),(75,18,7,'2025-01-09 10:02:07','2025-01-09 11:02:07'),(76,21,2,'2025-05-12 16:54:14','2025-05-12 17:54:14'),(77,32,17,'2025-03-10 11:24:44','2025-03-10 12:24:44'),(78,13,3,'2025-12-17 15:34:33','2025-12-17 16:34:33'),(79,49,24,'2025-04-23 12:23:25','2025-04-23 13:23:25'),(80,67,14,'2025-03-07 16:52:38','2025-03-07 17:52:38'),(81,9,18,'2025-10-12 15:57:15','2025-10-12 16:57:15'),(82,34,29,'2025-05-08 11:46:39','2025-05-08 12:46:39'),(83,10,12,'2025-04-17 19:14:09','2025-04-17 20:14:09'),(84,10,30,'2025-10-03 16:29:24','2025-10-03 17:29:24'),(85,12,1,'2025-08-10 19:41:34','2025-08-10 20:41:34'),(86,7,14,'2025-02-27 09:58:46','2025-02-27 10:58:46'),(87,54,17,'2025-09-25 16:34:51','2025-09-25 17:34:51'),(88,69,13,'2025-07-26 08:44:26','2025-07-26 09:44:26'),(89,67,25,'2025-03-17 14:35:40','2025-03-17 15:35:40'),(90,63,5,'2025-01-02 15:43:48','2025-01-02 16:43:48'),(91,28,25,'2025-04-06 09:28:45','2025-04-06 10:28:45'),(92,67,25,'2025-01-26 18:49:02','2025-01-26 19:49:02'),(93,64,14,'2025-02-26 10:47:58','2025-02-26 11:47:58'),(94,5,26,'2025-10-04 09:37:22','2025-10-04 10:37:22'),(95,24,12,'2025-04-21 11:23:14','2025-04-21 12:23:14'),(96,10,10,'2025-07-12 09:44:54','2025-07-12 10:44:54'),(97,19,16,'2025-08-11 11:16:05','2025-08-11 12:16:05'),(98,15,6,'2025-01-16 18:26:54','2025-01-16 19:26:54'),(99,16,28,'2025-03-12 13:57:27','2025-03-12 14:57:27'),(100,42,10,'2025-11-28 13:07:20','2025-11-28 14:07:20');
/*!40000 ALTER TABLE `visits` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `after_insert_visits` AFTER INSERT ON `visits` FOR EACH ROW BEGIN
    UPDATE doctors d
    SET d.total_patients = (SELECT DISTINCT COUNT(v.patient_id) FROM visits v WHERE v.doctor_id = NEW.doctor_id)
    WHERE d.id = NEW.doctor_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-02 22:52:28
