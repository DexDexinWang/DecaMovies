drop database decamovies;
create database decamovies;
use decamovies;

CREATE TABLE `Users` (
  `UserID` varchar(255) NOT NULL,
  `PassWord` varchar(255) NOT NULL,
  `FirstName` varchar(255) DEFAULT NULL,
  `LastName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UserID`)
);

CREATE TABLE `Movies` (
  `MovieID` varchar(255) NOT NULL,
  `MovieName` varchar(255) DEFAULT NULL,
  `Director` varchar(255) DEFAULT NULL,
  `Actor1` varchar(255) DEFAULT NULL,
  `Actor2` varchar(255) DEFAULT NULL,
  `Actor3` varchar(255) DEFAULT NULL,
  `ReleaseDate` varchar(255)  DEFAULT NULL,
  `Duration` int DEFAULT 0,
  `Rating` double DEFAULT 0.0,
  `OfficialURL` varchar(255) DEFAULT NULL,
  `ImaageURL` varchar(255) DEFAULT NULL,
  `Description` int DEFAULT NULL,
  PRIMARY KEY (`MovieID`)
);

CREATE TABLE `History` (
  `HistoryID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `UserID` varchar(255) NOT NULL,
  `MovieID` varchar(255) NOT NULL,
  `LastFavorTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`HistoryID`),
  KEY `User_Id` (`UserID`),
  KEY `Movie_Id` (`MovieID`),
  CONSTRAINT `HistoryID_MovieID_FK` FOREIGN KEY (`MovieID`) REFERENCES `Movies` (`MovieID`),
  CONSTRAINT `HistoryID_UserID_FK` FOREIGN KEY (`UserID`) REFERENCES `Users` (`UserID`)
); 


CREATE TABLE `Theaters` (
  `TheaterID` varchar(255) NOT NULL,
  `TheaterName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`TheaterID`)
);

CREATE TABLE `ShowTime` (
  `TheaterID` varchar(255) NOT NULL,
  `MovieID` varchar(255) NOT NULL,
  `DateTime` varchar(255) NOT NULL,
  `Links` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`TheaterID`,`MovieID`,`DateTime`),
  CONSTRAINT `HistoryIDUserID_MovieID_FK1` FOREIGN KEY (`MovieID`) REFERENCES `Movies` (`MovieID`),
  CONSTRAINT `HistoryID_TheaterID_FK` FOREIGN KEY (`TheaterID`) REFERENCES `Theaters` (`TheaterID`)
);


CREATE TABLE `Categories` (
  `MovieID` varchar(255) NOT NULL,
  `CategoryName` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`MovieID`,`CategoryName`),
  CONSTRAINT `category_movie_fk` FOREIGN KEY (`MovieID`) REFERENCES `Movies` (`MovieID`)
);


