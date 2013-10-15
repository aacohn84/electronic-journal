SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `electronic-journal` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `electronic-journal` ;

-- -----------------------------------------------------
-- Table `electronic-journal`.`group`
-- -----------------------------------------------------
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
CREATE TABLE IF NOT EXISTS `electronic-journal`.`user` (
  `id_user` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_group` INT UNSIGNED NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `unique_name` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE INDEX `unique_name_UNIQUE` (`unique_name` ASC),
  UNIQUE INDEX `iduser_UNIQUE` (`id_user` ASC),
  INDEX `user_id_group_fk_idx` (`id_group` ASC),
  CONSTRAINT `user_id_group_fk`
    FOREIGN KEY (`id_group`)
    REFERENCES `electronic-journal`.`group` (`id_group`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `electronic-journal`.`prompt`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `electronic-journal`.`prompt` (
  `id_prompt` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_creator` INT UNSIGNED NOT NULL,
  `id_group` INT UNSIGNED NOT NULL,
  `text` VARCHAR(1024) NOT NULL,
  `creation_date` DATETIME NOT NULL,
  `due_date` DATETIME NULL,
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
CREATE TABLE IF NOT EXISTS `electronic-journal`.`profile_question` (
  `id_question` INT UNSIGNED NOT NULL,
  `text` VARCHAR(140) NOT NULL,
  PRIMARY KEY (`id_question`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `electronic-journal`.`profile_answer`
-- -----------------------------------------------------
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
