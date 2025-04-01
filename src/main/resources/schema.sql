-- This SQL script creates the necessary tables for the application.

-- Menu Table:
CREATE TABLE IF NOT EXISTS menu (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  option VARCHAR(255),
  note VARCHAR(255)
);