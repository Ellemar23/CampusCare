-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 28, 2025 at 03:43 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `campuscare`
--

-- --------------------------------------------------------

--
-- Table structure for table `activity_log`
--

CREATE TABLE `activity_log` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `activity_type` varchar(50) NOT NULL,
  `description` text NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE `appointments` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `doctor_name` varchar(100) NOT NULL,
  `date` date NOT NULL,
  `time` varchar(20) NOT NULL,
  `type` varchar(50) NOT NULL,
  `reason` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `username` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`id`, `user_id`, `doctor_name`, `date`, `time`, `type`, `reason`, `created_at`, `username`) VALUES
(44, 8, 'Dr. Ramirez', '2025-05-22', '1:00 PM', 'In-Person', 'okay sabi mo eh', '2025-05-18 16:49:21', NULL),
(47, 8, 'Dr. Pretz Elle', '2025-05-23', '9:00 AM', 'In-Person', 'gogo', '2025-05-22 07:19:44', NULL),
(51, 16, 'Dr.Kris', '2025-05-30', '3:00 PM', 'Online', 'wala lang ngani', '2025-05-23 21:30:01', 'Dr.Kris'),
(59, 16, 'Dr.Elle', '2025-05-31', '10:30 AM', 'In-Person', 'try 2', '2025-05-23 22:37:37', 'Dr.Elle'),
(60, 16, 'Dr.Carl', '2025-05-29', '1:00 PM', 'Online', 'try5', '2025-05-27 23:33:53', 'Dr.Carl'),
(61, 16, 'Dr.Kris', '2025-05-28', '3:00 PM', 'Online', 'this day', '2025-05-28 00:46:47', 'Dr.Kris'),
(62, 16, 'Dr.pritz', '2025-05-30', '9:00 AM', 'In-Person', 'with user name', '2025-05-28 01:35:28', 'CampusCare');

-- --------------------------------------------------------

--
-- Table structure for table `appointment_limits`
--

