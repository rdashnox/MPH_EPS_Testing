-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: motorph_eps
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `leaves`
--

DROP TABLE IF EXISTS `leaves`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leaves` (
  `EID` int NOT NULL,
  `Leave_ID` text NOT NULL,
  `Date_Filed` text,
  `Date_From` text,
  `Date_To` text,
  `Reason_For_Leave` text,
  `Leave_Status` text,
  PRIMARY KEY (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leaves`
--

LOCK TABLES `leaves` WRITE;
/*!40000 ALTER TABLE `leaves` DISABLE KEYS */;
INSERT INTO `leaves` VALUES (10002,'L1013','2025-04-28','2025-05-12','2025-05-16','Vacation Leave','Approved'),(10003,'L1007','2025-02-24','2025-03-10','2025-03-17','Vacation Leave','Approved'),(10005,'L1001','2024-12-22','2025-01-05','2025-01-10','Vacation Leave','Rejected'),(10008,'L1011','2025-04-08','2025-04-22','2025-04-23','Bereavement Leave','Approved'),(10009,'L1004','2025-02-16','2025-02-10','2025-02-15','Medical Leave','Rejected'),(10011,'L1016','2025-06-01','2025-06-15','2025-06-16','Personal Time Off','Approved'),(10012,'L1002','2025-01-21','2025-01-15','2025-01-20','Medical Leave','Approved'),(10014,'L1009','2025-03-18','2025-04-01','2025-04-03','Compensatory Time Off','Approved'),(10018,'L1006','2025-02-15','2025-03-01','2025-03-02','Personal Time Off','Pending'),(10019,'L1014','2025-05-26','2025-05-20','2025-05-25','Medical Leave','Approved'),(10021,'L1012','2025-04-17','2025-05-01','2025-05-05','Personal Time Off','Rejected'),(10023,'L1003','2025-01-18','2025-02-01','2025-02-05','Bereavement Leave','Approved'),(10026,'L1015','2025-05-18','2025-06-01','2025-06-10','Leave Without Pay','Pending'),(10027,'L1008','2025-03-06','2025-03-20','2025-03-25','Vacation Leave','Pending'),(10030,'L1010','2025-03-27','2025-04-10','2025-04-17','Parental Leave','Approved'),(10031,'L1005','2025-02-06','2025-02-20','2025-02-28','Vacation Leave','Approved'),(10033,'L1017','2025-06-08','2025-06-22','2025-06-30','Compensatory Time Off','Pending');
/*!40000 ALTER TABLE `leaves` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'motorph_eps'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-22 12:49:31
