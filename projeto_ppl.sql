-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jul 10, 2014 at 08:02 PM
-- Server version: 5.6.16
-- PHP Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `projeto_ppl`
--

-- --------------------------------------------------------

--
-- Table structure for table `anexos`
--

CREATE TABLE IF NOT EXISTS `anexos` (
  `emprego_id` int(11) NOT NULL,
  `anexo_path` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ofertas_emprego`
--

CREATE TABLE IF NOT EXISTS `ofertas_emprego` (
  `emprego_id` int(11) NOT NULL AUTO_INCREMENT,
  `emprego_titulo` varchar(50) NOT NULL,
  `emprego_detalhes` varchar(500) NOT NULL,
  `emprego_candidatos_necessarios` int(11) NOT NULL,
  `emprego_perfil` varchar(100) NOT NULL,
  `emprego_area_atuacao` varchar(50) NOT NULL,
  `emprego_estado` varchar(50) NOT NULL,
  PRIMARY KEY (`emprego_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=19 ;

-- --------------------------------------------------------

--
-- Table structure for table `ofertas_recursos`
--

CREATE TABLE IF NOT EXISTS `ofertas_recursos` (
  `recursos_id` int(11) NOT NULL AUTO_INCREMENT,
  `recursos_nome` varchar(50) NOT NULL,
  `recursos_contacto` varchar(50) NOT NULL,
  `recursos_area_atuacao` varchar(50) NOT NULL,
  `recursos_estado_oferta` varchar(50) NOT NULL,
  `emprego_id` int(11) DEFAULT NULL,
  `is_new` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`recursos_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25 ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `user_password` varchar(50) NOT NULL,
  `user_privilege` tinyint(1) NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=17 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `user_name`, `user_password`, `user_privilege`) VALUES
(1, 'Leandro Mendes', 'd9da8170e8bc9f27b2d32a6c9a6c697d', 1),
(2, 'Daniel Rodrigues', '81dc9bdb52d04dc2036dbd8313ed055', 1),
(8, 'User Geral', '202cb962ac5975b964b7152d234b70', 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
