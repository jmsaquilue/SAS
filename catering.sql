-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 08-07-2020 a las 18:58:20
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

CREATE TABLE `CookShifts` (
  `id` int(11) NOT NULL,
  `cook_id` int(11) NOT NULL,
  `shift_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `CookShifts`
--

INSERT INTO `CookShifts` (`id`, `cook_id`, `shift_id`) VALUES
(1, 4, 1),
(2, 4, 2),
(3, 4, 3),
(4, 5, 3),
(5, 5, 4),
(6, 5, 5),
(7, 6, 4),
(8, 6, 5),
(9, 6, 6),
(10, 7, 5),
(11, 7, 6),
(12, 7, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Events`
--

CREATE TABLE `Events` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `date` date DEFAULT NULL,
  `organized` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Events`
--

INSERT INTO `Events` (`id`, `name`, `date`, `organized`) VALUES
(4, 'fete nationale francaise', '2020-07-14', 1),
(5, 'festa di compleanno', '2020-07-20', 1),
(6, 'SantAnna e San Gioacchino', '2020-07-26', 1),
(7, 'San Fermín', '2020-07-07', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Recipes`
--

CREATE TABLE `Recipes` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Recipes`
--

INSERT INTO `Recipes` (`id`, `name`, `description`) VALUES
(1, 'Polpo alla griglia', 'piatto di mare perfetto per secondi o antipasti, a seconda della porzione.'),
(2, 'Focaccine di patate ripiene.', 'sono un\'idea simpatica per un antipasto goloso e saporito. Il procedimento è abbastanza semplice e, tolta la cottura delle patate, neanche troppo lungo.'),
(3, 'Amor di polenta', 'un dolce antico, tipico della tradizione lombarda, in particolare della città di Varese, ragion per cui è conosciuto anche come il dolce di Varese.'),
(4, 'Cotoletta', 'Cotoletta (a rigore) di vitello impanata e fritta nel burro.');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Roles`
--

CREATE TABLE `Roles` (
  `id` char(1) NOT NULL,
  `role` varchar(128) DEFAULT 'servizio'
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

CREATE TABLE `Shifts` (
  `id` int(11) NOT NULL,
  `start` int(11) NOT NULL,
  `end` int(11) NOT NULL,
  `Day` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `Shifts`
--

INSERT INTO `Shifts` (`id`, `start`, `end`, `Day`) VALUES
(1, 7, 9, '2020-07-14'),
(2, 9, 11, '2020-07-14'),
(3, 11, 13, '2020-07-14'),
(4, 7, 9, '2020-08-14'),
(5, 9, 11, '2020-09-13'),
(6, 11, 13, '2020-10-12'),
(7, 15, 18, '2020-08-15');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SummarySheets`
--

CREATE TABLE `SummarySheets` (
  `id` int(11) NOT NULL,
  `creator` int(11) DEFAULT NULL,
  `event` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `SummarySheets`
--

INSERT INTO `SummarySheets` (`id`, `creator`, `event`) VALUES
(10, 2, 4),
(11, 2, 5),
(12, 2, 7);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `TaskCookShifts`
--

CREATE TABLE `TaskCookShifts` (
  `id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL,
  `cook_id` int(11) NOT NULL,
  `shift_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `Tasks`
--

CREATE TABLE `Tasks` (
  `id` int(11) NOT NULL,
  `timeEstimate` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT '1',
  `complete` tinyint(1) NOT NULL DEFAULT '0',
  `recipeid` int(11) DEFAULT NULL,
  `summaryid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `UserRoles`
--

CREATE TABLE `UserRoles` (
  `user_id` int(11) NOT NULL,
  `role_id` char(1) NOT NULL
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

CREATE TABLE `Users` (
  `id` int(11) NOT NULL,
  `username` varchar(128) NOT NULL DEFAULT '',
  `pass` varchar(128) NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  `surname` varchar(120) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL
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
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `CookShifts`
--
ALTER TABLE `CookShifts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `CookShifts_Shifts_id_fk` (`shift_id`),
  ADD KEY `CookShifts_Users_id_fk` (`cook_id`);

--
-- Indices de la tabla `Events`
--
ALTER TABLE `Events`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Event_Users_id_fk` (`organized`);

--
-- Indices de la tabla `Recipes`
--
ALTER TABLE `Recipes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `Roles`
--
ALTER TABLE `Roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `Roles_id_uindex` (`id`);

--
-- Indices de la tabla `Shifts`
--
ALTER TABLE `Shifts`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `SummarySheets`
--
ALTER TABLE `SummarySheets`
  ADD PRIMARY KEY (`id`),
  ADD KEY `SummarySheet_Events_id_fk` (`event`),
  ADD KEY `SummarySheet_Users_id_fk` (`creator`);

--
-- Indices de la tabla `TaskCookShifts`
--
ALTER TABLE `TaskCookShifts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `TaskCookShifts_Tasks_id_fk` (`task_id`);

--
-- Indices de la tabla `Tasks`
--
ALTER TABLE `Tasks`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Task_Recipe_id_fk` (`recipeid`),
  ADD KEY `Task_SummarySheets_id_fk` (`summaryid`);

--
-- Indices de la tabla `UserRoles`
--
ALTER TABLE `UserRoles`
  ADD KEY `UserRoles_Roles_id_fk` (`role_id`),
  ADD KEY `UserRoles_Users_id_fk` (`user_id`);

--
-- Indices de la tabla `Users`
--
ALTER TABLE `Users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `CookShifts`
--
ALTER TABLE `CookShifts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `Events`
--
ALTER TABLE `Events`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `Recipes`
--
ALTER TABLE `Recipes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `Shifts`
--
ALTER TABLE `Shifts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `SummarySheets`
--
ALTER TABLE `SummarySheets`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `TaskCookShifts`
--
ALTER TABLE `TaskCookShifts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT de la tabla `Tasks`
--
ALTER TABLE `Tasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT de la tabla `Users`
--
ALTER TABLE `Users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

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
