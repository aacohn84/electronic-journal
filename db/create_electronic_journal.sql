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
  `subject` VARCHAR(45) NOT NULL,
  `passphrase` VARCHAR(140) NULL,
  PRIMARY KEY (`id_group`),
  UNIQUE INDEX `id_group_UNIQUE` (`id_group` ASC),
  INDEX `group_id_teacher_fk_idx` (`id_teacher` ASC),
  UNIQUE INDEX `passphrase_UNIQUE` (`passphrase` ASC),
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `electronic-journal`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `electronic-journal`;
INSERT INTO `electronic-journal`.`user` (`id_user`, `unique_name`, `password`, `first_name`, `last_name`) VALUES (1, 'aacohn84', 'password', 'Aaron', 'Cohn');

COMMIT;

