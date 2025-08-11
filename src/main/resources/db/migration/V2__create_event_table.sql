CREATE TABLE event (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(255) NOT NULL,
    creation_date DATE NOT NULL,
    updated_date DATE NOT NULL,
    event_start_date_and_time DATETIME NOT NULL,
    event_end_date_and_time DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);

INSERT INTO event (name, category, description, location, creation_date, updated_date, event_start_date_and_time, event_end_date_and_time, user_id)
VALUES
('Mumbai Music Concert', 'Concert', 'Live performances by top Indian and international artists.', 'Wankhede Stadium, Mumbai', '2024-05-03', '2024-05-03', '2024-05-03 18:00:00', '2024-05-03 23:00:00', 1),
('Stand-up Comedy Night', 'Comedy Show', 'An evening full of laughter with popular comedians.', 'NCPA Auditorium, Mumbai', '2024-05-03', '2024-05-03', '2024-05-04 19:00:00', '2024-05-04 22:00:00', 1),
('Annual Business Summit', 'Summit', 'Leaders from various industries discuss market trends.', 'Taj Mahal Palace, Mumbai', '2024-05-03', '2024-05-03', '2024-05-05 09:00:00', '2024-05-05 17:00:00', 1),
('Chennai Arts Festival', 'Festival', 'Celebrating traditional and contemporary arts.', 'Music Academy, Chennai', '2024-05-03', '2024-05-03', '2024-05-06 10:00:00', '2024-05-06 20:00:00', 1),
('Bangalore Tech Conference', 'Technology', 'Showcasing latest innovations and startups.', 'Bangalore International Exhibition Centre', '2024-05-03', '2024-05-03', '2024-05-07 09:00:00', '2024-05-07 17:00:00', 2),
('Kolkata Food Expo', 'Exhibition', 'A gastronomic journey featuring diverse cuisines.', 'Pragati Maidan, Kolkata', '2024-05-03', '2024-05-03', '2024-05-08 11:00:00', '2024-05-08 20:00:00', 2),
('Pune Startup Meetup', 'Meetup', 'Networking event for local entrepreneurs and investors.', 'Hyatt Regency, Pune', '2024-05-03', '2024-05-03', '2024-05-09 16:00:00', '2024-05-09 20:00:00', 2),
('Delhi Literature Festival', 'Festival', 'Celebrating Indian and global literary talents.', 'India Habitat Centre, New Delhi', '2024-05-03', '2024-05-03', '2024-05-10 10:00:00', '2024-05-10 18:00:00', 2),
('Hyderabad Film Awards', 'Awards Ceremony', 'Honoring the best in Indian cinema.', 'Novotel Hyderabad Convention Centre', '2024-05-03', '2024-05-03', '2024-05-11 19:00:00', '2024-05-11 23:00:00', 2),
('Goa Beach Festival', 'Festival', 'Music, dance, and food festival by the sea.', 'Baga Beach, Goa', '2024-05-03', '2024-05-03', '2024-05-12 15:00:00', '2024-05-12 22:00:00', 2);
