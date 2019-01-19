CREATE DATABASE `kivaexercise` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

CREATE TABLE `lenderpayment` (
  `lenderPaymentId` varchar(128) NOT NULL,
  `loanId` varchar(128) NOT NULL,
  `lenderId` varchar(128) NOT NULL,
  `installments` int(11) NOT NULL,
  `outstandingInstallments` int(11) NOT NULL,
  `amount` double NOT NULL,
  `outstandingAmount` double NOT NULL,
  `firstinstallmentDate` varchar(64) DEFAULT NULL,
  `createdEpoc` bigint(11) NOT NULL,
  `updatedEpoc` bigint(11) NOT NULL,
  PRIMARY KEY (`lenderPaymentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `payment` (
  `paymentId` varchar(128) NOT NULL,
  `lenderPaymentId` varchar(128) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `createdEpoc` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`paymentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


