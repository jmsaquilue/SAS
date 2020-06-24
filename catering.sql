-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-06-2020 a las 09:00:34
-- Versión del servidor: 5.7.30-0ubuntu0.18.04.1
-- Versión de PHP: 7.3.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `catering`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `CookShifts`
--

CREATE TABLE IF NOT EXISTS `CookShifts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cook_id` int(11) NOT NULL,
  `shift_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `CookShifts_Shifts_id_fk` (`shift_id`),
  KEY `CookShifts_Users_id_fk` (`cook_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `CookShifts`
--

INSERT INTO `CookShifts` (`id`, `cook_id`, `shift_id`) VALUES
(1, 4, 1),
(2, 4, 2),
(3, 5, 1),
(4, 5, 4),
(5, 6, 3),
(6, 6, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Events`
--

CREATE TABLE IF NOT EXISTS `Events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `date` date DEFAULT NULL,
  `organized` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `Event_Users_id_fk` (`organized`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Events`
--

INSERT INTO `Events` (`id`, `name`, `date`, `organized`) VALUES
(1, 'Manana no hay clase', '2020-06-10', 1),
(3, 'Fin de examenes', '2020-07-03', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Recipes`
--

CREATE TABLE IF NOT EXISTS `Recipes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Recipes`
--

INSERT INTO `Recipes` (`id`, `name`, `description`) VALUES
(1, 'cena', 'mi polla rellena'),
(2, 'polenta', NULL),
(3, 'huevos', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Roles`
--

CREATE TABLE IF NOT EXISTS `Roles` (
  `id` char(1) NOT NULL,
  `role` varchar(128) DEFAULT 'servizio',
  PRIMARY KEY (`id`),
  UNIQUE KEY `Roles_id_uindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Roles`
--

INSERT INTO `Roles` (`id`, `role`) VALUES
('c', 'cuoco'),
('h', 'chef'),
('o', 'organizzatore'),
('s', 'servizio');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Shifts`
--

CREATE TABLE IF NOT EXISTS `Shifts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start` int(11) NOT NULL,
  `end` int(11) NOT NULL,
  `Day` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Shifts`
--

INSERT INTO `Shifts` (`id`, `start`, `end`, `Day`) VALUES
(1, 9, 11, '2020-08-15'),
(2, 11, 13, '2020-08-15'),
(3, 13, 15, '2020-08-15'),
(4, 15, 17, '2020-08-15'),
(5, 12, 14, '2020-08-15');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SummarySheets`
--

CREATE TABLE IF NOT EXISTS `SummarySheets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `creator` int(11) DEFAULT NULL,
  `event` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `SummarySheet_Events_id_fk` (`event`),
  KEY `SummarySheet_Users_id_fk` (`creator`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `SummarySheets`
--

INSERT INTO `SummarySheets` (`id`, `creator`, `event`) VALUES
(8, 2, 1),
(9, 2, 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `TaskCookShifts`
--

CREATE TABLE IF NOT EXISTS `TaskCookShifts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL,
  `cook_id` int(11) NOT NULL,
  `shift_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `TaskCookShifts_Tasks_id_fk` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `TaskCookShifts`
--

INSERT INTO `TaskCookShifts` (`id`, `task_id`, `cook_id`, `shift_id`) VALUES
(2, 2, 4, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Tasks`
--

CREATE TABLE IF NOT EXISTS `Tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timeEstimate` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT '1',
  `complete` tinyint(1) NOT NULL DEFAULT '0',
  `recipeid` int(11) DEFAULT NULL,
  `summaryid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `Task_Recipe_id_fk` (`recipeid`),
  KEY `Task_SummarySheets_id_fk` (`summaryid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Tasks`
--

INSERT INTO `Tasks` (`id`, `timeEstimate`, `quantity`, `complete`, `recipeid`, `summaryid`) VALUES
(2, 0, 1, 0, 1, 8),
(3, 0, 1, 0, 2, 8);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `UserRoles`
--

CREATE TABLE IF NOT EXISTS `UserRoles` (
  `user_id` int(11) NOT NULL,
  `role_id` char(1) NOT NULL,
  KEY `UserRoles_Roles_id_fk` (`role_id`),
  KEY `UserRoles_Users_id_fk` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `UserRoles`
--

INSERT INTO `UserRoles` (`user_id`, `role_id`) VALUES
(1, 'o'),
(2, 'h'),
(3, 'h'),
(4, 'h'),
(4, 'c'),
(5, 'c'),
(6, 'c'),
(7, 'c'),
(7, 's'),
(8, 's'),
(9, 's'),
(10, 's');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Users`
--

CREATE TABLE IF NOT EXISTS `Users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL DEFAULT '',
  `pass` varchar(128) NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  `surname` varchar(120) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Users`
--

INSERT INTO `Users` (`id`, `username`, `pass`, `name`, `surname`, `gender`, `age`, `email`) VALUES
(1, 'Carlin', 'Carlin', NULL, NULL, NULL, NULL, NULL),
(2, 'Lidia', 'Lidia', NULL, NULL, NULL, NULL, NULL),
(3, 'Tony', 'Tony', NULL, NULL, NULL, NULL, NULL),
(4, 'Marinella', 'Marinella', NULL, NULL, NULL, NULL, NULL),
(5, 'Guido', 'Guido', NULL, NULL, NULL, NULL, NULL),
(6, 'Antonietta', 'Antonietta', NULL, NULL, NULL, NULL, NULL),
(7, 'Paola', 'Paola', NULL, NULL, NULL, NULL, NULL),
(8, 'Silvia', 'Silvia', NULL, NULL, NULL, NULL, NULL),
(9, 'Marco', 'Marco', NULL, NULL, NULL, NULL, NULL),
(10, 'Piergiorgio', 'Piergiorgio', NULL, NULL, NULL, NULL, NULL);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `CookShifts`
--
ALTER TABLE `CookShifts`
  ADD CONSTRAINT `CookShifts_Shifts_id_fk` FOREIGN KEY (`shift_id`) REFERENCES `Shifts` (`id`),
  ADD CONSTRAINT `CookShifts_Users_id_fk` FOREIGN KEY (`cook_id`) REFERENCES `Users` (`id`);

--
-- Filtros para la tabla `Events`
--
ALTER TABLE `Events`
  ADD CONSTRAINT `Event_Users_id_fk` FOREIGN KEY (`organized`) REFERENCES `Users` (`id`);

--
-- Filtros para la tabla `SummarySheets`
--
ALTER TABLE `SummarySheets`
  ADD CONSTRAINT `SummarySheet_Events_id_fk` FOREIGN KEY (`event`) REFERENCES `Events` (`id`),
  ADD CONSTRAINT `SummarySheet_Users_id_fk` FOREIGN KEY (`creator`) REFERENCES `Users` (`id`);

--
-- Filtros para la tabla `TaskCookShifts`
--
ALTER TABLE `TaskCookShifts`
  ADD CONSTRAINT `TaskCookShifts_Tasks_id_fk` FOREIGN KEY (`task_id`) REFERENCES `Tasks` (`id`);

--
-- Filtros para la tabla `Tasks`
--
ALTER TABLE `Tasks`
  ADD CONSTRAINT `Task_Recipe_id_fk` FOREIGN KEY (`recipeid`) REFERENCES `Recipes` (`id`),
  ADD CONSTRAINT `Task_SummarySheets_id_fk` FOREIGN KEY (`summaryid`) REFERENCES `SummarySheets` (`id`);

--
-- Filtros para la tabla `UserRoles`
--
ALTER TABLE `UserRoles`
  ADD CONSTRAINT `UserRoles_Roles_id_fk` FOREIGN KEY (`role_id`) REFERENCES `Roles` (`id`),
  ADD CONSTRAINT `UserRoles_Users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
