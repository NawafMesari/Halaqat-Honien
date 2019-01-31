-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 16, 2019 at 09:05 PM
-- Server version: 5.7.20-log
-- PHP Version: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `haneen_halaqat`
--

-- --------------------------------------------------------

--
-- Table structure for table `guardian`
--

CREATE TABLE `guardian` (
  `G_id` int(11) NOT NULL,
  `G_name` varchar(45) NOT NULL,
  `Phone_num` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `guardian`
--

INSERT INTO `guardian` (`G_id`, `G_name`, `Phone_num`) VALUES
(1, 'أحمد عبدالوهاب ', '0503197375'),
(2, 'عبدالعزيز محمد', '0545344645'),
(3, 'مراد أحمد', '0545436778'),
(4, 'محمد سالم', '0557944278'),
(5, 'محمد الهندي', '0504934660'),
(6, 'سرور العموقي', '052454712');

-- --------------------------------------------------------

--
-- Table structure for table `halaqat`
--

CREATE TABLE `halaqat` (
  `H_id` int(11) NOT NULL,
  `H_name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `halaqat`
--

INSERT INTO `halaqat` (`H_id`, `H_name`) VALUES
(1, 'الابتدائي'),
(2, 'المتوسط'),
(3, 'الثانوي');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `S_id` int(11) NOT NULL,
  `F_name` varchar(20) NOT NULL,
  `S_name` varchar(20) NOT NULL,
  `L_name` varchar(20) NOT NULL,
  `Age` int(11) NOT NULL,
  `Birth_date` date NOT NULL,
  `Landline_num` varchar(10) DEFAULT NULL,
  `Memorized_chapters` int(11) NOT NULL,
  `Halaqah_id` int(11) DEFAULT NULL,
  `quardian_id` int(11) DEFAULT NULL,
  `Flag` tinyint(4) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`S_id`, `F_name`, `S_name`, `L_name`, `Age`, `Birth_date`, `Landline_num`, `Memorized_chapters`, `Halaqah_id`, `quardian_id`, `Flag`) VALUES
(1, 'عبدالوهاب', 'أحمد', 'الهندي', 22, '1417-01-01', '0114285896', 20, 3, 1, 1),
(2, 'نواف', 'عبدالعزيز', 'ميسري', 22, '1417-04-01', '0113445345', 9, 3, 2, 1),
(3, 'بدر', 'مراد', 'بانبيلة', 23, '1418-04-06', '0115693254', 18, 1, 3, 1),
(4, 'عبدالسلام', 'أحمد', 'الهندي', 19, '1420-12-07', '0114285896', 2, 2, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `teach`
--

CREATE TABLE `teach` (
  `S_id` int(11) NOT NULL,
  `T_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

CREATE TABLE `teacher` (
  `T_id` int(11) NOT NULL,
  `T_name` varchar(45) NOT NULL,
  `Phone_num` varchar(10) NOT NULL,
  `Salary` int(11) NOT NULL,
  `Maneger_id` int(11) DEFAULT NULL,
  `Flag` tinyint(4) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` (`T_id`, `T_name`, `Phone_num`, `Salary`, `Maneger_id`, `Flag`) VALUES
(1, 'مجاهد الأنصاري', '0564435684', 2000, NULL, 1),
(2, 'سعد الهديان', '0543548994', 2500, 1, 1),
(3, 'عبدالرحمن النشوان', '0548249221', 4000, 1, 1),
(4, 'عبدالملك الغيث', '0503569323', 1500, 1, 1),
(5, 'خالد عبدالله السلطان', '0554877234', 2400, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `teach_in`
--

CREATE TABLE `teach_in` (
  `T_id` int(11) NOT NULL,
  `H_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `guardian`
--
ALTER TABLE `guardian`
  ADD PRIMARY KEY (`G_id`);

--
-- Indexes for table `halaqat`
--
ALTER TABLE `halaqat`
  ADD PRIMARY KEY (`H_id`),
  ADD UNIQUE KEY `H_id_UNIQUE` (`H_id`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`S_id`),
  ADD UNIQUE KEY `s_id_UNIQUE` (`S_id`),
  ADD KEY `_idx` (`Halaqah_id`),
  ADD KEY `Fkey2_idx` (`quardian_id`);

--
-- Indexes for table `teach`
--
ALTER TABLE `teach`
  ADD PRIMARY KEY (`S_id`,`T_id`),
  ADD KEY `fg5_idx` (`T_id`);

--
-- Indexes for table `teacher`
--
ALTER TABLE `teacher`
  ADD PRIMARY KEY (`T_id`),
  ADD UNIQUE KEY `T_id_UNIQUE` (`T_id`),
  ADD KEY `FG_idx` (`Maneger_id`);

--
-- Indexes for table `teach_in`
--
ALTER TABLE `teach_in`
  ADD PRIMARY KEY (`T_id`,`H_id`),
  ADD KEY `fg3_idx` (`H_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `guardian`
--
ALTER TABLE `guardian`
  MODIFY `G_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `halaqat`
--
ALTER TABLE `halaqat`
  MODIFY `H_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `S_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `teacher`
--
ALTER TABLE `teacher`
  MODIFY `T_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `Fkey` FOREIGN KEY (`Halaqah_id`) REFERENCES `halaqat` (`H_id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `Fkey2` FOREIGN KEY (`quardian_id`) REFERENCES `guardian` (`G_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `teach`
--
ALTER TABLE `teach`
  ADD CONSTRAINT `fg4` FOREIGN KEY (`S_id`) REFERENCES `student` (`S_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fg5` FOREIGN KEY (`T_id`) REFERENCES `teacher` (`T_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `teacher`
--
ALTER TABLE `teacher`
  ADD CONSTRAINT `FG` FOREIGN KEY (`Maneger_id`) REFERENCES `teacher` (`T_id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `teach_in`
--
ALTER TABLE `teach_in`
  ADD CONSTRAINT `fg2` FOREIGN KEY (`T_id`) REFERENCES `teacher` (`T_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fg3` FOREIGN KEY (`H_id`) REFERENCES `halaqat` (`H_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
