CREATE USER 'app_user'@'%' IDENTIFIED BY 'rpavouetm42';
GRANT ALL PRIVILEGES ON secretaria_transporte.* TO 'app_user'@'%';
FLUSH PRIVILEGES;