## DATABASE Setup Documentation

> [!IMPORTANT] 
> Following are the scripts / queries that needs to be executed to setup the database.
> <br>This includes database, tables creation and creating the indexes that will help for faster retrieval.


1. Once you have the MySQL DB Instance connected.
2. Create a new a database `yuda_platform_db`.
```
-- Creating New Database
CREATE DATABASE `yuda_platform_db`;
````

3. Create a New Table `channel_mapping` to store the Mapping between the Channel Handle and Channel Id.
```
-- New Table to store the mapping b/w channel handle and channel id. 
CREATE TABLE `yuda_platform_db`.`channel_mapping` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `channel_handle` varchar(200) NOT NULL,
  `channel_id` varchar(200) NOT NULL,
  `created_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `channel_handle` (`channel_handle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT * FROM `yuda_platform_db`.`channel_mapping`;

-- Rollback:  Delete the Table
DROP TABLE `yuda_platform_db`.`channel_mapping`;
```


4. Create a New Table `video_details` to store the details related to videos like (title and description, etc).
```
-- New Table to store the video details
CREATE TABLE `yuda_platform_db`.`video_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `video_id` varchar(100) NOT NULL,
  `title` varchar(500) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `channel_id` varchar(200) NOT NULL,
  `created_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `publication_datetime` timestamp NOT NULL,
  `is_deleted` tinyint NOT NULL,
   PRIMARY KEY (`id`),
   UNIQUE KEY `video_id` (`video_id`),
   KEY `channel_id_idx` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT * FROM `yuda_platform_db`.`video_details`;
SHOW INDEXES from `yuda_platform_db`.`video_details`;

-- Rollback:  Delete the Table
DROP TABLE `yuda_platform_db`.`video_details`;
```


5. Create a New Table `video_stats` to store the statistics details related to videos like (view_count and like_count, etc).
```
-- New Table to store the video statistics
CREATE TABLE `yuda_platform_db`.`video_stats` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `video_id` varchar(100) NOT NULL,
  `view_count` bigint NOT NULL,
  `like_count` bigint NOT NULL,
  `comment_count` bigint NOT NULL,
  `created_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `video_id_created_datetime_idx` (`video_id`,`created_datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT * FROM `yuda_platform_db`.`video_stats`;

SHOW INDEXES from `yuda_platform_db`.`video_stats`;
-- Rollback:  Delete the Table
DROP TABLE `yuda_platform_db`.`video_stats`;

```
