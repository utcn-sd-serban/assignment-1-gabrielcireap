CREATE SCHEMA IF NOT EXISTS assignment;
USE assignment;

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT,
    username VARCHAR(45) NOT NULL,
    password VARCHAR(45) NOT NULL,
    email VARCHAR(45),
    score INTEGER,
    is_admin BOOLEAN,
    is_banned BOOLEAN,

    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS question (
    id INT AUTO_INCREMENT,
    user_id INT,
    title VARCHAR(45),
    text VARCHAR(500),
    creation_date DATETIME,
    vote_count INT,

    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS answer (
    id INT AUTO_INCREMENT,
    question_id INT NOT NULL,
    user_id INT,
    text VARCHAR(500),
    creation_date DATETIME,
    vote_count INT,

    PRIMARY KEY(id),
    FOREIGN KEY(question_id) REFERENCES question(id) ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS vote (
    id INT AUTO_INCREMENT,
    question_id INT NULL,
    answer_id INT NULL,
    user_id INT,
    is_upvote BOOLEAN,

    PRIMARY KEY(id),
    FOREIGN KEY(question_id) REFERENCES question(id) ON DELETE CASCADE,
    FOREIGN KEY(answer_id) REFERENCES answer(id) ON DELETE CASCADE,
    FOREIGN KEY(user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tag (
  id INT AUTO_INCREMENT,
  name VARCHAR(45),

  PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS questionToTags (
  question_id INT,
  tag_id INT,

  FOREIGN KEY(question_id) REFERENCES question(id) ON DELETE CASCADE,
  FOREIGN KEY(tag_id) REFERENCES tag(id) ON DELETE CASCADE
);