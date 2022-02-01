-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: smartschoolingdev
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `academic_details`
--

DROP TABLE IF EXISTS `academic_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `academic_details` (
  `academic_id` varchar(100) NOT NULL,
  `academic_year` varchar(100) NOT NULL,
  `display_name` varchar(100) NOT NULL,
  `academic_start_date` datetime NOT NULL,
  `academic_end_date` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_update_user` varchar(100) NOT NULL,
  PRIMARY KEY (`academic_id`),
  UNIQUE KEY `academic_details_un` (`academic_year`,`display_name`,`academic_id`,`academic_start_date`,`academic_end_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `academic_details`
--

LOCK TABLES `academic_details` WRITE;
/*!40000 ALTER TABLE `academic_details` DISABLE KEYS */;
INSERT INTO `academic_details` VALUES ('AY-2018-19','AY-2018-19','Academic Year 2018-19','2018-06-15 00:00:00','2019-05-15 00:00:00','2022-01-28 00:00:00','BASE'),('AY-2019-20','AY-2019-20','Academic Year 2019-20','2019-06-15 00:00:00','2020-05-15 00:00:00','2022-01-28 00:00:00','BASE'),('AY-2020-21','AY-2020-21','Academic Year 2020-21','2020-06-15 00:00:00','2021-05-15 00:00:00','2022-01-28 00:00:00','BASE'),('AY-2021-22','AY-2021-22','AY-2021-22','2021-06-15 00:00:00','2022-05-15 00:00:00','2020-01-19 00:00:00','base');
/*!40000 ALTER TABLE `academic_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `account_id` varchar(100) NOT NULL,
  `account_name` varchar(100) NOT NULL,
  `bank_name` varchar(100) DEFAULT NULL,
  `bank_account_number` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branch_classes_details`
--

DROP TABLE IF EXISTS `branch_classes_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `branch_classes_details` (
  `branch_id` varchar(100) NOT NULL,
  `class_id` varchar(100) NOT NULL,
  `eff_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`branch_id`,`class_id`,`eff_date`,`end_date`),
  KEY `branch_classes_details_fk_1` (`class_id`),
  CONSTRAINT `branch_classes_details_fk` FOREIGN KEY (`branch_id`) REFERENCES `institute_branch_det` (`branch_id`),
  CONSTRAINT `branch_classes_details_fk_1` FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branch_classes_details`
--

LOCK TABLES `branch_classes_details` WRITE;
/*!40000 ALTER TABLE `branch_classes_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `branch_classes_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class_subject_details`
--

DROP TABLE IF EXISTS `class_subject_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class_subject_details` (
  `class_id` varchar(100) NOT NULL,
  `sub_id` varchar(100) NOT NULL,
  `eff_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`class_id`,`sub_id`,`eff_date`,`end_date`),
  KEY `class_subject_details_fk_1` (`sub_id`),
  CONSTRAINT `class_subject_details_fk` FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`),
  CONSTRAINT `class_subject_details_fk_1` FOREIGN KEY (`sub_id`) REFERENCES `subjects` (`sub_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class_subject_details`
--

LOCK TABLES `class_subject_details` WRITE;
/*!40000 ALTER TABLE `class_subject_details` DISABLE KEYS */;
INSERT INTO `class_subject_details` VALUES ('1st','ENG','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('1st','MAR','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('1st','MATHS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('2nd','ENG','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('2nd','MAR','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('2nd','MATHS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('3rd','ENG','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('3rd','EVS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('3rd','MAR','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('3rd','MATHS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('3rd','SCI','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('4th','ENG','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('4th','EVS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('4th','MAR','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('4th','MATHS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('4th','SCI','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('5th','ENG','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('5th','GEO','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('5th','HIST','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('5th','MAR','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('5th','MATHS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('5th','SCI','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('6th','ENG','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('6th','GEO','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('6th','HIST','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('6th','MAR','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('6th','MATHS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('6th','SCI','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('LKG','ENG','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('LKG','MAR','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('LKG','MATHS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('UKG','ENG','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('UKG','MAR','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('UKG','MATHS','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE');
/*!40000 ALTER TABLE `class_subject_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classes`
--

DROP TABLE IF EXISTS `classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `classes` (
  `class_id` varchar(100) NOT NULL,
  `class_name` varchar(100) NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classes`
--

LOCK TABLES `classes` WRITE;
/*!40000 ALTER TABLE `classes` DISABLE KEYS */;
INSERT INTO `classes` VALUES ('1st','First Std','2022-01-08 00:00:00','BASE'),('2nd','Second Std','2022-01-08 00:00:00','BASE'),('3rd','Third Std','2022-01-08 00:00:00','BASE'),('4th','Forth std','2022-01-08 00:00:00','BASE'),('5th','Fifth Std','2022-01-26 00:00:00','BASE'),('6th','Sixth Std','2022-01-27 00:00:00','BASE'),('LKG','Lower KG','2022-01-08 00:00:00','BASE'),('UKG','Upper KG','2022-01-08 00:00:00','BASE');
/*!40000 ALTER TABLE `classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fee_details`
--

DROP TABLE IF EXISTS `fee_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fee_details` (
  `fee_id` varchar(100) NOT NULL,
  `class_id` varchar(100) NOT NULL,
  `route_id` varchar(100) NOT NULL,
  `amount` decimal(10,0) NOT NULL,
  `eff_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  PRIMARY KEY (`fee_id`,`class_id`,`route_id`,`amount`,`eff_date`,`end_date`),
  CONSTRAINT `fee_details_fk_2` FOREIGN KEY (`fee_id`) REFERENCES `fee_types` (`fee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fee_details`
--

LOCK TABLES `fee_details` WRITE;
/*!40000 ALTER TABLE `fee_details` DISABLE KEYS */;
INSERT INTO `fee_details` VALUES ('1','1st',' ',3000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('1','2nd',' ',4000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('1','3rd',' ',5000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('1','4th',' ',6000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('1','5th',' ',7000,'2022-01-27 00:00:00','2099-12-31 00:00:00'),('1','6th',' ',8000,'2022-01-27 00:00:00','2099-12-31 00:00:00'),('1','LKG',' ',1000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('1','UKG',' ',2000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('2',' ','Ghotan-Erandgaon',3000,'2022-01-27 00:00:00','2099-12-31 00:00:00'),('2',' ','Ghotan-Jalana',10000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('2',' ','Ghotan-Khanapur',4000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('2',' ','Ghotan-Shevgaon',6000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('3',' ',' ',4000,'2022-01-25 00:00:00','2099-12-31 00:00:00'),('4',' ',' ',2000,'2022-01-25 00:00:00','2099-12-31 00:00:00');
/*!40000 ALTER TABLE `fee_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fee_types`
--

DROP TABLE IF EXISTS `fee_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fee_types` (
  `fee_id` varchar(100) NOT NULL,
  `fee_name` varchar(100) NOT NULL,
  `fee_discription` varchar(100) DEFAULT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`fee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fee_types`
--

LOCK TABLES `fee_types` WRITE;
/*!40000 ALTER TABLE `fee_types` DISABLE KEYS */;
INSERT INTO `fee_types` VALUES ('1','Tution Fees','Tution fees for the classes','2022-01-08 00:00:00','BASE'),('2','Transport Fees','Transport fees for the bus facility','2022-01-08 00:00:00','BASE'),('3','Addmission Fees','Admission fee for the academic year','2022-01-08 00:00:00','BASE'),('4','Miscillanious Fees','','2022-01-08 00:00:00','BASE');
/*!40000 ALTER TABLE `fee_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `institute_branch_det`
--

DROP TABLE IF EXISTS `institute_branch_det`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `institute_branch_det` (
  `branch_id` varchar(100) NOT NULL,
  `institute_id` varchar(100) NOT NULL,
  `branch_name` varchar(200) NOT NULL,
  `foundation_date` datetime NOT NULL,
  `address` varchar(200) NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`branch_id`),
  KEY `institute_branch_det_fk` (`institute_id`),
  CONSTRAINT `institute_branch_det_fk` FOREIGN KEY (`institute_id`) REFERENCES `institute_det` (`institute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `institute_branch_det`
--

LOCK TABLES `institute_branch_det` WRITE;
/*!40000 ALTER TABLE `institute_branch_det` DISABLE KEYS */;
INSERT INTO `institute_branch_det` VALUES ('1','1','Test Branch 1','2020-01-01 00:00:00','Ghotan,Shevgaon,414502','2022-01-07 00:00:00','BASE'),('2','1','Test Branch 2','2021-04-28 00:00:00','Shevgaon,Shevgaon,414502','2022-01-07 00:00:00','BASE');
/*!40000 ALTER TABLE `institute_branch_det` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `institute_det`
--

DROP TABLE IF EXISTS `institute_det`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `institute_det` (
  `institute_id` varchar(100) NOT NULL,
  `institute_name` varchar(500) NOT NULL,
  `foundation_date` datetime NOT NULL,
  `address` varchar(500) NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`institute_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `institute_det`
--

LOCK TABLES `institute_det` WRITE;
/*!40000 ALTER TABLE `institute_det` DISABLE KEYS */;
INSERT INTO `institute_det` VALUES ('1','Test Institute','2020-01-01 00:00:00','Ghotan,Shevgaon,414502','2022-01-07 00:00:00','BASE');
/*!40000 ALTER TABLE `institute_det` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ref_table_types`
--

DROP TABLE IF EXISTS `ref_table_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ref_table_types` (
  `ref_table_type` varchar(10) NOT NULL,
  `ref_table_name` varchar(100) NOT NULL,
  PRIMARY KEY (`ref_table_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ref_table_types`
--

LOCK TABLES `ref_table_types` WRITE;
/*!40000 ALTER TABLE `ref_table_types` DISABLE KEYS */;
/*!40000 ALTER TABLE `ref_table_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `routes`
--

DROP TABLE IF EXISTS `routes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `routes` (
  `route_id` varchar(100) NOT NULL,
  `source` varchar(100) NOT NULL,
  `destination` varchar(100) NOT NULL,
  `distance` decimal(10,0) NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`route_id`),
  UNIQUE KEY `routes_un` (`route_id`,`source`,`destination`,`distance`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='	';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `routes`
--

LOCK TABLES `routes` WRITE;
/*!40000 ALTER TABLE `routes` DISABLE KEYS */;
INSERT INTO `routes` VALUES ('Ghotan-Erandgaon','Ghotan','Erandgaon',5,'2022-01-27 00:00:00','BASE'),('Ghotan-Jalana','Ghotan','Jalana',120,'2022-01-25 00:00:00','BASE'),('Ghotan-Khanapur','Ghotan','Khanapur',15,'2022-01-19 00:00:00','BASE'),('Ghotan-Shevgaon','Ghotan','Shevgaon',10,'2022-01-19 00:00:00','BASE');
/*!40000 ALTER TABLE `routes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_class_details`
--

DROP TABLE IF EXISTS `student_class_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_class_details` (
  `stud_id` varchar(100) NOT NULL,
  `academic_id` varchar(100) NOT NULL,
  `class_id` varchar(100) NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`stud_id`,`academic_id`,`class_id`),
  KEY `student_class_details_fk` (`class_id`),
  KEY `student_class_details_fk_2` (`academic_id`),
  CONSTRAINT `student_class_details_fk` FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`),
  CONSTRAINT `student_class_details_fk_1` FOREIGN KEY (`stud_id`) REFERENCES `student_details` (`stud_id`),
  CONSTRAINT `student_class_details_fk_2` FOREIGN KEY (`academic_id`) REFERENCES `academic_details` (`academic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_class_details`
--

LOCK TABLES `student_class_details` WRITE;
/*!40000 ALTER TABLE `student_class_details` DISABLE KEYS */;
INSERT INTO `student_class_details` VALUES ('1','AY-2021-22','1st','2022-01-19 00:00:00','BASE'),('10','AY-2021-22','1st','2022-01-24 00:00:00','BASE'),('11','AY-2021-22','2nd','2022-01-29 00:00:00','BASE'),('12','AY-2021-22','3rd','2022-01-24 00:00:00','BASE'),('13','AY-2021-22','4th','2022-01-29 00:00:00','BASE'),('2','AY-2021-22','2nd','2022-01-19 00:00:00','BASE'),('3','AY-2021-22','4th','2022-01-19 00:00:00','BASE'),('4','AY-2021-22','3rd','2022-01-19 00:00:00','BASE'),('5','AY-2021-22','LKG','2022-01-20 00:00:00','BASE'),('6','AY-2021-22','UKG','2022-01-29 00:00:00','BASE'),('7','AY-2021-22','LKG','2022-01-29 00:00:00','BASE'),('8','AY-2021-22','UKG','2022-01-21 00:00:00','BASE'),('9','AY-2021-22','1st','2022-01-21 00:00:00','BASE');
/*!40000 ALTER TABLE `student_class_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_details`
--

DROP TABLE IF EXISTS `student_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_details` (
  `stud_id` varchar(100) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `middle_name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) NOT NULL,
  `birth_date` datetime NOT NULL,
  `gender` varchar(100) DEFAULT NULL,
  `adhar` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mobile` varchar(100) NOT NULL,
  `alternate_mobile` varchar(100) DEFAULT NULL,
  `address` varchar(500) NOT NULL,
  `religion` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `caste` varchar(100) DEFAULT NULL,
  `transport` char(1) NOT NULL,
  `nationality` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`stud_id`),
  UNIQUE KEY `student_details_un` (`adhar`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_details`
--

LOCK TABLES `student_details` WRITE;
/*!40000 ALTER TABLE `student_details` DISABLE KEYS */;
INSERT INTO `student_details` VALUES ('1','Mahesh','Keshavrao','Ghuge','0098-05-22 00:00:00','Male','121212121212',NULL,'8600429732',NULL,'Ghotan','Hindu','NT-D','Y','Indian'),('10','Shubham','','Joshi','0099-06-08 00:00:00','Male','8965874585',NULL,'9856985478',NULL,'Jalana','Hindu','Open','N','Indian'),('11','Test','','Student 1','0098-11-04 00:00:00','Female','145144447787',NULL,'8574145236',NULL,'Ghotan','Hindu','NT-A','Y','Indian'),('12','Test','','Student 2','0098-08-07 00:00:00','Female','859685857485',NULL,'8596857415',NULL,'Ghotan','Hindu','NT-B','N','Indian'),('13','Test','','Student3','2002-01-12 00:00:00','Male','785588878787','','8596857415',NULL,'Khanapur','Hindu','OBC','Y','Indian'),('2','Abhijeet','','Jaybhaye','2001-11-18 00:00:00','Male','454545454545',NULL,'8959656585',NULL,'Shevgaon','Hindu','NT-D','Y','Indian'),('3','Nikhil','','Jaybhaye','2004-12-08 00:00:00','Male','896585696523',NULL,'6985632541',NULL,'Shevgaon','Hindu','NT-D','Y','Indian'),('4','Aditya','','Dikshit','0097-02-08 00:00:00','Male','565895474585',NULL,'7852547852',NULL,'Nashik','Hindu','Open','N','Indian'),('5','Harshavardhan','Dadasaheb','Garje','2010-02-10 00:00:00','Male','458596585236',NULL,'8596858745',NULL,'Ghotan','Hindu','NT-D','Y','Indian'),('6','Shruti','Kishor','Jaybhaye','2010-05-15 00:00:00','Female','111111111111',NULL,'5698785478',NULL,'Shevgaon','Hindu','NT-D','Y','Indian'),('7','Sai','Vishal','Sanap','2012-08-31 00:00:00','Male','8958658969',NULL,'5896985658',NULL,'Shevgaon','Hindu','NT-D','Y','Indian'),('8','Ayush','','Garje','2015-04-24 00:00:00','Male','589654125632',NULL,'4785585214',NULL,'Ghotan','Hindu','NT-D','Y','Indian'),('9','Akshay','','Garje','0097-06-08 00:00:00','Male','458574856963',NULL,'6985474412',NULL,'Padali','Hindu','OBC','Y','Indian');
/*!40000 ALTER TABLE `student_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_fees_details`
--

DROP TABLE IF EXISTS `student_fees_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_fees_details` (
  `stud_id` varchar(100) NOT NULL,
  `academic_id` varchar(100) NOT NULL,
  `fee_id` varchar(100) NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`stud_id`,`academic_id`,`fee_id`),
  KEY `student_fees_details_fk_1` (`fee_id`),
  KEY `student_fees_details_fk_2` (`academic_id`),
  CONSTRAINT `student_fees_details_fk` FOREIGN KEY (`stud_id`) REFERENCES `student_details` (`stud_id`),
  CONSTRAINT `student_fees_details_fk_1` FOREIGN KEY (`fee_id`) REFERENCES `fee_types` (`fee_id`),
  CONSTRAINT `student_fees_details_fk_2` FOREIGN KEY (`academic_id`) REFERENCES `academic_details` (`academic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_fees_details`
--

LOCK TABLES `student_fees_details` WRITE;
/*!40000 ALTER TABLE `student_fees_details` DISABLE KEYS */;
INSERT INTO `student_fees_details` VALUES ('1','AY-2021-22','1','2022-01-19 00:00:00','BASE'),('1','AY-2021-22','2','2022-01-19 00:00:00','BASE'),('1','AY-2021-22','3','2022-01-19 00:00:00','BASE'),('10','AY-2021-22','1','2022-01-24 00:00:00','BASE'),('10','AY-2021-22','3','2022-01-24 00:00:00','BASE'),('11','AY-2021-22','1','2022-01-29 00:00:00','BASE'),('11','AY-2021-22','2','2022-01-29 00:00:00','BASE'),('11','AY-2021-22','3','2022-01-29 00:00:00','BASE'),('12','AY-2021-22','1','2022-01-24 00:00:00','BASE'),('12','AY-2021-22','3','2022-01-24 00:00:00','BASE'),('13','AY-2021-22','1','2022-01-29 00:00:00','BASE'),('13','AY-2021-22','2','2022-01-29 00:00:00','BASE'),('13','AY-2021-22','3','2022-01-29 00:00:00','BASE'),('13','AY-2021-22','4','2022-01-29 00:00:00','BASE'),('2','AY-2021-22','1','2022-01-19 00:00:00','BASE'),('2','AY-2021-22','2','2022-01-19 00:00:00','BASE'),('2','AY-2021-22','3','2022-01-19 00:00:00','BASE'),('3','AY-2021-22','1','2022-01-19 00:00:00','BASE'),('3','AY-2021-22','2','2022-01-19 00:00:00','BASE'),('3','AY-2021-22','3','2022-01-19 00:00:00','BASE'),('4','AY-2021-22','1','2022-01-19 00:00:00','BASE'),('4','AY-2021-22','3','2022-01-19 00:00:00','BASE'),('5','AY-2021-22','1','2022-01-20 00:00:00','BASE'),('5','AY-2021-22','2','2022-01-20 00:00:00','BASE'),('5','AY-2021-22','3','2022-01-20 00:00:00','BASE'),('6','AY-2021-22','1','2022-01-29 00:00:00','BASE'),('6','AY-2021-22','2','2022-01-29 00:00:00','BASE'),('6','AY-2021-22','3','2022-01-29 00:00:00','BASE'),('7','AY-2021-22','1','2022-01-29 00:00:00','BASE'),('7','AY-2021-22','2','2022-01-29 00:00:00','BASE'),('7','AY-2021-22','3','2022-01-29 00:00:00','BASE'),('8','AY-2021-22','1','2022-01-21 00:00:00','BASE'),('8','AY-2021-22','2','2022-01-21 00:00:00','BASE'),('8','AY-2021-22','3','2022-01-21 00:00:00','BASE'),('9','AY-2021-22','1','2022-01-21 00:00:00','BASE'),('9','AY-2021-22','2','2022-01-21 00:00:00','BASE'),('9','AY-2021-22','3','2022-01-21 00:00:00','BASE');
/*!40000 ALTER TABLE `student_fees_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_transport_details`
--

DROP TABLE IF EXISTS `student_transport_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_transport_details` (
  `stud_id` varchar(100) NOT NULL,
  `route_id` varchar(100) NOT NULL,
  `eff_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`stud_id`,`route_id`,`eff_date`,`end_date`),
  KEY `student_transport_detaisl_fk_1` (`route_id`),
  CONSTRAINT `student_transport_detaisl_fk` FOREIGN KEY (`stud_id`) REFERENCES `student_details` (`stud_id`),
  CONSTRAINT `student_transport_detaisl_fk_1` FOREIGN KEY (`route_id`) REFERENCES `routes` (`route_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_transport_details`
--

LOCK TABLES `student_transport_details` WRITE;
/*!40000 ALTER TABLE `student_transport_details` DISABLE KEYS */;
INSERT INTO `student_transport_details` VALUES ('1','Ghotan-Khanapur','2022-01-19 00:00:00','2099-12-31 00:00:00','2022-01-19 00:00:00','BASE'),('11','Ghotan-Khanapur','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('13','Ghotan-Khanapur','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('2','Ghotan-Shevgaon','2022-01-19 00:00:00','2099-12-31 00:00:00','2022-01-19 00:00:00','BASE'),('3','Ghotan-Shevgaon','2022-01-19 00:00:00','2099-12-31 00:00:00','2022-01-19 00:00:00','BASE'),('5','Ghotan-Khanapur','2022-01-20 00:00:00','2099-12-31 00:00:00','2022-01-20 00:00:00','BASE'),('6','Ghotan-Shevgaon','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('7','Ghotan-Shevgaon','2022-01-29 00:00:00','2099-12-31 00:00:00','2022-01-29 00:00:00','BASE'),('8','Ghotan-Khanapur','2022-01-21 00:00:00','2099-12-31 00:00:00','2022-01-21 00:00:00','BASE'),('9','Ghotan-Shevgaon','2022-01-21 00:00:00','2099-12-31 00:00:00','2022-01-21 00:00:00','BASE');
/*!40000 ALTER TABLE `student_transport_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students_fees_collections_details`
--

DROP TABLE IF EXISTS `students_fees_collections_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students_fees_collections_details` (
  `collection_id` varchar(100) NOT NULL,
  `stud_id` varchar(100) NOT NULL,
  `academic_id` varchar(100) NOT NULL,
  `fee_id` varchar(100) NOT NULL,
  `amount` double NOT NULL,
  `collection_date` datetime NOT NULL,
  `account_id` varchar(100) NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`collection_id`),
  KEY `students_fees_collections_details_fk` (`stud_id`),
  KEY `students_fees_collections_details_fk_1` (`academic_id`),
  KEY `students_fees_collections_details_fk_2` (`fee_id`),
  KEY `students_fees_collections_details_fk_3` (`account_id`),
  CONSTRAINT `students_fees_collections_details_fk` FOREIGN KEY (`stud_id`) REFERENCES `student_details` (`stud_id`),
  CONSTRAINT `students_fees_collections_details_fk_1` FOREIGN KEY (`academic_id`) REFERENCES `academic_details` (`academic_id`),
  CONSTRAINT `students_fees_collections_details_fk_2` FOREIGN KEY (`fee_id`) REFERENCES `fee_types` (`fee_id`),
  CONSTRAINT `students_fees_collections_details_fk_3` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students_fees_collections_details`
--

LOCK TABLES `students_fees_collections_details` WRITE;
/*!40000 ALTER TABLE `students_fees_collections_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `students_fees_collections_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subjects`
--

DROP TABLE IF EXISTS `subjects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subjects` (
  `sub_id` varchar(100) NOT NULL,
  `sub_name` varchar(100) NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`sub_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subjects`
--

LOCK TABLES `subjects` WRITE;
/*!40000 ALTER TABLE `subjects` DISABLE KEYS */;
INSERT INTO `subjects` VALUES ('ENG','English','2022-01-08 00:00:00','BASE'),('EVS','Environmental Studies','2022-01-08 00:00:00','BASE'),('GEO','Geography','2022-01-28 00:00:00','BASE'),('HIST','History','2022-01-28 00:00:00','BASE'),('MAR','Marathi','2022-01-08 00:00:00','BASE'),('MATHS','Mathematics','2022-01-08 00:00:00','BASE'),('SCI','Science','2022-01-08 00:00:00','BASE');
/*!40000 ALTER TABLE `subjects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transaction_details`
--

DROP TABLE IF EXISTS `transaction_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_details` (
  `transaction_det_id` varchar(100) NOT NULL,
  `transaction_id` varchar(100) NOT NULL,
  `account_id` varchar(100) NOT NULL,
  `transaction_type` char(1) NOT NULL,
  `ref_id` varchar(100) DEFAULT NULL,
  `ref_table_type` varchar(10) DEFAULT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`transaction_det_id`),
  KEY `transaction_details_fk` (`account_id`),
  KEY `transaction_details_fk_1` (`ref_table_type`),
  KEY `transaction_details_fk_2` (`transaction_id`),
  CONSTRAINT `transaction_details_fk` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
  CONSTRAINT `transaction_details_fk_1` FOREIGN KEY (`ref_table_type`) REFERENCES `ref_table_types` (`ref_table_type`),
  CONSTRAINT `transaction_details_fk_2` FOREIGN KEY (`transaction_id`) REFERENCES `transactions` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_details`
--

LOCK TABLES `transaction_details` WRITE;
/*!40000 ALTER TABLE `transaction_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `transaction_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `transaction_id` varchar(100) NOT NULL,
  `amount` double NOT NULL,
  `transaction_date` datetime NOT NULL,
  `last_update_time` datetime NOT NULL,
  `last_user` varchar(100) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_type` varchar(100) NOT NULL,
  `user_role` varchar(100) NOT NULL,
  `user_description` varchar(100) NOT NULL,
  PRIMARY KEY (`user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'smartschoolingdev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-01 13:57:32
