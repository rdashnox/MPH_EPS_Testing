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
-- Table structure for table `payrollmph`
--

DROP TABLE IF EXISTS `payrollmph`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payrollmph` (
  `EID` int NOT NULL,
  `Last_Name` varchar(50) NOT NULL,
  `First_Name` varchar(50) NOT NULL,
  `Birthday` date DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `Phone_Number` varchar(20) DEFAULT NULL,
  `Username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `Password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `SSS_#` varchar(20) DEFAULT NULL,
  `Philhealth_#` varchar(20) DEFAULT NULL,
  `TIN_#` varchar(20) DEFAULT NULL,
  `Pag-ibig_#` varchar(20) DEFAULT NULL,
  `Status` varchar(20) DEFAULT NULL,
  `Designation` varchar(50) DEFAULT NULL,
  `Immediate_Supervisor` varchar(50) DEFAULT NULL,
  `Basic_Salary` decimal(10,2) DEFAULT NULL,
  `Rice_Subsidy` decimal(10,2) DEFAULT NULL,
  `Phone_Allowance` decimal(10,2) DEFAULT NULL,
  `Clothing_Allowance` decimal(10,2) DEFAULT NULL,
  `Half_Month_Rate` decimal(10,2) DEFAULT NULL,
  `Hourly_Rate` decimal(10,2) DEFAULT NULL,
  `Over_Time` int DEFAULT NULL,
  `Performance_Bonus` decimal(10,2) DEFAULT NULL,
  `Holiday_Pay` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payrollmph`
--

LOCK TABLES `payrollmph` WRITE;
/*!40000 ALTER TABLE `payrollmph` DISABLE KEYS */;
INSERT INTO `payrollmph` VALUES (10001,'Garcia','Manuel III','1983-10-11','Valero Carpark Building Valero Street 1227, Makati City','966-860-270','mgarcia@motorph.com','123abc','44-4506057-3','820126853951','442-605-657-000','691295330870','Regular','Chief Executive Officer','N/A',90009.00,1500.00,2000.00,1000.00,45000.00,535.71,2,5000.00,8571.36),(10002,'Lim','Antonio','1988-06-19','San Antonio De Padua 2, Block 1 Lot 8 and 2, Dasmarinas, Cavite','171-867-411','alim@motorph.com','adminako','52-2061274-9','331735646338','683-102-776-000','663904995411','Regular','Chief Operating Officer','Garcia, Manuel III',60000.00,1500.00,2000.00,1000.00,30000.00,357.14,2,5000.00,5714.24),(10003,'Aquino','Bianca Sofia','1989-08-04','Rm. 402 4/F Jiao Building Timog Avenue Cor. Quezon Avenue 1100, Quezon City','966-889-370','bsaquino@motorph.com','123abc','30-8870406-2','177451189665','971-711-280-000','171519773969','Regular','Chief Finance Officer','Garcia, Manuel III',60000.00,1500.00,2000.00,1000.00,30000.00,357.14,3,5000.00,5714.24),(10004,'Reyes','Isabella','1994-06-16','460 Solanda Street Intramuros 1000, Manila','786-868-477','ireyes@motorph.com','admin','40-2511815-0','341911411254','876-809-437-000','416946776041','Regular','Chief Marketing Officer','Garcia, Manuel III',60000.00,1500.00,2000.00,1000.00,30000.00,357.14,1,5000.00,5714.24),(10005,'Hernandez','Eduard','1989-09-23','National Highway, Gingoog,  Misamis Occidental','088-861-012','ehernandez@motorph.com','admin','50-5577638-1','957436191812','031-702-374-000','952347222457','Regular','IT Operations and Systems','Lim, Antonio',52670.00,1500.00,1000.00,1000.00,26335.00,313.51,3,5000.00,5016.16),(10006,'Villanueva','Andrea Mae','1988-02-14','17/85 Stracke Via Suite 042, Poblacion, Las PiÃ±as 4783 Dinagat Islands ','918-621-603','amvillanueva@motorph.com','123abc','49-1632020-8','382189453145','317-674-022-000','441093369646','Regular','HR Manager','Lim, Antonio',52670.00,1500.00,1000.00,1000.00,26335.00,313.51,2,5000.00,5016.16),(10007,'San Jose','Brad','1996-03-15','99 Strosin Hills, Poblacion, Bislig 5340 Tawi-Tawi','797-009-261','bsanjose@motorph.com','123abc','40-2400714-1','239192926939','672-474-690-000','210850209964','Regular','HR Team Leader','Villanueva, Andrea Mae',10000.00,1500.00,800.00,800.00,21488.00,255.80,3,5000.00,4092.80),(10008,'Romualdez','Alice','1992-05-14','12A/33 Upton Isle Apt. 420, Roxas City 1814 Surigao del Norte ','983-606-799','aromualdez@motorph.com','123abc','55-4476527-2','545652640232','888-572-294-000','211385556888','Regular','HR Rank and File','San, Jose Brad',22500.00,1500.00,500.00,500.00,11250.00,133.93,3,5000.00,2142.88),(10009,'Atienza','Rosie ','1948-09-24','90A Dibbert Terrace Apt. 190, San Lorenzo 6056 Davao del Norte','266-036-427','ratienza@motorph.com','emp123','41-0644692-3','708988234853','604-997-793-000','260107732354','Regular','HR Rank and File','San, Jose Brad',22500.00,1500.00,500.00,500.00,11250.00,133.93,2,5000.00,2142.88),(10010,'Alvaro','Roderick','1988-03-30','#284 T. Morato corner, Scout Rallos Street, Quezon City','053-381-386','ralvaro@motorph.com','123abc','64-7605054-4','578114853194','525-420-419-000','799254095212','Regular','Accounting Head','Aquino, Bianca Sofia ',52670.00,1500.00,1000.00,1000.00,26335.00,313.51,1,5000.00,5016.16),(10011,'Salcedo','Anthony','1993-09-14','93/54 Shanahan Alley Apt. 183, Santo Tomas 1572 Masbate','070-766-300','asalcedo@motorph.com','123abc','26-9647608-3','126445315651','210-805-911-000','218002473454','Regular','Payroll Manager','Alvaro, Roderick',50825.00,1500.00,1000.00,1000.00,25413.00,302.53,3,5000.00,4840.48),(10012,'Lopez','Josie ','1987-01-14','49 Springs Apt. 266, Poblacion, Taguig 3200 Occidental Mindoro','478-355-427','jlopez@motorph.com','123abc','44-8563448-3','431709011012','218-489-737-000','113071293354','Regular','Payroll Team Leader','Salcedo, Anthony',38475.00,1500.00,800.00,800.00,19238.00,229.02,2,5000.00,3664.32),(10013,'Farala','Martha','1942-01-11','42/25 Sawayn Stream, Ubay 1208 Zamboanga del Norte ','329-034-366','mfarala@motorph.com','123abc','45-5656375-0','233693897247','210-835-851-000','631130283546','Regular','Payroll Rank and File','Salcedo, Anthony',24000.00,1500.00,500.00,500.00,12000.00,142.86,3,5000.00,2285.76),(10014,'Martinez','Leila','1970-07-11','37/46 Kulas Roads, Maragondon 0962 Quirino ','877-110-749','lmartinez@motorph.com','employee','27-2090996-4','515741057496','275-792-513-000','101205445886','Regular','Payroll Rank and File','Salcedo, Anthony',24000.00,1500.00,500.00,500.00,12000.00,142.86,3,5000.00,2285.76),(10015,'Romualdez','Fredrick ','1985-03-10','22A/52 Lubowitz Meadows, Pililla 4895 Zambales','023-079-009','fromualdez@motorph.com','123abc','26-8768374-1','308366860059','598-065-761-000','223057707853','Regular','Account Manager','Lim, Antonio',53500.00,1500.00,1000.00,1000.00,26750.00,318.45,2,5000.00,5095.20),(10016,'Mata','Christian','1987-10-21','90 O\'Keefe Spur Apt. 379, Catigbian 2772 Sulu ','783-776-744','cmata@motorph.com','123abc','49-2959312-6','824187961962','103-100-522-000','631052853464','Regular','Account Team Leader','Romualdez, Fredrick ',42975.00,1500.00,800.00,800.00,21488.00,255.80,1,5000.00,4092.80),(10017,'De Leon','Selena ','1975-02-20','89A Armstrong Trace, Compostela 7874 Maguindanao','975-432-139','sdeleon@motorph.com','123abc','27-2090208-8','587272469938','482-259-498-000','719007608464','Regular','Account Team Leader','Romualdez, Fredrick ',41850.00,1500.00,800.00,800.00,20925.00,249.11,2,5000.00,3985.76),(10018,'San Jose','Allison ','1986-06-24','08 Grant Drive Suite 406, Poblacion, Iloilo City 9186 La Union','179-075-129','asanjose@motorph.com','123abc','45-3251383-0','745148459521','121-203-336-000','114901859343','Regular','Account Rank and File','Mata, Christian',22500.00,1500.00,500.00,500.00,11250.00,133.93,3,5000.00,2142.88),(10019,'Rosario','Cydney ','1996-10-06','93A/21 Berge Points, Tapaz 2180 Quezon','868-819-912','crosario@motorph.com','123abc','49-1629900-2','579253435499','122-244-511-000','265104358643','Regular','Account Rank and File','Mata, Christian',22500.00,1500.00,500.00,500.00,11250.00,133.93,3,5000.00,2142.88),(10020,'Bautista','Mark ','1991-02-12','65 Murphy Center Suite 094, Poblacion, Palayan 5636 Quirino','683-725-348','mbautista@motorph.com','123abc','49-1647342-5','399665157135','273-970-941-000','260054585575','Regular','Account Rank and File','Mata, Christian',23250.00,1500.00,500.00,500.00,11625.00,138.39,3,5000.00,2214.24),(10021,'Lazaro','Darlene ','1985-11-25','47A/94 Larkin Plaza Apt. 179, Poblacion, Caloocan 2751 Quirino','740-721-558','dlazaro@motorph.com','123abc','45-5617168-2','606386917510','354-650-951-000','104907708845','Probationary','Account Rank and File','Mata, Christian',23250.00,1500.00,500.00,500.00,11625.00,138.39,2,5000.00,2214.24),(10022,'Delos Santos','Kolby ','1980-02-26','06A Gulgowski Extensions, Bongabon 6085 Zamboanga del Sur','739-443-033','kdelosantos@motorph.com','123abc','52-0109570-6','357451271274','187-500-345-000','113017988667','Probationary','Account Rank and File','Mata, Christian',24000.00,1500.00,500.00,500.00,12000.00,142.86,1,5000.00,2285.76),(10023,'Santos','Vella ','1983-12-31','99A Padberg Spring, Poblacion, Mabalacat 3959 Lanao del Sur','955-879-269','vsantos@motorph.com','123abc','52-9883524-3','548670482885','101-558-994-000','360028104576','Probationary','Account Rank and File','Mata, Christian',22500.00,1500.00,500.00,500.00,11250.00,133.93,2,5000.00,2142.88),(10024,'Del Rosario','Tomas','1978-12-18','80A/48 Ledner Ridges, Poblacion, Kabankalan 8870 Marinduque','882-550-989','tdelrosario@motorph.com','123abc','45-5866331-6','953901539995','560-735-732-000','913108649964','Probationary','Account Rank and File','Mata, Christian',22500.00,1500.00,500.00,500.00,11250.00,133.93,2,5000.00,2142.88),(10025,'Tolentino','Jacklyn ','1984-05-19','96/48 Watsica Flats Suite 734, Poblacion, Malolos 1844 Ifugao','675-757-366','jtolentino@motorph.com','123abc','47-1692793-0','753800654114','841-177-857-000','210546661243','Probationary','Account Rank and File','De Leon, Selena',24000.00,1500.00,500.00,500.00,12000.00,142.86,1,5000.00,2285.76),(10026,'Gutierrez','Percival ','1970-12-18','58A Wilderman Walks, Poblacion, Digos 5822 Davao del Sur','512-899-876','pgutierrez@motorph.com','123abc','40-9504657-8','797639382265','502-995-671-000','210897095686','Probationary','Account Rank and File','De Leon, Selena',24750.00,1500.00,500.00,500.00,12375.00,147.32,3,5000.00,2357.12),(10027,'Manalaysay','Garfield ','1986-08-28','60 Goyette Valley Suite 219, Poblacion, Tabuk 3159 Lanao del Sur','948-628-136','gmanalaysay@motorph.com','123abc','45-3298166-4','810909286264','336-676-445-000','211274476563','Probationary','Account Rank and File','De Leon, Selena',24750.00,1500.00,500.00,500.00,12375.00,147.32,1,5000.00,2357.12),(10028,'Villegas','Lizeth ','1981-12-12','66/77 Mann Views, Luisiana 1263 Dinagat Islands','332-372-215','lvillegas@motorph.com','123abc','40-2400719-4','934389652994','210-395-397-000','122238077997','Probationary','Account Rank and File','De Leon, Selena',24000.00,1500.00,500.00,500.00,12000.00,142.86,3,5000.00,2285.76),(10029,'Ramos','Carol ','1978-08-20','72/70 Stamm Spurs, Bustos 4550 Iloilo','250-700-389','cramos@motorph.com','123abc','60-1152206-4','351830469744','395-032-717-000','212141893454','Probationary','Account Rank and File','De Leon, Selena',22500.00,1500.00,500.00,500.00,11250.00,133.93,1,5000.00,2142.88),(10030,'Maceda','Emelia ','1973-04-14','50A/83 Bahringer Oval Suite 145, Kiamba 7688 Nueva Ecija','973-358-041','emelia@motorph.com','123abc','54-1331005-0','465087894112','215-973-013-000','515012579765','Probationary','Account Rank and File','De Leon, Selena',22500.00,1500.00,500.00,500.00,11250.00,133.93,3,5000.00,2142.88),(10031,'Aguilar','Delia ','1989-01-27','95 Cremin Junction, Surallah 2809 Cotabato','529-705-439','daguilar@motorph.com','123abc','52-1859253-1','136451303068','599-312-588-000','110018813465','Probationary','Account Rank and File','De Leon, Selena',22500.00,1500.00,500.00,500.00,11250.00,133.93,1,5000.00,2142.88),(10032,'Castro','John Rafael','1992-02-09','Hi-way, Yati, Liloan Cebu','332-424-955 ','jrcastro@motorph.com','123abc','26-7145133-4','601644902402','404-768-309-000','697764069311','Regular','Sales & Marketing','Reyes, Isabella',52670.00,1500.00,1000.00,1000.00,26335.00,313.51,3,5000.00,5016.16),(10033,'Martinez','Carlos Ian','1990-11-16','Bulala, Camalaniugan','078-854-208','cimartinez@motorph.com','123abc','11-5062972-7','380685387212','256-436-296-000','993372963726','Regular','Supply Chain and Logistics','Reyes, Isabella',52670.00,1500.00,1000.00,1000.00,26335.00,313.51,2,5000.00,5016.16),(10034,'Santos','Beatriz','1990-08-07','Agapita Building, Metro Manila','526-639-511','bsantos@motorph.com','123abc','20-2987501-5','918460050077','911-529-713-000','874042259378','Regular','Customer Service and Relations','Reyes, Isabella',52670.00,1500.00,1000.00,1000.00,26335.00,313.51,3,5000.00,5016.16);
/*!40000 ALTER TABLE `payrollmph` ENABLE KEYS */;
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

-- Dump completed on 2025-03-28 12:32:40
