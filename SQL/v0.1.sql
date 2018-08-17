create database pri;
use pri;

DROP TABLE IF EXISTS `sysconfig`;
CREATE TABLE `sysconfig` (
  `confKey` varchar(255) NOT NULL,
  `confValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`confKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 comment='配置信息';