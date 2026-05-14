-- MySQL dump 10.13  Distrib 8.0.45, for Linux (x86_64)
--
-- Host: localhost    Database: reservation_platform
-- ------------------------------------------------------
-- Server version	8.0.45-0ubuntu0.22.04.1

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
-- Table structure for table `admin_restaurant`
--

DROP TABLE IF EXISTS `admin_restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_restaurant` (
  `admin_id` bigint NOT NULL,
  `restaurant_id` bigint NOT NULL,
  PRIMARY KEY (`admin_id`,`restaurant_id`),
  KEY `fk_admin_restaurant_restaurant` (`restaurant_id`),
  CONSTRAINT `FKg4pdjkpq74le70t1v9w893699` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`),
  CONSTRAINT `FKq0taakbo5naqsi9va8b1njkdp` FOREIGN KEY (`admin_id`) REFERENCES `administrator` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_restaurant`
--

LOCK TABLES `admin_restaurant` WRITE;
/*!40000 ALTER TABLE `admin_restaurant` DISABLE KEYS */;
INSERT INTO `admin_restaurant` VALUES (1,1),(2,1),(1,52),(1,53),(3,53);
/*!40000 ALTER TABLE `admin_restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `administrator` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `role` varchar(50) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `last_login` timestamp NULL DEFAULT NULL,
  `email` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (1,'chrisreservation','ADMIN','2026-04-28 21:52:27','2026-05-13 11:44:36','ceichhorst1@madisoncollege.edu'),(2,'dyanareso','ADMIN','2026-05-10 14:26:42','2026-05-10 18:00:47','dyanasystems@gmail.com'),(3,'superadmin','ADMIN','2026-05-12 14:24:22','2026-05-12 14:24:22','dyanasystems+1@gmail.com');
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `service_instance_id` int NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `party_size` int DEFAULT '1',
  `status` enum('PENDING','CONFIRMED','CANCELLED') DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `handled_by_admin_id` bigint DEFAULT NULL,
  `additionalComments` text,
  `allergenInfo` text,
  `version` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_reservation_instance` (`service_instance_id`),
  KEY `fk_reservation_admin` (`handled_by_admin_id`),
  CONSTRAINT `fk_reservation_instance` FOREIGN KEY (`service_instance_id`) REFERENCES `service_instance` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (19,45,'Patrick Star','pstar@email.com',3,'CONFIRMED','2026-04-27 01:03:51',1,'','',3),(20,47,'Squidward Tentpoles','stent@email.com',5,'CONFIRMED','2026-04-28 17:39:46',1,'Bringing my mom, dad, and siblings.','',4),(21,49,'Eugene Krabs','ekrabs@email.com',4,'CONFIRMED','2026-04-28 18:34:04',NULL,'Can I sit next to the cash register?!','No crab',0),(22,49,'sheldon plankton','plankton@chumbucket.com8',3,'CANCELLED','2026-04-30 20:13:38',NULL,'can you seat my computer wife karen','shellfish',0),(23,58,'Larry Lobster','llobster@email.com',4,'CONFIRMED','2026-05-02 23:41:17',NULL,'How tough are ya?','No lobster',0),(24,66,'Squilliam Fancyson','fancy@email.com',2,'CONFIRMED','2026-05-03 04:19:39',NULL,'I have a giant balloon / casino','Strict gold only diet',0),(25,62,'Spongebob Squarepants','ceichhorst1@madisoncollege.edu',4,'CONFIRMED','2026-05-07 18:56:14',1,'I\'m ready','No snail or starfish',1),(26,62,'Patrick Star','ceichhorst1@madisoncollege.edu',4,'CONFIRMED','2026-05-07 19:09:30',NULL,'I live under a rock','No starfish',0),(27,62,'Sheldon Plankton','ceichhorst1@madisoncollege.edu',4,'CONFIRMED','2026-05-07 19:23:10',NULL,'I went to college!','No plankton',0),(28,62,'Old Man Jenkins','dyanasystems@gmail.com',4,'CONFIRMED','2026-05-07 19:38:38',NULL,'Nice day we are having!','No dairy',0),(29,62,'Squidward Tentacles','dyanasystems@gmail.com',4,'CONFIRMED','2026-05-07 20:38:43',NULL,'Nothing that is a heart attack on a bun','No calamari',0),(30,62,'Timmy Turner','dyanasystems@gmail.com',6,'CONFIRMED','2026-05-08 15:30:55',NULL,'I\'m only 10!','N/A',0),(31,62,'Denzel Crocker','dyanasystems@gmail.com',6,'CONFIRMED','2026-05-08 16:38:27',NULL,'FAIRY GODPARENTS!!!','N/A',0),(32,62,'Chester McBadbat','dyanasystems@gmail.com',2,'CONFIRMED','2026-05-08 16:42:03',NULL,'','N/A',0),(33,62,'Doug Dimmadome','dyanasystems@gmail.com',6,'CONFIRMED','2026-05-08 19:56:42',NULL,'I\'m Doug Dimmadome, owner of the Dimmsdale Dimmadome!','No blubber',0),(34,49,'Jorgen VonStrangle','dyanasystems@gmail.com',10,'CONFIRMED','2026-05-08 20:08:17',NULL,'HAHA HAHA','N/A',0),(35,49,'Jorgen VonStrangle','dyanasystems@gmail.com',10,'CONFIRMED','2026-05-08 20:08:52',NULL,'LOOK I MADE ANOTHER ONE','N/A',0),(36,49,'Jorgen VonStrangle','dyanasystems@gmail.com',10,'CONFIRMED','2026-05-08 20:09:20',NULL,'LOOK I MADE A THIRD ONE','N/A',0),(37,49,'Jorgen VonStrangle','dyanasystems@gmail.com',6,'CONFIRMED','2026-05-08 20:09:57',NULL,'WHY CAN\'T I MAKE ANOTHER ONE FOR 10 PEOPLE?! AHHHH','N/A',0),(38,60,'Jimmy Neutron','dyanasystems@gmail.com',6,'CONFIRMED','2026-05-09 03:54:06',NULL,'','N/A',0),(39,79,'Carl Wheezer','dyanasystems@gmail.com',6,'CONFIRMED','2026-05-10 01:23:23',NULL,'I love llamas','No croissants',0),(40,77,'Hugh Newton','dyanasystems@gmail.com',5,'CONFIRMED','2026-05-10 01:40:41',1,'I just love ducks!','N/A',2),(41,80,'Judy Neutron','dyanasystems@gmail.com',4,'CONFIRMED','2026-05-10 02:56:57',NULL,'Be careful of my husband Hugh!','N/A',0),(42,79,'Sheen Esteban','dyanasystems@gmail.com',6,'CONFIRMED','2026-05-10 17:29:38',NULL,'I love Ultra Lord!','N/A',0),(43,79,'Adam West','dyanasystems@gmail.com',4,'CONFIRMED','2026-05-11 03:26:33',NULL,'WHERE?!','No cat food',0),(44,79,'Barnacle Boy','dyanasystems@gmail.com',4,'CONFIRMED','2026-05-11 03:34:08',NULL,'That\'s Barnacle MAN','No small krabby patties',0),(45,64,'Charlie Dompler','dyanasystems@gmail.com',4,'CONFIRMED','2026-05-12 00:21:43',NULL,'I cannot wait for this iconic meal','N/A',0),(46,66,'Allan Red','dyanasystems@gmail.com',6,'CONFIRMED','2026-05-12 03:56:26',NULL,'','N/A',0),(47,68,'Pim Pimling','dyanasystems@gmail.com',2,'CONFIRMED','2026-05-12 17:30:24',NULL,'Hello!','N/A',0),(48,87,'Mister Boss','dyanasystems@gmail.com',2,'CONFIRMED','2026-05-12 18:50:58',NULL,'Hello everyone!','N/A',0),(49,66,'Eric Cartman','dyanasystems@gmail.com',4,'CONFIRMED','2026-05-12 19:06:45',NULL,'I like cheesy poofs','N/A',0),(50,87,'Randy Marsh','dyanasystems@gmail.com',4,'CONFIRMED','2026-05-12 19:07:39',NULL,'I didn\'t hear no bell!','N/A',0),(51,95,'Stan Marsh','dyanasystems@gmail.com',6,'CONFIRMED','2026-05-12 19:08:09',NULL,'','',0),(52,77,'Kenny McCormick','dyanasystems@gmail.com',5,'CONFIRMED','2026-05-13 05:10:12',1,'By the window please!','1 gluten free',1),(53,81,'Eric Cartman','dyanasystems@gmail.com',5,'CONFIRMED','2026-05-13 16:34:59',1,'Sit by the window please','1 gluten free',1);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reservation_action`
--

DROP TABLE IF EXISTS `reservation_action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_action` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `reservation_id` bigint DEFAULT NULL,
  `admin_id` bigint DEFAULT NULL,
  `action` enum('CREATED','CONFIRMED','CANCELLED','UPDATED') NOT NULL,
  `action_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_action_admin` (`admin_id`),
  KEY `fk_action_reservation` (`reservation_id`),
  CONSTRAINT `fk_action_admin` FOREIGN KEY (`admin_id`) REFERENCES `administrator` (`id`),
  CONSTRAINT `fk_action_reservation` FOREIGN KEY (`reservation_id`) REFERENCES `reservation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation_action`
--

LOCK TABLES `reservation_action` WRITE;
/*!40000 ALTER TABLE `reservation_action` DISABLE KEYS */;
INSERT INTO `reservation_action` VALUES (1,20,1,'UPDATED','2026-05-09 16:01:53'),(2,40,1,'UPDATED','2026-05-09 20:41:53'),(3,40,1,'UPDATED','2026-05-09 21:29:08'),(4,19,1,'UPDATED','2026-05-13 06:09:19'),(5,19,1,'UPDATED','2026-05-13 06:25:25'),(6,19,1,'UPDATED','2026-05-13 06:47:57'),(7,52,1,'UPDATED','2026-05-13 06:48:39'),(8,25,1,'UPDATED','2026-05-13 08:08:25'),(9,53,1,'UPDATED','2026-05-13 11:47:41');
/*!40000 ALTER TABLE `reservation_action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `require_allergen_info` tinyint(1) DEFAULT '0',
  `scheduling_type` enum('DATE_ONLY','DATE_TIME','FIXED_TIME_SLOTS') NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `timezone` varchar(50) DEFAULT 'UTC',
  `admin_id` int DEFAULT NULL,
  `description` text,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `how_it_works` text,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES (1,'Dyana Diner',1,'DATE_TIME','2026-04-11 19:46:42','UTC',NULL,'A cozy modern diner offering curated dining experiences every fall and spring.','Madison','WI','Reservations are required and we sell out quickly.\r\n\r\nService is available on select Tuesdays, Wednesdays and Thursdays.\r\nDining begins at 12:00 pm and lasts approximately 1 - 1 ½ hours.\r\nLunch is $30 per person plus tax.\r\nMake reservations online. The reservation web form will be visible when reservations are available!','dyanasystems@gmail.com'),(52,'Iona\'s Cafe',1,'DATE_ONLY','2026-05-12 14:13:40','America/Chicago',NULL,'A cozy neighborhood cafe offering curated dining experiences and seasonal menus.','Appleton','WI','Reservations are booked by date only. Dining times are assigned by the restaurant before service.','dyanasystems@gmail.com'),(53,'Fox Valley Supper Club',0,'FIXED_TIME_SLOTS','2026-05-12 14:13:43','America/Chicago',NULL,'A classic Wisconsin supper club featuring traditional dinner service and fixed seating times.','Oshkosh','WI','Guests select from predefined reservation time slots established by the restaurant.','dyanasystems@gmail.com');
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service_instance`
--

DROP TABLE IF EXISTS `service_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_instance` (
  `id` int NOT NULL AUTO_INCREMENT,
  `restaurant_id` bigint NOT NULL,
  `service_date` date NOT NULL,
  `service_time` time NOT NULL,
  `capacity` int NOT NULL,
  `version` int DEFAULT '0',
  `visible` tinyint(1) NOT NULL DEFAULT '1',
  `end_time` time DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_instance_restaurant` (`restaurant_id`),
  CONSTRAINT `fk_instance_restaurant` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=99 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service_instance`
--

LOCK TABLES `service_instance` WRITE;
/*!40000 ALTER TABLE `service_instance` DISABLE KEYS */;
INSERT INTO `service_instance` VALUES (45,1,'2026-05-05','12:00:00',40,0,1,NULL),(47,1,'2026-05-06','12:00:00',40,0,1,NULL),(49,1,'2026-05-07','12:00:00',40,0,1,NULL),(51,1,'2026-05-12','12:00:00',40,0,1,NULL),(52,1,'2026-05-13','12:00:00',40,2,1,NULL),(54,1,'2026-05-19','16:00:00',40,0,1,'21:00:00'),(55,1,'2026-05-19','16:15:00',40,0,1,'21:00:00'),(56,1,'2026-05-19','16:30:00',40,0,1,'21:00:00'),(57,1,'2026-05-19','16:45:00',40,0,1,'21:00:00'),(58,1,'2026-05-19','17:00:00',40,0,1,'21:00:00'),(59,1,'2026-05-19','17:15:00',40,0,1,'21:00:00'),(60,1,'2026-05-19','17:30:00',40,0,1,'21:00:00'),(61,1,'2026-05-19','17:45:00',40,0,1,'21:00:00'),(62,1,'2026-05-19','18:00:00',40,0,1,'21:00:00'),(63,1,'2026-05-19','18:15:00',40,0,1,'21:00:00'),(64,1,'2026-05-19','18:30:00',40,0,1,'21:00:00'),(65,1,'2026-05-19','18:45:00',40,1,0,'21:00:00'),(66,1,'2026-05-19','19:00:00',40,0,1,'21:00:00'),(67,1,'2026-05-19','19:15:00',40,0,1,'21:00:00'),(68,1,'2026-05-19','19:30:00',40,0,1,'21:00:00'),(69,1,'2026-05-19','19:45:00',40,0,1,'21:00:00'),(70,1,'2026-05-19','20:00:00',40,1,0,'21:00:00'),(71,1,'2026-05-19','20:15:00',40,1,0,'21:00:00'),(75,1,'2026-06-10','18:00:00',20,0,1,'21:00:00'),(76,1,'2026-06-10','18:15:00',20,0,1,'21:00:00'),(77,1,'2026-06-10','18:30:00',20,0,1,'21:00:00'),(78,1,'2026-06-10','18:45:00',20,0,1,'21:00:00'),(79,1,'2026-06-10','19:00:00',20,0,1,'21:00:00'),(80,1,'2026-06-10','19:15:00',20,0,1,'21:00:00'),(81,1,'2026-06-10','19:30:00',20,0,1,'21:00:00'),(82,1,'2026-06-10','19:45:00',20,0,1,'21:00:00'),(83,1,'2026-06-10','20:00:00',20,0,1,'21:00:00'),(84,1,'2026-06-10','20:15:00',20,1,0,'21:00:00'),(85,1,'2026-06-10','20:30:00',20,0,1,'21:00:00'),(86,1,'2026-06-10','20:45:00',20,1,0,'21:00:00'),(87,52,'2026-05-20','18:00:00',30,0,1,NULL),(88,52,'2026-05-21','18:00:00',30,0,1,NULL),(89,52,'2026-05-22','18:00:00',30,0,1,NULL),(90,53,'2026-05-20','17:00:00',30,0,1,'19:00:00'),(91,53,'2026-05-20','18:00:00',30,0,1,'20:00:00'),(92,53,'2026-05-20','19:00:00',30,0,1,'21:00:00'),(93,53,'2026-05-21','17:00:00',30,0,1,'19:00:00'),(94,53,'2026-05-21','18:00:00',30,0,1,'20:00:00'),(95,53,'2026-05-21','19:00:00',30,0,1,'21:00:00'),(96,53,'2026-05-22','17:00:00',30,0,1,'19:00:00'),(97,53,'2026-05-22','18:00:00',30,0,1,'20:00:00'),(98,53,'2026-05-22','19:00:00',30,0,1,'21:00:00');
/*!40000 ALTER TABLE `service_instance` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-13 17:09:24
