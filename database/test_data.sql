-- ========================================
-- Media Ratings Platform - Sample Test Data
-- ========================================
-- This script populates the database with sample data for testing and demonstration.
-- All passwords are 'password123' for easy testing.
-- Password hash generated using BCrypt with cost factor 12

-- Clean existing data (if any)
TRUNCATE TABLE rating_likes, ratings, favorites, media_entries, users RESTART IDENTITY CASCADE;

-- ========================================
-- USERS
-- ========================================
-- Note: All passwords are 'password123'
-- The hash below is: $2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLaEg3jO
-- Tokens follow format: username-{UUID}-mrpToken

-- Temporarily allow manual ID insertion for users table
ALTER TABLE users ALTER COLUMN id DROP IDENTITY IF EXISTS;

INSERT INTO users (id, username, password_hash, token, favoritegenre) VALUES
(1, 'john_doe', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLaEg3jO', 'john_doe-550e8400-e29b-41d4-a716-446655440001-mrpToken', 'sci-fi'),
(2, 'jane_smith', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLaEg3jO', 'jane_smith-550e8400-e29b-41d4-a716-446655440002-mrpToken', 'drama'),
(3, 'bob_wilson', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLaEg3jO', 'bob_wilson-550e8400-e29b-41d4-a716-446655440003-mrpToken', 'fantasy'),
(4, 'alice_brown', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLaEg3jO', 'alice_brown-550e8400-e29b-41d4-a716-446655440004-mrpToken', 'horror'),
(5, 'charlie_davis', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzpLaEg3jO', 'charlie_davis-550e8400-e29b-41d4-a716-446655440005-mrpToken', 'action');

-- Restore identity column and update sequence
ALTER TABLE users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (START WITH 6);
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- ========================================
-- MEDIA ENTRIES
-- ========================================

INSERT INTO media_entries (id, title, description, media_type, release_year, genre, age_restriction, creator_id) VALUES
-- Movies
(1, 'The Matrix', 'A computer hacker learns about the true nature of reality', 'movie', 1999, ARRAY['sci-fi', 'action'], 16, 1),
(2, 'Inception', 'A thief who steals corporate secrets through dream-sharing technology', 'movie', 2010, ARRAY['sci-fi', 'thriller'], 13, 1),
(3, 'The Shawshank Redemption', 'Two imprisoned men bond over a number of years', 'movie', 1994, ARRAY['drama'], 16, 2),
(4, 'Pulp Fiction', 'The lives of two mob hitmen, a boxer, and a pair of diner bandits intertwine', 'movie', 1994, ARRAY['drama', 'thriller'], 18, 2),
(5, 'The Dark Knight', 'Batman must accept one of the greatest psychological tests', 'movie', 2008, ARRAY['action', 'thriller'], 13, 5),
(6, 'Interstellar', 'A team of explorers travel through a wormhole in space', 'movie', 2014, ARRAY['sci-fi', 'drama'], 13, 1),
(7, 'The Lord of the Rings: The Fellowship of the Ring', 'A meek Hobbit embarks on a journey to destroy a powerful ring', 'movie', 2001, ARRAY['fantasy', 'action'], 13, 3),
(8, 'Parasite', 'Greed and class discrimination threaten the newly formed symbiotic relationship', 'movie', 2019, ARRAY['drama', 'thriller'], 16, 2),

-- Series
(9, 'Breaking Bad', 'A high school chemistry teacher turned methamphetamine producer', 'series', 2008, ARRAY['drama', 'thriller'], 18, 2),
(10, 'Game of Thrones', 'Nine noble families fight for control over the lands of Westeros', 'series', 2011, ARRAY['fantasy', 'drama'], 18, 3),
(11, 'Stranger Things', 'A young boy disappears and supernatural forces emerge', 'series', 2016, ARRAY['horror', 'sci-fi'], 16, 4),
(12, 'The Witcher', 'Geralt of Rivia, a solitary monster hunter, struggles to find his place', 'series', 2019, ARRAY['fantasy', 'action'], 16, 3),
(13, 'Black Mirror', 'An anthology series exploring a twisted, high-tech multiverse', 'series', 2011, ARRAY['sci-fi', 'thriller'], 16, 1),

-- Games
(14, 'The Witcher 3: Wild Hunt', 'An open-world RPG set in a visually stunning fantasy universe', 'game', 2015, ARRAY['fantasy', 'action'], 18, 3),
(15, 'The Last of Us', 'A brutal journey through a pandemic-ravaged America', 'game', 2013, ARRAY['action', 'horror'], 18, 4),
(16, 'Red Dead Redemption 2', 'An epic tale of life in Americas unforgiving heartland', 'game', 2018, ARRAY['action', 'drama'], 18, 5),
(17, 'Half-Life: Alyx', 'A VR return to the Half-Life series', 'game', 2020, ARRAY['sci-fi', 'action'], 16, 1),
(18, 'Dark Souls III', 'A challenging action RPG with dark fantasy elements', 'game', 2016, ARRAY['fantasy', 'action'], 16, 3);

-- Update media_entries sequence
SELECT setval('media_entries_id_seq', (SELECT MAX(id) FROM media_entries));

-- ========================================
-- RATINGS
-- ========================================

INSERT INTO ratings (media_id, user_id, stars, comment, comment_approved) VALUES
-- User 1 (john_doe) - Sci-Fi enthusiast
(1, 1, 5, 'Revolutionary film that changed sci-fi forever!', true),
(2, 1, 5, 'Mind-bending masterpiece', true),
(6, 1, 5, 'Nolan at his best', true),
(13, 1, 4, 'Thought-provoking episodes', true),
(17, 1, 5, 'Best VR experience ever', true),

-- User 2 (jane_smith) - Drama lover
(3, 2, 5, 'One of the greatest films of all time', true),
(4, 2, 5, 'Tarantino genius', true),
(8, 2, 5, 'Stunning social commentary', true),
(9, 2, 5, 'Best TV series ever made', true),
(1, 2, 4, 'Great action sequences', true),
(16, 2, 4, 'Beautiful storytelling', false),  -- Unapproved comment

-- User 3 (bob_wilson) - Fantasy fan
(7, 3, 5, 'Epic fantasy at its finest', true),
(10, 3, 4, 'Amazing world-building despite the ending', true),
(12, 3, 4, 'Toss a coin to your Witcher!', true),
(14, 3, 5, 'Best RPG I have ever played', true),
(18, 3, 5, 'Challenging but rewarding', true),

-- User 4 (alice_brown) - Horror/Thriller enthusiast
(11, 4, 5, 'Nostalgic and terrifying', true),
(15, 4, 5, 'Emotional rollercoaster', true),
(4, 4, 4, 'Intense and gripping', true),
(5, 4, 4, 'Heath Ledger was phenomenal', true),
(9, 4, 3, 'Good but overrated', false),  -- Unapproved comment

-- User 5 (charlie_davis) - Action movies
(5, 5, 5, 'Best Batman movie ever!', true),
(1, 5, 5, 'Timeless classic', true),
(16, 5, 5, 'Masterpiece of gaming', true),
(7, 5, 4, 'Epic adventure', true),
(2, 5, 4, 'Complex but amazing', true),

-- Cross-pollination for better recommendations
(6, 2, 4, 'Beautiful visuals', true),
(13, 4, 5, 'Each episode is a movie', true),
(14, 5, 4, 'Great story', true),
(10, 2, 3, 'Started strong but lost me', true),
(17, 3, 3, 'Innovative but needs better content', false);  -- Unapproved comment

-- ========================================
-- RATING LIKES
-- ========================================

INSERT INTO rating_likes (rating_id, user_id) VALUES
-- Users liking each other's reviews
(1, 2),  -- jane likes john's Matrix review
(1, 5),  -- charlie likes john's Matrix review
(9, 1),  -- john likes jane's Breaking Bad review
(9, 4),  -- alice likes jane's Breaking Bad review
(13, 2), -- jane likes bob's LOTR review
(13, 5), -- charlie likes bob's LOTR review
(17, 3), -- bob likes alice's Stranger Things review
(21, 1), -- john likes charlie's Batman review
(21, 2), -- jane likes charlie's Batman review
(4, 5),  -- charlie likes john's Black Mirror review
(8, 3),  -- bob likes jane's Parasite review
(16, 4), -- alice likes bob's Dark Souls review
(20, 2), -- jane likes alice's The Dark Knight review
(6, 4);  -- alice likes john's Interstellar review

-- ========================================
-- FAVORITES
-- ========================================

INSERT INTO favorites (user_id, media_id) VALUES
-- User 1 favorites (Sci-Fi fan)
(1, 1),  -- The Matrix
(1, 2),  -- Inception
(1, 13), -- Black Mirror

-- User 2 favorites (Drama lover)
(2, 3),  -- Shawshank Redemption
(2, 9),  -- Breaking Bad
(2, 8),  -- Parasite

-- User 3 favorites (Fantasy fan)
(3, 7),  -- LOTR
(3, 14), -- The Witcher 3
(3, 10), -- Game of Thrones

-- User 4 favorites (Horror/Thriller)
(4, 11), -- Stranger Things
(4, 15), -- The Last of Us
(4, 5),  -- The Dark Knight

-- User 5 favorites (Action)
(5, 5),  -- The Dark Knight
(5, 16), -- Red Dead Redemption 2
(5, 1);  -- The Matrix

-- ========================================
-- SUMMARY
-- ========================================
-- Users: 5 (all with password 'password123')
-- Media: 18 (6 movies, 5 series, 7 games)
-- Ratings: 30 (mix of approved and unapproved comments)
-- Rating Likes: 14
-- Favorites: 15
--
-- Test accounts (username / password / token):
-- john_doe / password123 / john_doe-550e8400-e29b-41d4-a716-446655440001-mrpToken
-- jane_smith / password123 / jane_smith-550e8400-e29b-41d4-a716-446655440002-mrpToken
-- bob_wilson / password123 / bob_wilson-550e8400-e29b-41d4-a716-446655440003-mrpToken
-- alice_brown / password123 / alice_brown-550e8400-e29b-41d4-a716-446655440004-mrpToken
-- charlie_davis / password123 / charlie_davis-550e8400-e29b-41d4-a716-446655440005-mrpToken
