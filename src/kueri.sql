CREATE SCHEMA `kasir_minimarket_schema`;

CREATE TABLE `kasir_minimarket_schema`.`users` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(30) NOT NULL,
  `password` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `password_UNIQUE` (`password` ASC) VISIBLE);

ALTER TABLE `kasir_minimarket_schema`.`users`
DROP INDEX `password_UNIQUE` ;
;

CREATE TABLE `kasir_minimarket_schema`.`products` (
  `id_product` INT NOT NULL AUTO_INCREMENT,
  `product_name` VARCHAR(50) NOT NULL,
  `product_price` BIGINT NOT NULL,
  `discount` BIGINT NOT NULL,
  PRIMARY KEY (`id_product`));

CREATE TABLE `kasir_minimarket_schema`.`transaction` (
  `id_transaction` INT NOT NULL AUTO_INCREMENT,
  `id_product` INT NOT NULL,
  `number_of_items` INT NOT NULL,
  `total_price` BIGINT NOT NULL,
  PRIMARY KEY (`id_transaction`),
  INDEX `id_product_idx` (`id_product` ASC) VISIBLE,
  CONSTRAINT `id_product`
    FOREIGN KEY (`id_product`)
    REFERENCES `kasir_minimarket_schema`.`products` (`id_product`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `kasir_minimarket_schema`.`payment` (
  `id_payment` INT NOT NULL,
  `transaction_total` BIGINT NOT NULL,
  `payment_total` BIGINT NOT NULL,
  `change` BIGINT NOT NULL,
  PRIMARY KEY (`id_payment`));

ALTER TABLE `kasir_minimarket_schema`.`users`
CHANGE COLUMN `password` `password` VARCHAR(60) NOT NULL ;

ALTER TABLE `kasir_minimarket_schema`.`products`
ADD COLUMN `id_user` INT NOT NULL AFTER `discount`,
ADD INDEX `id_use_idx` (`id_user` ASC) VISIBLE;
;
ALTER TABLE `kasir_minimarket_schema`.`products`
ADD CONSTRAINT `id_user`
  FOREIGN KEY (`id_user`)
  REFERENCES `kasir_minimarket_schema`.`users` (`id_user`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `kasir_minimarket_schema`.`products`
DROP FOREIGN KEY `id_user`;
ALTER TABLE `kasir_minimarket_schema`.`products`
ADD CONSTRAINT `fk_id_user`
  FOREIGN KEY (`id_user`)
  REFERENCES `kasir_minimarket_schema`.`users` (`id_user`);

ALTER TABLE `kasir_minimarket_schema`.`transaction`
ADD COLUMN `id_user` INT NOT NULL AFTER `total_price`,
ADD INDEX `fk_transaction_id_user_idx` (`id_user` ASC) VISIBLE;
;
ALTER TABLE `kasir_minimarket_schema`.`transaction`
ADD CONSTRAINT `fk_transaction_id_user`
  FOREIGN KEY (`id_user`)
  REFERENCES `kasir_minimarket_schema`.`users` (`id_user`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

ALTER TABLE `kasir_minimarket_schema`.`payment`
ADD COLUMN `date` DATETIME NOT NULL AFTER `change`;

ALTER TABLE `kasir_minimarket_schema`.`payment`
CHANGE COLUMN `id_payment` `id_payment` INT NOT NULL AUTO_INCREMENT ;
