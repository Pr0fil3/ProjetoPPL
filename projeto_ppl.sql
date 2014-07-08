-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jul 08, 2014 at 11:09 PM
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

--
-- Dumping data for table `anexos`
--

INSERT INTO `anexos` (`emprego_id`, `anexo_path`) VALUES
(1, 'C:\\Users\\-nikeiZprooo-\\Desktop\\ProjetoPPL\\AnexosProjetoPP\\1\\323732539\\MinecraftLog.txt'),
(1, 'C:\\Users\\-nikeiZprooo-\\Desktop\\ProjetoPPL\\AnexosProjetoPP\\1\\19540433\\ACRevelations.ini'),
(2, 'C:\\Users\\-nikeiZprooo-\\Desktop\\ProjetoPPL\\AnexosProjetoPP\\2\\2024186570\\meta.xml'),
(3, 'C:\\Users\\-nikeiZprooo-\\Desktop\\ProjetoPPL\\AnexosProjetoPP\\3\\1686912081\\Trabalho 1º.pdf');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `ofertas_emprego`
--

INSERT INTO `ofertas_emprego` (`emprego_id`, `emprego_titulo`, `emprego_detalhes`, `emprego_candidatos_necessarios`, `emprego_perfil`, `emprego_area_atuacao`, `emprego_estado`) VALUES
(1, 'Programador Junior JAVA', 'Procura-se programador recém-licenciado de JAVA', 1, 'Licenciado e com vontade de enfrentar desafios!', 'DESENVOLVIMENTO', 'APROVADO'),
(2, 'Procuram-se administradores de redes experientes.', 'Procuram-se administradores de redes para trabalhar em Lisboa.', 2, 'Experiencia de 10 anos.', 'REDE', 'NAO_APROVADO'),
(3, 'Procuras-se Programador C#', 'Procuras-se Programador C#', 3, 'Experiencia 3 anos', 'DESENVOLVIMENTO', 'NAO_APROVADO');

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=14 ;

--
-- Dumping data for table `ofertas_recursos`
--

INSERT INTO `ofertas_recursos` (`recursos_id`, `recursos_nome`, `recursos_contacto`, `recursos_area_atuacao`, `recursos_estado_oferta`, `emprego_id`, `is_new`) VALUES
(4, 'António Manuel', '+351 915684628', 'DESENVOLVIMENTO', 'APROVADO', 1, 0),
(5, 'José Miguel', '963215489', 'DESENVOLVIMENTO', 'APROVADO', NULL, 1),
(6, 'João Pereira', '926548765', 'BASES_DADOS', 'APROVADO', NULL, 1),
(7, 'André Vitorino', '936548932', 'REDE', 'APROVADO', NULL, 1),
(8, 'Fábio Martins', '966548979', 'BASES_DADOS', 'APROVADO', NULL, 1),
(9, 'Igor Monte', '966548721', 'BASES_DADOS', 'APROVADO', NULL, 1),
(10, 'Anabela Andrade', '926547856', 'DESENVOLVIMENTO', 'APROVADO', NULL, 1),
(11, 'Miguel Sovinha', '963214568', 'REDE', 'APROVADO', NULL, 1),
(12, 'Daniel Montenegro', '916589137', 'REDE', 'APROVADO', NULL, 1),
(13, 'Leandro Mendes', '916465897', 'DESENVOLVIMENTO', 'APROVADO', NULL, 1);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `user_name`, `user_password`, `user_privilege`) VALUES
(1, 'Leandro Mendes', 'd9da8170e8bc9f27b2d32a6c9a6c697d', 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
