-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               5.6.35-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for vendingmachines
DROP DATABASE IF EXISTS `vendingmachines`;
CREATE DATABASE IF NOT EXISTS `vendingmachines` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `vendingmachines`;

-- Dumping structure for table vendingmachines.inventory
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE IF NOT EXISTS `inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productId` int(11) NOT NULL,
  `currentNumber` int(11) NOT NULL,
  `maxNumber` int(11) NOT NULL,
  `price` int(11) NOT NULL,
  `machineId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table vendingmachines.inventory: ~0 rows (approximately)
DELETE FROM `inventory`;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` (`id`, `productId`, `currentNumber`, `maxNumber`, `price`, `machineId`) VALUES
	(1, 1, 5, 10, 65, 1),
	(2, 2, 5, 10, 100, 1),
	(3, 3, 8, 10, 150, 1);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;

-- Dumping structure for table vendingmachines.machine
DROP TABLE IF EXISTS `machine`;
CREATE TABLE IF NOT EXISTS `machine` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `insertedValue` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- Dumping data for table vendingmachines.machine: ~0 rows (approximately)
DELETE FROM `machine`;
/*!40000 ALTER TABLE `machine` DISABLE KEYS */;
INSERT INTO `machine` (`id`, `name`, `insertedValue`) VALUES
	(1, 'TestVendingMachine', 0);
/*!40000 ALTER TABLE `machine` ENABLE KEYS */;

-- Dumping structure for table vendingmachines.product
DROP TABLE IF EXISTS `product`;
CREATE TABLE IF NOT EXISTS `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- Dumping data for table vendingmachines.product: ~0 rows (approximately)
DELETE FROM `product`;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`id`, `name`) VALUES
	(1, 'Item A'),
	(2, 'Item B'),
	(3, 'Item C');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

-- Dumping structure for table vendingmachines.register
DROP TABLE IF EXISTS `register`;
CREATE TABLE IF NOT EXISTS `register` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `currencyName` varchar(45) NOT NULL,
  `currencyValue` int(11) NOT NULL,
  `currentNumber` int(11) NOT NULL,
  `maxNumber` int(11) NOT NULL,
  `machineId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table vendingmachines.register: ~0 rows (approximately)
DELETE FROM `register`;
/*!40000 ALTER TABLE `register` DISABLE KEYS */;
INSERT INTO `register` (`id`, `currencyName`, `currencyValue`, `currentNumber`, `maxNumber`, `machineId`) VALUES
	(1, 'Nickel', 5, 50, 70, 1),
	(2, 'Dime', 10, 30, 120, 1),
	(3, 'Quarter', 25, 20, 50, 1),
	(4, 'Dollar', 100, 10, 20, 1);
/*!40000 ALTER TABLE `register` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
