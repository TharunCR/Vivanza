CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    gender VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    mobile VARCHAR(20) NOT NULL,
    creation_date DATE NOT NULL,
    updated_date DATE NOT NULL,
    events_of_interest VARCHAR(255),
    role enum('USER','ADMIN') NOT NULL);

INSERT INTO users (name, password, gender, address, email, mobile, creation_date, updated_date, events_of_interest, role)
VALUES
('Tony Stark', '$2a$10$wQAvKT.23CMK9qMqJWEaxuWAO2WuHboZaS8okIbMw9c58oAnTtNFe', 'Male', '10880 Malibu Point, Malibu', 'tony.stark@example.com', '1112223333', '2024-05-03', '2024-05-03', 'Technology, Engineering', 'USER'),
('Ellen Ripley', '$2a$10$ZtFqAL6kM.qeflA7YHA.POx2bMZ7lNpQdl9KzmyRL.ClQD74G8EbS', 'Female', 'LV-426 Colony', 'ellen.ripley@example.com', '4445556666', '2024-05-03', '2024-05-03', 'Science Fiction, Survival', 'USER'),
('Indiana Jones', '$2a$10$3io8kQRNSQDdgbbIU0BWj.iufPXXEgms22gXmNaOI5C3IP3IJ7OVO', 'Male', 'Temple of Doom, India', 'indiana.jones@example.com', '7778889999', '2024-05-03', '2024-05-03', 'Adventure, History', 'USER');
