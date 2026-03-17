--STATUS
-- 1 nowo dodany / edytownay
-- 2 zgloszony
-- 3 zatwierdzony
-- 4 zawiesozny



-- 1. Dodaj 3 użytkowników
INSERT INTO users (username, passwordHash) VALUES
   ('admin', '1234'),
   ('test_user', '1234'),
   ('tester', 'testpass');

-- 2. Dodaj 5 tagów
INSERT INTO tags (name) VALUES
    ('Super'),
    ('Żydzi'),
    ('Suchar'),
    ('Hindusi'),
    ('Rudzi');

-- 3. Dodaj 2 kawały dla użytkownika testowego (test_user - id: 2)
INSERT INTO jokes (title, content, user_id, status) VALUES
    ('Suchar', 'Dlaczego programiści nie lubią natury? Bo ma za dużo bugów.', 1, 1),
    ('Student PWR', 'Jak poznać studenta PWR? Sam ci o tym powie w ciągu pierwszych 5 minut.', 1, 1);

-- 4. Powiąż kawały z tagami (Relacja M:N)
-- Kawał 1 (id: 1) ma tagi: Suchar (3), Programowanie (4)
-- Kawał 2 (id: 2) ma tagi: Politechnika (1), Sesja (2)
INSERT INTO joke_tags (joke_id, tag_id) VALUES
    (1, 3), (1, 4),
    (2, 1), (2, 2);

-- 5. Dodaj 5 komentarzy powiązanych z tymi dwoma kawałami
INSERT INTO comments (text, rating, joke_id, user_id) VALUES
  ('Dobre, ale stare.', 4, 1, 2),
  ('U mnie działa.', 5, 1, 3),
  ('Nieśmieszne.', 2, 1, 1),
  ('Prawda objawiona.', 5, 2, 2),
  ('Znam lepsze o dziekanie.', 3, 2, 3);