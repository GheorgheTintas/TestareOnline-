create schema testareonline;
USE testareonline;
CREATE TABLE User (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);
CREATE TABLE Quiz (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_user_owner BIGINT NOT NULL,
    nume VARCHAR(255) NOT NULL,
    data_creare DATETIME NOT NULL,
    FOREIGN KEY (id_user_owner) REFERENCES User(id) ON DELETE CASCADE
);
 
CREATE TABLE Intrebare (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_quiz BIGINT NOT NULL,
    descriere_intrebare VARCHAR(255) NOT NULL,
    varianta_raspuns_1 VARCHAR(255),
    varianta_raspuns_2 VARCHAR(255),
    varianta_raspuns_3 VARCHAR(255),
    varianta_raspuns_4 VARCHAR(255),
    varianta_raspuns_corecta VARCHAR(255),
    FOREIGN KEY (id_quiz) REFERENCES Quiz(id) ON DELETE CASCADE
);
 
CREATE TABLE User_Quiz (
    id_quiz BIGINT NOT NULL,
    id_user_participant BIGINT NOT NULL,
    punctaj INT default -1,
    PRIMARY KEY (id_quiz, id_user_participant),
    FOREIGN KEY (id_quiz) REFERENCES Quiz(id) ON DELETE CASCADE,
    FOREIGN KEY (id_user_participant) REFERENCES User(id) ON DELETE CASCADE
);
 
CREATE TABLE Raspuns_Intrebare_User (
    id_user BIGINT NOT NULL,
    id_intrebare BIGINT NOT NULL,
    raspuns_dat VARCHAR(255),
    PRIMARY KEY (id_user, id_intrebare),
    FOREIGN KEY (id_user) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (id_intrebare) REFERENCES Intrebare(id) ON DELETE CASCADE
);
-- Populate User table
INSERT INTO `User` (username, password_hash) VALUES 
('john_doe', 'hashed_password_123'),
('jane_smith', 'hashed_password_456'),
('alice_wonder', 'hashed_password_789');
 
-- Populate Quiz table
INSERT INTO Quiz (id_user_owner, nume, data_creare) VALUES 
(1, 'General Knowledge Quiz', '2024-12-01 10:00:00'),
(2, 'Science Trivia', '2024-12-02 15:00:00');
 
-- Populate Intrebare table
INSERT INTO Intrebare (id_quiz, descriere_intrebare, varianta_raspuns_1, varianta_raspuns_2, varianta_raspuns_3, varianta_raspuns_4, varianta_raspuns_corecta) VALUES 
(1, 'What is the capital of France?', 'Paris', 'London', 'Berlin', 'Madrid', 'Paris'),
(1, 'Which planet is known as the Red Planet?', 'Earth', 'Mars', 'Venus', 'Jupiter', 'Mars'),
(2, 'What is the chemical symbol for water?', 'O2', 'H2O', 'CO2', 'N2', 'H2O'),
(2, 'What gas do plants absorb from/ the atmosphere?', 'Oxygen', 'Carbon Dioxide', 'Nitrogen', 'Helium', 'Carbon Dioxide');
 
-- Populate User_Quiz table
INSERT INTO User_Quiz (id_quiz, id_user_participant, punctaj) VALUES 
(1, 3, 20), -- Alice participated in General Knowledge Quiz
(2, 1, 15); -- John participated in Science Trivia
 
-- Populate Raspuns_Intrebare_User table
INSERT INTO Raspuns_Intrebare_User (id_user, id_intrebare, raspuns_dat) VALUES 
(3, 1, 'Paris'), -- Alice answered correctly for question 1
(3, 2, 'Mars'),  -- Alice answered correctly for question 2
(1, 3, 'H2O'),  -- John answered correctly for question 3
(1, 4, 'Carbon Dioxide'); -- John answered correctly for question 4

INSERT INTO `User` (username, password_hash) VALUES 
('test', '$2a$10$OLX4PWkR5uwxkJkhedD28.JlLeadKwuwu/6JGLv9pY/Wd9qkKUYWq');
DROP table spring_session;
Select * from user;
truncate table spring_session_attributes;
truncate table spring_session;
INSERT INTO Quiz (id_user_owner, nume, data_creare) VALUES 
(4, 'Quiz', '2024-12-01 20:00:00');
SELECT * FROM user;
SELECT * FROM quiz;
SELECT * FROM user_quiz;
SELECT * FROM intrebare;
SELECT * FROM raspuns_intrebare_user;
INSERT INTO User_Quiz (id_quiz, id_user_participant, punctaj) VALUES (4, 3, 10);
INSERT INTO Raspuns_Intrebare_User (id_user, id_intrebare, raspuns_dat) VALUES (4, 1, 'Paris'),(4, 2, 'Mars'),(4, 3, 'H2O');
INSERT INTO Intrebare (id_quiz, descriere_intrebare, varianta_raspuns_1, varianta_raspuns_2, varianta_raspuns_3, varianta_raspuns_4, varianta_raspuns_corecta) VALUES 
(4, 'What is the capital of France?', 'Paris', 'London', 'Berlin', 'Madrid', 'Paris'),
(4, 'Which planet is known as the Red Planet?', 'Earth', 'Mars', 'Venus', 'Jupiter', 'Mars'),
(4, 'What is the chemical symbol for water?', 'O2', 'H2O', 'CO2', 'N2', 'H2O'),
(4, 'What gas do plants absorb from/ the atmosphere?', 'Oxygen', 'Carbon Dioxide', 'Nitrogen', 'Helium', 'Carbon Dioxide');