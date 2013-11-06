SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `electronic-journal` ;
CREATE SCHEMA IF NOT EXISTS `electronic-journal` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `electronic-journal` ;

-- -----------------------------------------------------
-- Table `electronic-journal`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`user` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`user` (
  `id_user` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `unique_name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE INDEX `unique_name_UNIQUE` (`unique_name` ASC),
  UNIQUE INDEX `iduser_UNIQUE` (`id_user` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `electronic-journal`.`teacher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`teacher` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`teacher` (
  `id_teacher` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_teacher`),
  UNIQUE INDEX `id_teacher_UNIQUE` (`id_teacher` ASC),
  CONSTRAINT `teacher_id_teacher_fk`
    FOREIGN KEY (`id_teacher`)
    REFERENCES `electronic-journal`.`user` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `electronic-journal`.`group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`group` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`group` (
  `id_group` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_teacher` INT UNSIGNED NOT NULL,
  `period` INT UNSIGNED NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `subject` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_group`),
  UNIQUE INDEX `id_group_UNIQUE` (`id_group` ASC),
  INDEX `group_id_teacher_fk_idx` (`id_teacher` ASC),
  CONSTRAINT `group_id_teacher_fk`
    FOREIGN KEY (`id_teacher`)
    REFERENCES `electronic-journal`.`teacher` (`id_teacher`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `electronic-journal`.`prompt`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`prompt` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`prompt` (
  `id_prompt` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_group` INT UNSIGNED NOT NULL,
  `text` VARCHAR(1024) NOT NULL,
  `creation_date` DATETIME NOT NULL,
  PRIMARY KEY (`id_prompt`),
  UNIQUE INDEX `id_prompt_UNIQUE` (`id_prompt` ASC),
  INDEX `prompt_id_group_fk_idx` (`id_group` ASC),
  CONSTRAINT `prompt_id_group_fk`
    FOREIGN KEY (`id_group`)
    REFERENCES `electronic-journal`.`group` (`id_group`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `electronic-journal`.`prompt_response`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`prompt_response` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`prompt_response` (
  `id_response` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_prompt` INT UNSIGNED NOT NULL,
  `id_responder` INT UNSIGNED NOT NULL,
  `text` VARCHAR(1024) NOT NULL,
  `creation_date` DATETIME NOT NULL,
  UNIQUE INDEX `id_response_UNIQUE` (`id_response` ASC),
  INDEX `response_id_prompt_fk_idx` (`id_prompt` ASC),
  INDEX `response_id_responder_fk_idx` (`id_responder` ASC),
  PRIMARY KEY (`id_prompt`, `id_responder`),
  CONSTRAINT `response_id_prompt_fk`
    FOREIGN KEY (`id_prompt`)
    REFERENCES `electronic-journal`.`prompt` (`id_prompt`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `response_id_responder_fk`
    FOREIGN KEY (`id_responder`)
    REFERENCES `electronic-journal`.`user` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `electronic-journal`.`comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`comment` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`comment` (
  `id_comment` INT UNSIGNED NOT NULL,
  `id_response` INT UNSIGNED NOT NULL,
  `id_commenter` INT UNSIGNED NOT NULL,
  `text` VARCHAR(1024) NULL,
  `creation_date` DATETIME NULL,
  PRIMARY KEY (`id_comment`),
  UNIQUE INDEX `idcomment_UNIQUE` (`id_comment` ASC),
  INDEX `comment_id_response_fk_idx` (`id_response` ASC),
  INDEX `comment_id_responder_fk_idx` (`id_commenter` ASC),
  CONSTRAINT `comment_id_response_fk`
    FOREIGN KEY (`id_response`)
    REFERENCES `electronic-journal`.`prompt_response` (`id_response`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `comment_id_commenter_fk`
    FOREIGN KEY (`id_commenter`)
    REFERENCES `electronic-journal`.`user` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `electronic-journal`.`roster`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`roster` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`roster` (
  `id_group` INT UNSIGNED NOT NULL,
  `id_user` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_group`, `id_user`),
  INDEX `roster_id_user_fk_idx` (`id_user` ASC),
  CONSTRAINT `roster_id_group_fk`
    FOREIGN KEY (`id_group`)
    REFERENCES `electronic-journal`.`group` (`id_group`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `roster_id_user_fk`
    FOREIGN KEY (`id_user`)
    REFERENCES `electronic-journal`.`user` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `electronic-journal` ;

-- -----------------------------------------------------
-- procedure GET_RESPONSES
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`GET_RESPONSES`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `GET_RESPONSES`(IN user_id INT)
BEGIN
	SELECT
		*
	FROM
		`prompt_response`
	WHERE
		`id_responder` = user_id
	ORDER BY
		creation_date;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure GET_USER_BY_NAME
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`GET_USER_BY_NAME`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `GET_USER_BY_NAME`(IN unique_user_name VARCHAR(45))
BEGIN
	SELECT
		*
	FROM
		`user`
	WHERE
		`unique_name` = unique_user_name;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure GET_USER_BY_ID
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`GET_USER_BY_ID`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `GET_USER_BY_ID`(IN user_id INT)
BEGIN
	SELECT
		*
	FROM
		`user`
	WHERE
		`id_user` = user_id;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure WRITE_PROMPT
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`WRITE_PROMPT`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `WRITE_PROMPT` (IN `groupId` INT, IN `promptText` VARCHAR(1024))
BEGIN
	INSERT INTO `Prompt` (
		`id_group`,
		`text`,
		`creation_date`
	) VALUES (
		`groupId`,
		`promptText`,
		NOW()
	);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure CREATE_USER
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`CREATE_USER`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `CREATE_USER` (
	IN `uniqueName` VARCHAR(45), 
	IN `password` VARCHAR(45), 
	IN `firstName` VARCHAR(45), 
	IN `lastName` VARCHAR(45))
BEGIN
	INSERT INTO `user` (
		`unique_name`,
		`password`,
		`first_name`,
		`last_name`
	) VALUES (
		`uniqueName`,
		`password`,
		`firstName`,
		`lastName`
	);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure CREATE_GROUP
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`CREATE_GROUP`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `CREATE_GROUP` (
	IN `teacherId` INT, 
	IN `period` INT, 
	IN `name` VARCHAR(45), 
	IN `subject` VARCHAR(45))
BEGIN
	INSERT INTO `group` (
		`id_teacher`,
		`period`,
		`name`,
		`subject`
	) VALUES (
		`teacherId`,
		`period`,
		`name`,
		`subject`
	);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure SAVE_RESPONSE
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`SAVE_RESPONSE`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `SAVE_RESPONSE` (
	IN responderId INT,
	IN promptId INT,
	IN responseText VARCHAR(1024))
BEGIN
	INSERT INTO `prompt_response` (
		`id_responder`,
		`id_prompt`,
		`text`,
		`creation_date`
	) VALUES (
		responderId,
		promptId,
		responseText,
		NOW()
	) ON DUPLICATE KEY UPDATE `text` = values(`text`);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure GET_MOST_RECENT_PROMPT_AND_RESPONSE
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`GET_MOST_RECENT_PROMPT_AND_RESPONSE`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `GET_MOST_RECENT_PROMPT_AND_RESPONSE` (
	IN groupId INT, 
	IN responderId INT)
BEGIN
	SELECT * 
	FROM `prompt` LEFT JOIN (
		SELECT * FROM `prompt_response` WHERE `id_responder` = responderId
	) `prompt_response`
	USING(`id_prompt`)
	WHERE `id_group` = groupId
	ORDER BY `prompt`.`creation_date`
	LIMIT 1;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure ADD_TO_ROSTER
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`ADD_TO_ROSTER`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `ADD_TO_ROSTER` (IN `groupId` INT, IN `userId` INT)
BEGIN
	INSERT INTO `roster` (
		`id_group`,
		`id_user`
	) VALUES (
		`groupId`,
		`userId`
	) 
	ON DUPLICATE KEY UPDATE `id_group` = `groupId`, `id_user` = `userid`;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure GET_GROUPS
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`GET_GROUPS`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `GET_GROUPS` (IN `userId` INT)
BEGIN
	SELECT * 
	FROM `roster` LEFT JOIN `group`
	USING(`id_group`)
	WHERE `id_user` = `userId`;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure SET_TEACHER
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`SET_TEACHER`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `SET_TEACHER` (IN userId INT, IN isTeacher BOOL)
BEGIN
	IF isTeacher = TRUE THEN
		INSERT INTO `teacher` VALUES (userId) 
		ON DUPLICATE KEY UPDATE `id_teacher` = userId;
	ELSE
		DELETE FROM `teacher` WHERE `id_teacher` = userId;
	END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure GET_USER_BY_NAME_AND_PASSWORD
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`GET_USER_BY_NAME_AND_PASSWORD`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `GET_USER_BY_NAME_AND_PASSWORD` (
	IN `username` VARCHAR(45), 
	IN `password` VARCHAR(45))
BEGIN
	SELECT * FROM `user` WHERE `unique_name` = `username` AND `user`.`password` = `password`;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure IS_TEACHER
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`IS_TEACHER`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `IS_TEACHER` (
	IN `userId` INT,
	OUT `isTeacher` BOOLEAN)
BEGIN
	DECLARE teacherId INT;

	DECLARE cur1 CURSOR FOR 
		SELECT * FROM `teacher` WHERE `id_teacher` = `userId`;
	
	SET `isTeacher` = FALSE;
	
	OPEN cur1;
	FETCH cur1 INTO teacherId;
	
	IF teacherId IS NOT NULL THEN 
		SET `isTeacher` = TRUE;
	END IF;
	
	CLOSE cur1;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure GET_GROUPS_TAUGHT_BY_USER
-- -----------------------------------------------------

USE `electronic-journal`;
DROP procedure IF EXISTS `electronic-journal`.`GET_GROUPS_TAUGHT_BY_USER`;

DELIMITER $$
USE `electronic-journal`$$
CREATE PROCEDURE `GET_GROUPS_TAUGHT_BY_USER` (IN `teacherId` INT)
BEGIN
	SELECT * FROM `group` WHERE `id_teacher` = `teacherId`;
END$$

DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
