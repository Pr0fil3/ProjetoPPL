-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jul 04, 2014 at 08:30 PM
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
  `anexo_path` varchar(100) NOT NULL
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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `ofertas_emprego`
--

INSERT INTO `ofertas_emprego` (`emprego_id`, `emprego_titulo`, `emprego_detalhes`, `emprego_candidatos_necessarios`, `emprego_perfil`, `emprego_area_atuacao`, `emprego_estado`) VALUES
(2, '123', '123', 1, '123', 'REDE', 'POR_APROVAR');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `ofertas_recursos`
--

INSERT INTO `ofertas_recursos` (`recursos_id`, `recursos_nome`, `recursos_contacto`, `recursos_area_atuacao`, `recursos_estado_oferta`, `emprego_id`, `is_new`) VALUES
(1, 'Leandro', '123', 'REDE', 'POR_APROVAR', NULL, 0),
(2, 'João', '123', 'REDE', 'POR_APROVAR', NULL, 0),
(3, 'Miguel', '132', 'REDE', 'POR_APROVAR', NULL, 0);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
