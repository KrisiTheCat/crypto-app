-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Време на генериране: 16 апр 2025 в 19:47
-- Версия на сървъра: 8.2.0
-- Версия на PHP: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данни: `crypto_ms`
--

-- --------------------------------------------------------

--
-- Структура на таблица `crypto`
--

DROP TABLE IF EXISTS `crypto`;
CREATE TABLE IF NOT EXISTS `crypto` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `symbol` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Схема на данните от таблица `crypto`
--

INSERT INTO `crypto` (`id`, `name`, `symbol`) VALUES
(1, 'Bitcoin', 'BTC'),
(2, 'Ethereum', 'ETH'),
(3, 'Tether', 'USDT'),
(4, 'XRP', 'XRP'),
(5, 'BNB', 'BNB'),
(6, 'Solana', 'SOL'),
(7, 'USDC', 'USDC'),
(8, 'TRON', 'TRX'),
(9, 'Dogecoin', 'DOGE'),
(10, 'Cardano', 'ADA'),
(11, 'Wrapped Bitcoin', 'WBTC'),
(12, 'LEO Token', 'LEO'),
(13, 'Chainlink', 'LINK'),
(14, 'USDS', 'USDS'),
(15, 'Stellar', 'XLM'),
(16, 'Monero', 'XMR'),
(17, 'Uniswap', 'UNI'),
(18, 'Aeve', 'AAVE'),
(19, 'Bittensor', 'TAO'),
(20, 'Maker', 'MKR');

-- --------------------------------------------------------

--
-- Структура на таблица `crypto_seq`
--

DROP TABLE IF EXISTS `crypto_seq`;
CREATE TABLE IF NOT EXISTS `crypto_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Схема на данните от таблица `crypto_seq`
--

INSERT INTO `crypto_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Структура на таблица `holding`
--

DROP TABLE IF EXISTS `holding`;
CREATE TABLE IF NOT EXISTS `holding` (
  `id` bigint NOT NULL,
  `investment` double DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `crypto_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4piakq1b5cv13slxmbxtobjr0` (`crypto_id`),
  KEY `FKqj6c499yadk3ka6gakt0t527s` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Структура на таблица `holding_seq`
--

DROP TABLE IF EXISTS `holding_seq`;
CREATE TABLE IF NOT EXISTS `holding_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Схема на данните от таблица `holding_seq`
--

INSERT INTO `holding_seq` (`next_val`) VALUES
(1351);

-- --------------------------------------------------------

--
-- Структура на таблица `transaction`
--

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE IF NOT EXISTS `transaction` (
  `id` bigint NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `method` tinyint DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` double NOT NULL,
  `crypto_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9qvcqovc8mo7kdapyl8rbc1jt` (`crypto_id`),
  KEY `FKsg7jp0aj6qipr50856wf6vbw1` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Структура на таблица `transaction_seq`
--

DROP TABLE IF EXISTS `transaction_seq`;
CREATE TABLE IF NOT EXISTS `transaction_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Схема на данните от таблица `transaction_seq`
--

INSERT INTO `transaction_seq` (`next_val`) VALUES
(1651);

-- --------------------------------------------------------

--
-- Структура на таблица `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL,
  `balance` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Схема на данните от таблица `user`
--

INSERT INTO `user` (`id`, `balance`, `name`, `password`) VALUES
(1, 10000, 'Kristina Stoyanova', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4');

-- --------------------------------------------------------

--
-- Структура на таблица `user_holdings`
--

DROP TABLE IF EXISTS `user_holdings`;
CREATE TABLE IF NOT EXISTS `user_holdings` (
  `user_id` bigint NOT NULL,
  `holdings_id` bigint NOT NULL,
  UNIQUE KEY `UKigp76d7uclqfkthci7jdy9hud` (`holdings_id`),
  KEY `FKjct535e895e22weeqriqa3tmy` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Структура на таблица `user_seq`
--

DROP TABLE IF EXISTS `user_seq`;
CREATE TABLE IF NOT EXISTS `user_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Схема на данните от таблица `user_seq`
--

INSERT INTO `user_seq` (`next_val`) VALUES
(1);

-- --------------------------------------------------------

--
-- Структура на таблица `user_transactions`
--

DROP TABLE IF EXISTS `user_transactions`;
CREATE TABLE IF NOT EXISTS `user_transactions` (
  `user_id` bigint NOT NULL,
  `transactions_id` bigint NOT NULL,
  UNIQUE KEY `UKk12ybnbbgjjr7jr9d2831xoua` (`transactions_id`),
  KEY `FK7ow9tl4pncou0lojsgqej6sce` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Ограничения за дъмпнати таблици
--

--
-- Ограничения за таблица `holding`
--
ALTER TABLE `holding`
  ADD CONSTRAINT `FK4piakq1b5cv13slxmbxtobjr0` FOREIGN KEY (`crypto_id`) REFERENCES `crypto` (`id`),
  ADD CONSTRAINT `FKqj6c499yadk3ka6gakt0t527s` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Ограничения за таблица `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `FK9qvcqovc8mo7kdapyl8rbc1jt` FOREIGN KEY (`crypto_id`) REFERENCES `crypto` (`id`),
  ADD CONSTRAINT `FKsg7jp0aj6qipr50856wf6vbw1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Ограничения за таблица `user_holdings`
--
ALTER TABLE `user_holdings`
  ADD CONSTRAINT `FKc5xhp2gaay3nr828o43hk17l5` FOREIGN KEY (`holdings_id`) REFERENCES `holding` (`id`),
  ADD CONSTRAINT `FKjct535e895e22weeqriqa3tmy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Ограничения за таблица `user_transactions`
--
ALTER TABLE `user_transactions`
  ADD CONSTRAINT `FK7ow9tl4pncou0lojsgqej6sce` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKli5uya1wifm860dqhjke7kyuj` FOREIGN KEY (`transactions_id`) REFERENCES `transaction` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
