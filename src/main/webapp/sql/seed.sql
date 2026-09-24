-- ============================================
-- SHAKTHI MART - SEED DATA
-- ============================================

-- ============================================
-- USERS
-- ============================================

INSERT INTO users (name, email, password, role)
SELECT 'Arun', 'arun@shakthimart.com', '$2a$10$examplepasswordhash', 'SELLER'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'arun@shakthimart.com'
);

INSERT INTO users (name, email, password, role)
SELECT 'Kavin', 'kavin@shakthimart.com', '$2a$10$examplepasswordhash', 'SELLER'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'kavin@shakthimart.com'
);

INSERT INTO users (name, email, password, role)
SELECT 'Meena', 'meena@shakthimart.com', '$2a$10$examplepasswordhash', 'SELLER'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'meena@shakthimart.com'
);

INSERT INTO users (name, email, password, role)
SELECT 'Priya', 'priya@shakthimart.com', '$2a$10$examplepasswordhash', 'SELLER'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'priya@shakthimart.com'
);

INSERT INTO users (name, email, password, role)
SELECT 'Sanjay', 'sanjay@shakthimart.com', '$2a$10$examplepasswordhash', 'SELLER'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'sanjay@shakthimart.com'
);

-- ============================================
-- SERVICES
-- ============================================

-- 1. Web Development
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Web Development',
       'Modern responsive website development using HTML, CSS and JavaScript.',
       500.00,
       'Technology'
FROM users
WHERE email = 'arun@shakthimart.com';

-- 2. Java Programming
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Java Programming',
       'Java programming support including OOP, collections and basic projects.',
       400.00,
       'Technology'
FROM users
WHERE email = 'kavin@shakthimart.com';

-- 3. Graphic Design
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Graphic Design',
       'Creative posters, social media designs and simple branding materials.',
       300.00,
       'Design'
FROM users
WHERE email = 'meena@shakthimart.com';

-- 4. UI/UX Design
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'UI/UX Design',
       'Clean and user-friendly mobile and website interface design.',
       600.00,
       'Design'
FROM users
WHERE email = 'priya@shakthimart.com';

-- 5. Academic Tutoring
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Academic Tutoring',
       'Online tutoring support for school and college academic subjects.',
       200.00,
       'Education'
FROM users
WHERE email = 'sanjay@shakthimart.com';

-- 6. Python Programming
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Python Programming',
       'Python basics, problem solving and beginner programming assistance.',
       450.00,
       'Technology'
FROM users
WHERE email = 'kavin@shakthimart.com';

-- 7. Digital Marketing
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Digital Marketing',
       'Social media marketing, content planning and basic online promotion.',
       350.00,
       'Digital Marketing'
FROM users
WHERE email = 'meena@shakthimart.com';

-- 8. Content Writing
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Content Writing',
       'Simple articles, website content and social media captions.',
       250.00,
       'Writing'
FROM users
WHERE email = 'sanjay@shakthimart.com';

-- 9. Video Editing
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Video Editing',
       'Basic video editing for short videos, reels and presentations.',
       550.00,
       'Media'
FROM users
WHERE email = 'priya@shakthimart.com';

-- 10. Data Analysis
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Data Analysis',
       'Basic data cleaning, analysis and visualization using Python.',
       700.00,
       'Data Analysis'
FROM users
WHERE email = 'arun@shakthimart.com';

-- 11. Business Presentation
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Business Presentation',
       'Professional PowerPoint presentation creation for projects and business ideas.',
       300.00,
       'Business'
FROM users
WHERE email = 'meena@shakthimart.com';

-- 12. Resume Design
INSERT INTO services
(creator_id, name, description, price, category)
SELECT id,
       'Resume Design',
       'Clean and professional resume formatting for students and job seekers.',
       250.00,
       'Design'
FROM users
WHERE email = 'priya@shakthimart.com';
