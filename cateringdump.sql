-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Lug 12, 2023 alle 13:05
-- Versione del server: 10.4.28-MariaDB
-- Versione PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `catering`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `events`
--

CREATE TABLE `events` (
  `id` int(11) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `date_start` date DEFAULT NULL,
  `date_end` date DEFAULT NULL,
  `expected_participants` int(11) DEFAULT NULL,
  `organizer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `events`
--

INSERT INTO `events` (`id`, `name`, `date_start`, `date_end`, `expected_participants`, `organizer_id`) VALUES
(1, 'Convegno Agile Community', '2020-09-25', '2020-09-25', 100, 2),
(2, 'Compleanno di Manuela', '2020-08-13', '2020-08-13', 25, 2),
(3, 'Fiera del Sedano Rapa', '2020-10-02', '2020-10-04', 400, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `kitchentasks`
--

CREATE TABLE `kitchentasks` (
  `id` int(11) NOT NULL,
  `summarysheet_id` int(11) NOT NULL DEFAULT 0,
  `subduty_id` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `menufeatures`
--

CREATE TABLE `menufeatures` (
  `menu_id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL DEFAULT '',
  `value` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `menufeatures`
--

INSERT INTO `menufeatures` (`menu_id`, `name`, `value`) VALUES
(80, 'Richiede cuoco', 0),
(80, 'Buffet', 0),
(80, 'Richiede cucina', 0),
(80, 'Finger food', 0),
(80, 'Piatti caldi', 0),
(82, 'Richiede cuoco', 0),
(82, 'Buffet', 0),
(82, 'Richiede cucina', 0),
(82, 'Finger food', 0),
(82, 'Piatti caldi', 0),
(86, 'Richiede cuoco', 0),
(86, 'Buffet', 0),
(86, 'Richiede cucina', 0),
(86, 'Finger food', 0),
(86, 'Piatti caldi', 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `menuitems`
--

CREATE TABLE `menuitems` (
  `id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `section_id` int(11) DEFAULT NULL,
  `description` tinytext DEFAULT NULL,
  `recipe_id` int(11) NOT NULL,
  `position` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `menuitems`
--

INSERT INTO `menuitems` (`id`, `menu_id`, `section_id`, `description`, `recipe_id`, `position`) VALUES
(121, 80, 0, 'Croissant', 11, 0),
(122, 80, 0, 'Biscotti', 12, 1),
(123, 82, 0, 'Croissant', 11, 0),
(124, 86, 41, 'Vitello tonnato', 13, 0),
(125, 86, 42, 'Risotto ai funghi', 3, 0),
(126, 86, 42, 'Gnocchi al pomodoro', 10, 1),
(127, 86, 42, 'Lasagne al forno', 1, 2),
(128, 86, 43, 'Salmone alla griglia', 7, 0),
(129, 86, 43, 'Pollo al curry', 9, 1),
(130, 86, 44, 'Tiramisù', 4, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `menus`
--

CREATE TABLE `menus` (
  `id` int(11) NOT NULL,
  `title` tinytext DEFAULT NULL,
  `owner_id` int(11) DEFAULT NULL,
  `published` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `menus`
--

INSERT INTO `menus` (`id`, `title`, `owner_id`, `published`) VALUES
(80, 'Coffee break mattutino', 2, 1),
(82, 'Coffee break pomeridiano', 2, 1),
(86, 'Cena di compleanno pesce', 3, 1);

-- --------------------------------------------------------

--
-- Struttura della tabella `menusections`
--

CREATE TABLE `menusections` (
  `id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `name` tinytext DEFAULT NULL,
  `position` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `menusections`
--

INSERT INTO `menusections` (`id`, `menu_id`, `name`, `position`) VALUES
(41, 86, 'Antipasti', 0),
(42, 86, 'Primi', 1),
(43, 86, 'Secondi', 2),
(44, 86, 'Dessert', 3),
(45, 87, 'Antipasti', 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `recipes`
--

CREATE TABLE `recipes` (
  `ID` int(11) NOT NULL,
  `name` tinytext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `recipes`
--

INSERT INTO `recipes` (`ID`, `name`) VALUES
(1, 'Lasagne al forno'),
(2, 'Insalata di pollo'),
(3, 'Risotto ai funghi'),
(4, 'Tiramisù'),
(5, 'Spaghetti alla carbonara'),
(6, 'Pizza Margherita'),
(7, 'Salmone alla griglia'),
(8, 'Panna cotta'),
(9, 'Pollo al curry'),
(10, 'Gnocchi al pomodoro'),
(11, 'Croissant'),
(12, 'Biscotti'),
(13, 'Vitello Tonnato');

-- --------------------------------------------------------

--
-- Struttura della tabella `roles`
--

CREATE TABLE `roles` (
  `id` char(1) NOT NULL,
  `role` varchar(128) NOT NULL DEFAULT 'servizio'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `roles`
--

INSERT INTO `roles` (`id`, `role`) VALUES
('c', 'cuoco'),
('h', 'chef'),
('o', 'organizzatore'),
('s', 'servizio');

-- --------------------------------------------------------

--
-- Struttura della tabella `servicemenu`
--

CREATE TABLE `servicemenu` (
  `id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `approved` tinyint(1) DEFAULT 0,
  `proposals` varchar(128) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `servicemenu`
--

INSERT INTO `servicemenu` (`id`, `menu_id`, `service_id`, `approved`, `proposals`) VALUES
(1, 80, 2, 0, ''),
(11, 86, 1, 0, '');

-- --------------------------------------------------------

--
-- Struttura della tabella `services`
--

CREATE TABLE `services` (
  `id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `service_date` date DEFAULT NULL,
  `time_start` time DEFAULT NULL,
  `time_end` time DEFAULT NULL,
  `expected_participants` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `services`
--

INSERT INTO `services` (`id`, `event_id`, `name`, `service_date`, `time_start`, `time_end`, `expected_participants`) VALUES
(1, 2, 'Cena', '2020-08-13', '20:00:00', '23:30:00', 25),
(2, 1, 'Coffee break mattino', '2020-09-25', '10:30:00', '11:30:00', 100),
(3, 1, 'Colazione di lavoro', '2020-09-25', '13:00:00', '14:00:00', 80),
(4, 1, 'Coffee break pomeriggio', '2020-09-25', '16:00:00', '16:30:00', 100),
(5, 1, 'Cena sociale', '2020-09-25', '20:00:00', '22:30:00', 40),
(6, 3, 'Pranzo giorno 1', '2020-10-02', '12:00:00', '15:00:00', 200),
(7, 3, 'Pranzo giorno 2', '2020-10-03', '12:00:00', '15:00:00', 300),
(8, 3, 'Pranzo giorno 3', '2020-10-04', '12:00:00', '15:00:00', 400);

-- --------------------------------------------------------

--
-- Struttura della tabella `subduties`
--

CREATE TABLE `subduties` (
  `ID` int(11) NOT NULL,
  `recipe_id` int(11) DEFAULT NULL,
  `name` tinytext DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dump dei dati per la tabella `subduties`
--

INSERT INTO `subduties` (`ID`, `recipe_id`, `name`) VALUES
(1, 1, 'Preparare il ragù di carne.'),
(2, 1, 'Preparare la besciamella.'),
(3, 1, 'Cuocere le lasagne al dente.'),
(4, 1, 'Assemblare gli strati di pasta, ragù e besciamella.'),
(5, 1, 'Cuocere in forno per 30 minuti.'),
(6, 2, 'Grigliare il pollo.'),
(7, 2, 'Tagliare il pollo grigliato a cubetti.'),
(8, 2, 'Preparare le verdure.'),
(9, 2, 'Preparare il dressing al limone.'),
(10, 2, 'Mescolare tutti gli ingredienti in una ciotola.'),
(11, 2, 'Servire l\'insalata di pollo.'),
(12, 3, 'Soffriggere i funghi con aglio e prezzemolo.'),
(13, 3, 'Aggiungere il riso e tostarlo leggermente.'),
(14, 3, 'Aggiungere gradualmente il brodo di carne e cuocere mescolando.'),
(15, 3, 'Mantecare con burro e formaggio.'),
(16, 4, 'Preparare il caffè e farlo raffreddare.'),
(17, 4, 'Preparare la crema di mascarpone.'),
(18, 4, 'Inzuppare i savoiardi nel caffè.'),
(19, 4, 'Alternare strati di savoiardi e crema in un recipiente.'),
(20, 4, 'Spolverizzare con cacao amaro.'),
(21, 5, 'Cuocere gli spaghetti in acqua salata.'),
(22, 5, 'Rosolare la pancetta in una padella.'),
(23, 5, 'Scolare la pasta e saltarla nella padella con la pancetta.'),
(24, 5, 'Aggiungere uova sbattute e formaggio.'),
(25, 5, 'Servire gli spaghetti alla carbonara con pepe nero.'),
(26, 6, 'Preparare l\'impasto per la pizza.'),
(27, 6, 'Stendere l\'impasto e aggiungere salsa di pomodoro e mozzarella.'),
(28, 6, 'Aggiungere gli ingredienti desiderati (ad esempio, basilico, olive).'),
(29, 6, 'Cuocere in forno fino a doratura.'),
(30, 6, 'Servire la pizza Margherita tagliata a fette.'),
(31, 7, 'Preparare il salmone con sale, pepe e olio d\'oliva.'),
(32, 7, 'Grigliare il salmone fino a cottura desiderata.'),
(33, 7, 'Servire il salmone alla griglia con contorni a scelta.'),
(34, 8, 'Scaldare la panna con lo zucchero e la vaniglia.'),
(35, 8, 'Ammollare la gelatina in acqua fredda.'),
(36, 8, 'Aggiungere la gelatina alla panna calda e mescolare fino a scioglimento.'),
(37, 8, 'Versare il composto nelle ciotole e mettere in frigorifero per solidificare.'),
(38, 8, 'Servire la panna cotta con salsa di frutta o caramello.'),
(39, 9, 'Tagliare il pollo a pezzi e cuocerlo in padella con olio.'),
(40, 9, 'Preparare la salsa di curry con cipolle, aglio, spezie e latte di cocco.'),
(41, 9, 'Aggiungere il pollo nella salsa e cuocere fino a cottura completa.'),
(42, 9, 'Servire il pollo al curry con riso o pane naan.'),
(43, 10, 'Preparare l\'impasto per gli gnocchi con patate, farina, uova e sale.'),
(44, 10, 'Formare gli gnocchi e cuocerli in acqua bollente salata.'),
(45, 10, 'Preparare la salsa di pomodoro con cipolla, aglio e passata di pomodoro.'),
(46, 10, 'Scolare gli gnocchi e condire con la salsa di pomodoro.'),
(47, 10, 'Servire gli gnocchi al pomodoro con formaggio grattugiato e basilico.'),
(48, 11, 'Preparare la pasta sfoglia, tagliandola in triangoli.'),
(49, 11, 'Arrotolare i triangoli dalla base verso la punta per formare i croissant.'),
(50, 11, 'Far lievitare i croissant su una teglia per 1 ora e cuocerli in forno a 180°C per 15-20 minuti.'),
(51, 12, 'Preparare l\'impasto mescolando farina, burro, zucchero e uova.'),
(52, 12, 'Aggiungere gocce di cioccolato all\'impasto e mescolare bene.'),
(53, 12, 'Formare palline con l\'impasto, disporle su una teglia e cuocere in forno a 180°C per 10-12 minuti.'),
(54, 13, 'Cuocere il vitello in acqua bollente salata con aromi e verdure per 1-2 ore.'),
(55, 13, 'Tagliare il vitello a fette sottili e disporle su un piatto.'),
(56, 13, 'Preparare la salsa tonnata mescolando tonno, maionese, capperi, acciughe, succo di limone e olio d\'oliva e distribuirla sulle fette di vitello.');

-- --------------------------------------------------------

--
-- Struttura della tabella `summarysheets`
--

CREATE TABLE `summarysheets` (
  `id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `userroles`
--

CREATE TABLE `userroles` (
  `user_id` int(11) NOT NULL,
  `role_id` char(1) NOT NULL DEFAULT 's'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `userroles`
--

INSERT INTO `userroles` (`user_id`, `role_id`) VALUES
(1, 'o'),
(2, 'o'),
(2, 'h'),
(3, 'h'),
(4, 'h'),
(4, 'c'),
(5, 'c'),
(6, 'c'),
(7, 'c'),
(8, 's'),
(9, 's'),
(10, 's'),
(7, 's');

-- --------------------------------------------------------

--
-- Struttura della tabella `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(128) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dump dei dati per la tabella `users`
--

INSERT INTO `users` (`id`, `username`) VALUES
(1, 'Carlin'),
(2, 'Lidia'),
(3, 'Tony'),
(4, 'Marinella'),
(5, 'Guido'),
(6, 'Antonietta'),
(7, 'Paola'),
(8, 'Silvia'),
(9, 'Marco'),
(10, 'Piergiorgio');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `kitchentasks`
--
ALTER TABLE `kitchentasks`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `menuitems`
--
ALTER TABLE `menuitems`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `menus`
--
ALTER TABLE `menus`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `menusections`
--
ALTER TABLE `menusections`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `recipes`
--
ALTER TABLE `recipes`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `servicemenu`
--
ALTER TABLE `servicemenu`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `subduties`
--
ALTER TABLE `subduties`
  ADD PRIMARY KEY (`ID`);

--
-- Indici per le tabelle `summarysheets`
--
ALTER TABLE `summarysheets`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `events`
--
ALTER TABLE `events`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT per la tabella `kitchentasks`
--
ALTER TABLE `kitchentasks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=94;

--
-- AUTO_INCREMENT per la tabella `menuitems`
--
ALTER TABLE `menuitems`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=131;

--
-- AUTO_INCREMENT per la tabella `menus`
--
ALTER TABLE `menus`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=89;

--
-- AUTO_INCREMENT per la tabella `menusections`
--
ALTER TABLE `menusections`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT per la tabella `recipes`
--
ALTER TABLE `recipes`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT per la tabella `servicemenu`
--
ALTER TABLE `servicemenu`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT per la tabella `services`
--
ALTER TABLE `services`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT per la tabella `subduties`
--
ALTER TABLE `subduties`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT per la tabella `summarysheets`
--
ALTER TABLE `summarysheets`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT per la tabella `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
