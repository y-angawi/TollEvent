/*Name: Yahya Angawi
  Student ID: D00233709
*/

CREATE DATABASE IF NOT EXISTS `user_database`;
USE `user_database`;

CREATE TABLE IF NOT EXISTS `TollEvents` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
    `imgId` numeric NOT NULL,
    `reg` varchar(50) NOT NULL,
    `TIMESTAMP` TIMESTAMP(3) NOT NULL,
    PRIMARY KEY  (`imgId`)
  );

            INSERT INTO TollEvents VALUES (null, "151DL200"),
            		          (null, "152DL345"),
            			  (null, "161C3457"),
            			  (null, "181MH3456");
SELECT * FROM `tollevents` WHERE `reg` = "191LH1111"

CREATE TABLE IF NOT EXISTS `Vehicles` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REG` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
  );
-- //=====================================================//
CREATE TABLE IF NOT EXISTS `customer` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `address` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
  );

INSERT INTO customer VALUES (null, "yahya angawi", "123 whatever st."),
            		          (null, "adam", "123 whatever st."),
            			  (null, "anne", "123 whatever st."),
                                  (null, "dermont", "123 whatever st."),
            			  (null, "derek", "123 whatever st."),
                                  (null, "john doe", "123 whatever st."),
            			  (null, "john wick", "123 whatever st."),
                                  (null, "alice wonderland", "123 whatever st."),
                                  (null, "crowley", "123 whatever st."),
            			  (null, "hulk green", "123 whatever st.");

CREATE TABLE IF NOT EXISTS `vehicle` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REG` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  PRIMARY KEY  (`ID`)
  );

INSERT INTO vehicle VALUES (null, "151DL200", "car"),
            		          (null, "152DL345", "van"),
            			  (null, "161C3457", "Truck"),
                                  (null, "181MH3456", "car"),
            			  (null, "181MH3458", "van"),
                                  (null, "181MH3459", "Truck"),
            			  (null, "181MH3461", "car"),
                                  (null, "191LH1111", "van"),
            			  (null, "191LH1112", "Truck"),
                                  (null, "191LH1113", "car"),
                                  
            		          (null, "191LH1114", "van"),
            			  (null, "192D33457", "Truck"),
            		          (null, "201CN3456", "car"),
            			  (null, "201CN3457", "van"),
                                  (null, "201LH3025", "Truck"),
                                  (null, "201LH304", "car"),
                                  (null, "201LH305", "van"),
            			  (null, "201LH306", "Truck"),
            			  (null, "201LH3064", "car"),
            		          (null, "201LH307", "van"),

            			  (null, "201LH3076", "Truck"),
                                  (null, "201LH308", "car"),
            			  (null, "201LH3083", "car"),
            			  (null, "201LH309", "van"),
                                  (null, "201LH310", "Truck"),
                                  (null, "201LH311", "car"),
                                  (null, "201LH312", "van"),
            			  (null, "201LH355", "Truck"),
            			  (null, "201LH777", "car"),
                                  (null, "151MN666", "van");

CREATE TABLE IF NOT EXISTS `customer_vehicles` (
  `customerid` int(11) NOT NULL,
  `vehicleid` int(11) NOT NULL,
  PRIMARY KEY  (`customerid`, `vehicleid`),
FOREIGN KEY (`customerid`)
      REFERENCES customer(`id`),
FOREIGN KEY (`vehicleid`)
      REFERENCES vehicle(`id`)
  );

INSERT INTO customer_vehicles VALUES ("1", "1"),
            		          ("1", "2"),
            			  ("1", "3"),
                                  ("2", "4"),
            			  ("2", "5"),
                                  ("2", "6"),
            			  ("3", "7"),
                                  ("3", "8"),
            			  ("3", "9"),
                                  ("4", "10"),
                                  
            		          ("4", "11"),
            			  ("4", "12"),
            		          ("5", "13"),
            			  ("5", "14"),
                                  ("5", "15"),
                                  ("6", "16"),
                                  ("6", "17"),
            			  ("6", "18"),
            			  ("7", "19"),
            		          ("7", "20"),

            			  ("7", "21"),
                                  ("8", "22"),
            			  ("8", "23"),
            			  ("8", "24"),
                                  ("9", "25"),
                                  ("9", "26"),
                                  ("9", "27"),
            			  ("10", "28"),
            			  ( "10", "29"),
                                  ("10", "30");


CREATE TABLE IF NOT EXISTS `vehicle_type_cost` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `cost` int(11) NOT NULL,
  PRIMARY KEY  (`ID`)
  );

INSERT INTO vehicle_type_cost VALUES (null, "car", "5"),
            		          (null, "van", "10"),
            			  (null, "Truck", "15");
-- //====================================phpmyadmin quiry
SELECT `name`, sum(`cost`) as `cost` FROM `TollEvents`, `customer`, `vehicle`, 
`customer_vehicles`, `vehicle_type_cost` WHERE `TollEvents`.reg = `vehicle`.reg 
and `vehicle`.id = `customer_vehicles`.vehicleid 
and `customer`.id = `customer_vehicles`.customerid 
and `vehicle`.type = `vehicle_type_cost`.type 
AND MONTH(`TollEvents`.`TIMESTAMP`) < MONTH(curdate())  and `TollEvents`.bill = null GROUP BY `customer`.name ORDER BY `customer`.name ASC;

