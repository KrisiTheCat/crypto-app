-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1:3306
-- Време на генериране: 18 апр 2025 в 20:01
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
(3, 'Monero', 'XMR'),
(4, 'Solana', 'SOL'),
(5, 'XRP', 'XRP'),
(6, 'Wrapped Bitcoin', 'WBTC'),
(7, 'Binance Coin USD', 'BUSD'),
(8, 'Chainlink', 'LINK'),
(9, 'USDC', 'USDC'),
(10, 'TRON', 'TRX'),
(11, 'Dogecoin', 'DOGE'),
(12, 'Cardano', 'ADA'),
(13, 'Uniswap', 'UNI'),
(14, 'Tether', 'USDT'),
(15, 'LEO Token', 'LEO'),
(16, 'USDS', 'USDS'),
(17, 'Stellar', 'XLM'),
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
  `id` bigint NOT NULL AUTO_INCREMENT,
  `investment` double DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `crypto_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `holding_crypto_id` (`crypto_id`),
  KEY `holding_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
(1);

-- --------------------------------------------------------

--
-- Структура на таблица `transaction`
--

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE IF NOT EXISTS `transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `crypto_id` bigint DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` double NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `method` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `crypto_id` (`crypto_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
(1);

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

--
-- Ограничения за дъмпнати таблици
--

--
-- Ограничения за таблица `holding`
--
ALTER TABLE `holding`
  ADD CONSTRAINT `FK4piakq1b5cv13slxmbxtobjr0` FOREIGN KEY (`crypto_id`) REFERENCES `crypto` (`id`),
  ADD CONSTRAINT `FKqj6c499yadk3ka6gakt0t527s` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `holding_crypto_id` FOREIGN KEY (`crypto_id`) REFERENCES `crypto` (`id`),
  ADD CONSTRAINT `holding_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Ограничения за таблица `transaction`
--
ALTER TABLE `transaction`
  ADD CONSTRAINT `transaction_crypto_id` FOREIGN KEY (`crypto_id`) REFERENCES `crypto` (`id`),
  ADD CONSTRAINT `transaction_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
