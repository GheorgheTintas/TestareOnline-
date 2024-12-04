USE TestareOnline;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);
INSERT INTO users (username, password_hash) 
VALUES ('user', 'test');
SELECT * FROM USERS;
UPDATE users
SET password_hash ='$2a$10$OLX4PWkR5uwxkJkhedD28.JlLeadKwuwu/6JGLv9pY/Wd9qkKUYWq'
WHERE username = 'user';


