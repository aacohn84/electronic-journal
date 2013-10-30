SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `electronic-journal` ;
CREATE SCHEMA IF NOT EXISTS `electronic-journal` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `electronic-journal` ;

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
  UNIQUE INDEX `id_group_UNIQUE` (`id_group` ASC))
ENGINE = InnoDB;


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
-- Table `electronic-journal`.`prompt`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`prompt` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`prompt` (
  `id_prompt` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_creator` INT UNSIGNED NOT NULL,
  `id_group` INT UNSIGNED NOT NULL,
  `text` VARCHAR(1024) NOT NULL,
  `creation_date` DATETIME NOT NULL,
  PRIMARY KEY (`id_prompt`),
  UNIQUE INDEX `id_prompt_UNIQUE` (`id_prompt` ASC),
  INDEX `prompt_id_creator_fk_idx` (`id_creator` ASC),
  INDEX `prompt_id_group_fk_idx` (`id_group` ASC),
  CONSTRAINT `prompt_id_creator_fk`
    FOREIGN KEY (`id_creator`)
    REFERENCES `electronic-journal`.`user` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
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
  PRIMARY KEY (`id_response`),
  UNIQUE INDEX `id_response_UNIQUE` (`id_response` ASC),
  INDEX `response_id_prompt_fk_idx` (`id_prompt` ASC),
  INDEX `response_id_responder_fk_idx` (`id_responder` ASC),
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
-- Table `electronic-journal`.`profile_question`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`profile_question` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`profile_question` (
  `id_question` INT UNSIGNED NOT NULL,
  `text` VARCHAR(140) NOT NULL,
  PRIMARY KEY (`id_question`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `electronic-journal`.`profile_answer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `electronic-journal`.`profile_answer` ;

CREATE TABLE IF NOT EXISTS `electronic-journal`.`profile_answer` (
  `id_user` INT UNSIGNED NOT NULL,
  `id_question` INT UNSIGNED NOT NULL,
  `text` VARCHAR(140) NOT NULL,
  PRIMARY KEY (`id_user`),
  INDEX `answer_id_question_fk_idx` (`id_question` ASC),
  CONSTRAINT `answer_id_question_fk`
    FOREIGN KEY (`id_question`)
    REFERENCES `electronic-journal`.`profile_question` (`id_question`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `answer_id_user_fk`
    FOREIGN KEY (`id_user`)
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
CREATE PROCEDURE `WRITE_PROMPT` (IN `creatorId` INT, IN `groupId` INT, IN `promptText` VARCHAR(1024))
BEGIN
	INSERT INTO `Prompt` (
		`id_creator`,
		`id_group`,
		`text`,
		`creation_date`
	) VALUES (
		`creatorId`,
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
	IN `teacherid` INT, 
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

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
