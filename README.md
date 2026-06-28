# MokuLib Server

Backend server for MokuLib, a personal library system.

## Deploy

### Database

1. Prepare a MySQL 8 environment.
2. Create mokulib database: ``CREATE DATABASE `mokulib`;``.
3. Create dedicated database user: ``CREATE USER 'mokulib'@'%' IDENTIFIED BY 'mokulib';``.
4. Grant privileges to the user: ``GRANT ALL PRIVILEGES ON `mokulib`.* TO 'mokulib'@'%';``.
