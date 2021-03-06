CREATE TABLE Admin(
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    token VARCHAR(255),
    token_created DATETIME,
    CONSTRAINT PK_id PRIMARY KEY(id)
);

CREATE TABLE User(
    id INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    token VARCHAR(255),
    CONSTRAINT PK_id PRIMARY KEY(id)
);

CREATE TABLE Resource(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT PK_id PRIMARY KEY(id)
);

CREATE TABLE Appointment(
    id INT NOT NULL AUTO_INCREMENT,
    resourceId INT NOT NULL,
    userId INT,
    appointmentDate DATE NOT NULL,
    message VARCHAR(255),
    status ENUM('OPEN','CLOSED') NOT NULL,
    CONSTRAINT PK_id PRIMARY KEY(id),
    CONSTRAINT FK_resourceId FOREIGN KEY (resourceId) REFERENCES Resource(id),
    CONSTRAINT FK_userId FOREIGN KEY (userId) REFERENCES User(id)
);

INSERT INTO Admin (username, password) VALUES ("admin", "21232f297a57a5a743894a0e4a801fc3");

INSERT INTO User (email, token) VALUES ("user1@gmail.com", "token1");
INSERT INTO User (email, token) VALUES ("user2@gmail.com", "token2");
INSERT INTO User (email, token) VALUES ("user3@gmail.com", "token3");

INSERT INTO Resource (name) VALUES ("Dentist1");
INSERT INTO Resource (name) VALUES ("Dentist2");