CREATE TABLE `appointment_limits` (
  `date` date NOT NULL,
  `time` varchar(20) NOT NULL,
  `limit_count` int(11) NOT NULL DEFAULT 5
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appointment_limits`
--

INSERT INTO `appointment_limits` (`date`, `time`, `limit_count`) VALUES
('2025-05-29', '10:30 AM', 0),
('2025-05-29', '1:00 PM', 5),
('2025-05-29', '3:00 PM', 5),
('2025-05-29', '9:00 AM', 5);

-- --------------------------------------------------------

--
-- Table structure for table `doctor`
--

CREATE TABLE `doctor` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `time` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `doctor`
--

INSERT INTO `doctor` (`id`, `name`, `time`) VALUES
(1, 'Dr.pritz', '9:00 AM'),
(2, 'Dr.Elle', '10:30 AM'),
(3, 'Dr.Carl', '1:00 PM'),
(4, 'Dr.Kris', '3:00 PM');

-- --------------------------------------------------------

--
-- Table structure for table `health_info`
--

CREATE TABLE `health_info` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `blood_group` varchar(10) DEFAULT NULL,
  `height` varchar(10) DEFAULT NULL,
  `weight` varchar(10) DEFAULT NULL,
  `allergies` text DEFAULT NULL,
  `conditions` text DEFAULT NULL,
  `medications` text DEFAULT NULL,
  `last_checkup` date DEFAULT NULL,
  `emergency_contact` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `health_info`
--

INSERT INTO `health_info` (`id`, `user_id`, `blood_group`, `height`, `weight`, `allergies`, `conditions`, `medications`, `last_checkup`, `emergency_contact`) VALUES
(1, 16, 'AB+', '167', '50kg', 'Peanut', 'Mild', 'Water', '0000-00-00', '+639917452192');

-- --------------------------------------------------------

--
-- Table structure for table `holidays`
--

CREATE TABLE `holidays` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `reason` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `holidays`
--

INSERT INTO `holidays` (`id`, `date`, `reason`) VALUES
(4, '2025-05-31', 'Wala appointment');

-- --------------------------------------------------------

--
-- Table structure for table `medical_history`
--

CREATE TABLE `medical_history` (
  `user_id` varchar(50) NOT NULL,
  `date` datetime NOT NULL,
  `name` varchar(200) NOT NULL,
  `past_diagnoses` text DEFAULT NULL,
  `previous_surgeries` text DEFAULT NULL,
  `chronic_conditions` text DEFAULT NULL,
  `hospitalizations` text DEFAULT NULL,
  `allergies` text DEFAULT NULL,
  `medications_history` text DEFAULT NULL,
  `vaccination_records` text DEFAULT NULL,
  `family_medical_history` text DEFAULT NULL,
  `lifestyle_factors` text DEFAULT NULL,
  `doctor_notes` text DEFAULT NULL,
  `lab_test_results` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medical_history`
--

INSERT INTO `medical_history` (`user_id`, `date`, `name`, `past_diagnoses`, `previous_surgeries`, `chronic_conditions`, `hospitalizations`, `allergies`, `medications_history`, `vaccination_records`, `family_medical_history`, `lifestyle_factors`, `doctor_notes`, `lab_test_results`) VALUES
('16', '2025-05-24 06:14:26', 'Ellemar Pundavela', 'Headache', 'N/A', 'Ubo', 'Makati Med', 'Hatdog', 'Biogesic\nMeth', 'Yesterday', 'N/A', 'Running', 'Medjo delulu', 'Positive Delulu'),
('16', '2025-05-24 06:51:22', 'Hshshs', 'syaux xaun', 'janNz', 'nanz z', 'nabNzb', '', '', '', '', 'ahbab', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `otp_codes`
--

CREATE TABLE `otp_codes` (
  `id` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `otp` varchar(10) NOT NULL,
  `expiry_time` datetime NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `otp_codes`
--

INSERT INTO `otp_codes` (`id`, `email`, `otp`, `expiry_time`, `created_at`) VALUES
(144, 't39281030@gmail.com', '441593', '2025-05-28 09:01:22', '2025-05-27 23:16:40');

-- --------------------------------------------------------

--
-- Table structure for table `signup`
--

CREATE TABLE `signup` (
  `no.` int(200) NOT NULL,
  `role` varchar(200) NOT NULL,
  `name` varchar(200) NOT NULL,
  `gender` varchar(250) NOT NULL,
  `age` int(250) NOT NULL,
  `contact` int(100) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  `confirmPassword` varchar(200) NOT NULL,
  `Date` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `signup`
--

INSERT INTO `signup` (`no.`, `role`, `name`, `gender`, `age`, `contact`, `email`, `password`, `confirmPassword`, `Date`) VALUES
(2, '', 'ELLEMAR PUNDAVELA', 'Male', 18, 2147483647, 'epundavela.a12344954@umak.edu.ph', '', '', '2025-04-26 22:19:18'),
(3, '', 'ELLEMAR PUNDAVELA', 'Male', 18, 2147483647, 't39281030@gmail.com', 'Ellemar12345', '', '2025-04-26 22:52:26'),
(5, '', 'EllemarPundavela', 'Male', 18, 2147483647, 'ellemar.pundavela@depedmakati.ph', 'ellemar451', 'ellemar451', '2025-05-16 20:03:31'),
(6, '', 'EllemarPundavela', 'Male', 18, 2147483647, 'ellemar@gmail.com', 'ellemar451', 'ellemar451', '2025-05-16 20:11:30'),
(7, '', 'Elle', 'male', 18, 2147483647, 'elle@gmail.com', 'Elle451', 'Elle451', '2025-05-16 21:08:46'),
(8, '', 'DQ', 'Male', 20, 2147483647, 'janpauldequiroz123@gmail.com', 'qwerty', 'qwerty', '2025-05-17 14:59:23'),
(9, '', 'Vanessa', 'female', 21, 2147483647, 'vanessaofrancia@gmail.com', '123456', '123456', '2025-05-17 17:26:38'),
(10, '', 'Kim', 'Male', 20, 2147483647, 'efrenkimdanieltomotorgo@gmal.com', 'www.kim.com', 'www.kim.com', '2025-05-19 14:42:48'),
(11, '', 'harvey', 'non', 13, 2147483647, 'harvey@gmail.com', 'qwerty', 'qwerty', '2025-05-19 19:15:51'),
(12, '', 'Elle', 'male', 180, 2147483647, 'Ellemar123@gmail.com', 'Elle1234', 'Elle1234', '2025-05-19 19:48:35'),
(17, '', 'CampusCare', 'male', 19, 2147483647, 'campuscareapp@gmail.com', '$2y$10$9.bRUeBIKuiDBLGOBbAz6umLVvPzvCXxN3K3dg0zSeoXM/QoxYnOu', '$2y$10$9.bRUeBIKuiDBLGOBbAz6umLVvPzvCXxN3K3dg0zSeoXM/QoxYnOu', '2025-05-22 18:35:02'),
(20, 'Student', 'CampusCare', 'Male', 19, 2147483647, 't39281030@gmail.com', '$2y$10$GFHwXyRArUPagUiKWFdi7ee0MVfSxiT6O8Q2VswcVUaZs81oMzLFW', '', '2025-05-28 07:14:59'),
(21, 'Student', 'ELLEMAR PUNDAVELA', 'Male', 19, 2147483647, 'ellemarpundavela69@gmail.com', '$2y$10$xoMrgEW8e88CJ9.YAj6gvOqHA.x8kBVgLfk1rigwzUmcCrfh32gGy', '', '2025-05-28 15:48:21'),
(22, 'Student', 'ELLEMAR PUNDAVELA', 'Male', 19, 2147483647, 'ellemarpundavela12@gmail.com', '$2y$10$qhxJc9rzwqV.DguREwrma.INSwKRShUiMnZUfbKRuQgRbbzeQNCPO', '', '2025-05-28 15:49:31'),
(23, 'Student', 'ELLEMAR PUNDAVELA', 'Male', 19, 2147483647, 'ellemarpundavela12@gmail.com', '$2y$10$/0G2W.H5QYwJ8YmShg0bmevMOXmm0EVy485iY0Xp.1Sa95m/QT0VO', '', '2025-05-28 15:52:13'),
(25, 'Student', 'EllemarPundavela', 'Male', 19, 2147483647, 'ellemarpundavela69@gmail.com', '$2y$10$Q/nC83lN5316mOiXP4mUMuiAlOC1ngPksMNaIGRsqv7CUS9w/xmQq', '', '2025-05-28 16:17:26'),
(26, 'Student', 'ELLEMAR PUNDAVELA', 'Male', 18, 2147483647, 'ellemarpundavela69@gmail.com', '$2y$10$0KlnBFlLhg0Y1tLtkfWmYuSHzesJheGLV7ghPrv48rEz6l5s4WsUO', '', '2025-05-28 16:34:10'),
(27, 'Teacher', 'ELLEMAR PUNDAVELA', 'Male', 25, 2147483647, 'ellemarpundavela12@gmail.com', '$2y$10$JGDL11bYP.B4P9Fx079xuOPRJO1afFhZpSf/mZ.AmCJvHChV1iTry', '', '2025-05-28 16:38:01'),
(28, 'Student', 'ELLEMAR PUNDAVELA', 'Male', 25, 2147483647, 'ellemarpundavela12@gmail.com', '$2y$10$.KYsM0bXtijoceAF1mtRjOVGBoIpDhMKaT6lfe5rey7dqfHIjLUry', '', '2025-05-28 16:47:35');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(200) NOT NULL,
  `role` varchar(200) NOT NULL,
  `name` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `Password` varchar(250) NOT NULL,
  `Date` date NOT NULL,
  `verified` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `role`, `name`, `email`, `Password`, `Date`, `verified`) VALUES
(1, '', '', 'admin@gmail.com', '12345678', '2025-04-26', 0),
(3, '', '', 'epundavela.a12344954@umak.edu.ph', 'Ellemar12345', '2025-04-26', 0),
(6, '', 'ellemar451', 'ellemar.pundavela@depedmakati.ph', 'EllemarPundavela', '2025-05-16', 0),
(7, '', 'EllemarPundavela', 'ellemar@gmail.com', 'ellemar451', '2025-05-16', 0),
(8, '', 'Elle', 'elle@gmail.com', 'Elle451', '2025-05-16', 0),
(9, '', 'DQ', 'janpauldequiroz123@gmail.com', 'qwerty', '2025-05-17', 0),
(10, '', 'Vanessa', 'vanessaofrancia@gmail.com', '123456', '2025-05-17', 0),
(11, '', 'Kim', 'efrenkimdanieltomotorgo@gmal.com', 'www.kim.com', '2025-05-19', 0),
(12, '', 'harvey', 'harvey@gmail.com', 'qwerty', '2025-05-19', 0),
(13, '', 'Elle', 'Ellemar123@gmail.com', 'Elle1234', '2025-05-19', 0),
(16, 'AdminUser', 'CampusCare', 'campuscareapp@gmail.com', '$2y$10$9.bRUeBIKuiDBLGOBbAz6umLVvPzvCXxN3K3dg0zSeoXM/QoxYnOu', '2025-05-22', 1),
(20, 'Admin', 'CampusCare', 't39281030@gmail.com', '$2y$10$GFHwXyRArUPagUiKWFdi7ee0MVfSxiT6O8Q2VswcVUaZs81oMzLFW', '2025-05-28', 1),
(26, 'Student', 'ELLEMAR PUNDAVELA', 'ellemarpundavela69@gmail.com', '$2y$10$0KlnBFlLhg0Y1tLtkfWmYuSHzesJheGLV7ghPrv48rEz6l5s4WsUO', '2025-05-28', 1),
(28, 'Student', 'ELLEMAR PUNDAVELA', 'ellemarpundavela12@gmail.com', '$2y$10$.KYsM0bXtijoceAF1mtRjOVGBoIpDhMKaT6lfe5rey7dqfHIjLUry', '2025-05-28', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activity_log`
--
ALTER TABLE `activity_log`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `appointments`
--
ALTER TABLE `appointments`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `appointment_limits`
--
ALTER TABLE `appointment_limits`
  ADD PRIMARY KEY (`date`,`time`);

--
-- Indexes for table `doctor`
--
ALTER TABLE `doctor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `health_info`
--
ALTER TABLE `health_info`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `holidays`
--
ALTER TABLE `holidays`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `medical_history`
--
ALTER TABLE `medical_history`
  ADD PRIMARY KEY (`user_id`,`date`);

--
-- Indexes for table `otp_codes`
--
ALTER TABLE `otp_codes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `signup`
--
ALTER TABLE `signup`
  ADD PRIMARY KEY (`no.`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activity_log`
--
ALTER TABLE `activity_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `appointments`
--
ALTER TABLE `appointments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;

--
-- AUTO_INCREMENT for table `doctor`
--
ALTER TABLE `doctor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `health_info`
--
ALTER TABLE `health_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `holidays`
--
ALTER TABLE `holidays`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `otp_codes`
--
ALTER TABLE `otp_codes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=183;

--
-- AUTO_INCREMENT for table `signup`
--
ALTER TABLE `signup`
  MODIFY `no.` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `health_info`
--
ALTER TABLE `health_info`
  ADD CONSTRAINT `health_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
