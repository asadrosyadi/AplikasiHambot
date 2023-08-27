-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 26, 2023 at 03:44 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hambot`
--

-- --------------------------------------------------------

--
-- Table structure for table `datasensor`
--

CREATE TABLE `datasensor` (
  `id` int(11) NOT NULL,
  `HWID` varchar(256) NOT NULL,
  `email` text NOT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `baterai` float NOT NULL,
  `tangki_pestisida` float NOT NULL,
  `pergerakan_robot` text NOT NULL,
  `deteksi_hama` text NOT NULL,
  `jarak_hama` float NOT NULL,
  `penyemprotan_hama` text NOT NULL,
  `mode_penyemprotan` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `datasensor`
--

INSERT INTO `datasensor` (`id`, `HWID`, `email`, `time`, `baterai`, `tangki_pestisida`, `pergerakan_robot`, `deteksi_hama`, `jarak_hama`, `penyemprotan_hama`, `mode_penyemprotan`) VALUES
(3, 'VP221201D', 'admin@admin.com', '2023-08-26 13:25:32', 70, 20, 'tidak', 'aman', 0, 'mati', 'otomatis'),
(6, 'asad', 'rosyadi.asad@gmail.com ', '2023-08-26 08:57:04', 0, 0, 'tidak', 'aman', 0, 'mati', 'otomatis'),
(7, 'VP221201D', 'admin@admin.com', '2023-08-26 13:43:52', 80, 18, 'diam', 'ada', 11, 'mati', 'otomatis');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `nama` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL,
  `token` text NOT NULL,
  `HWID` text NOT NULL,
  `waktu_penyemprotan` time NOT NULL,
  `mode_penyemprotan` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `nama`, `email`, `password`, `token`, `HWID`, `waktu_penyemprotan`, `mode_penyemprotan`) VALUES
(1, 'admin', 'admin@admin.com', '$2y$10$5dItoBBjp0xz5u58xlOKQelEiUZuu.KLFGQF0Ur1SPHlwMod1y1O6', 'zwJdmaOCQoDW9XgH', 'VP221201D', '18:10:20', 'otomatis'),
(8, 'Mohammad As\'ad Rosyadi', 'rosyadi.asad@gmail.com ', '$2y$10$MJlMm1lGcw/OSItM.wiVl.G4cQhyEtjL57M23DzACFATXAyZvZ/6C', 'a89b5cc12914851a07f763c9210c72a1', 'asad', '00:00:00', 'manual');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `datasensor`
--
ALTER TABLE `datasensor`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `datasensor`
--
ALTER TABLE `datasensor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
