INSERT INTO Utilisateur (username, password) VALUES
('admin', 'admin');  -- À chiffrer dans ton backend


-- Insertion de livres
INSERT INTO Livre (nom_livre, auteur, restreint) VALUES
('Les Miserables', 'Victor Hugo', true),
('L Etranger', 'Albert Camus', true),
('Harry Potter a l ecole des sorciers', 'J.K. Rowling', true);

INSERT INTO Exemplaire (id_livre, disponible) VALUES
(1, true), (1, true), (1, true),
(2, true), (2, true),
(3, true);


-- Insertion profils
INSERT INTO Profil (profil, quota, nbr_jour_pret,  nbr_jour_pret_penalite, quota_reservation, quota_prolongement) VALUES
('Etudiant', 2, 7, 10, 1, 3),
('Enseignant', 3, 9, 9, 2, 5),
('Professionnel', 4, 12, 8, 3, 7);

-- Jours fériés 2025 Madagascar
INSERT INTO JourFerie (date, description) VALUES
  ('2025-07-26', 'Ferie 1'),
  ('2025-07-19', 'Ferie 2');

-- dimanche
INSERT INTO JourFerie (date, description) VALUES
  ('2025-07-13', 'dimanche 1'),
  ('2025-07-20', 'dimanche 2'),
  ('2025-07-27', 'dimanche 3'),
  ('2025-08-03', 'dimanche 4'),
  ('2025-08-10', 'dimanche 5'),
  ('2025-08-17', 'dimanche 6');

  
INSERT INTO Adherent (nom, prenom, email, mdp, telephone, date_naissance, adresse, id_profil, actif)
VALUES
('Amine Bensaid', 'Amine Bensaid', 'ETU001', 'ETU001', '0321123456', '2010-05-15', 'Lot 101 Tana', 1, true),
('Sarah El Khattabi', 'Sarah El Khattabi', 'ETU002', 'ETU002', '0321987654', '1990-12-01', 'Lot 202 Tana', 1, true),
('Youssef Moujahid', 'Youssef Moujahid', 'ETU003', 'ETU003', '0331456723', '1985-03-20', 'Lot 303 Tana', 1, true),
('Nadia Benali', 'Nadia Benali', 'ENS001', 'ENS001', '0331456723', '1985-03-20', 'Lot 303 Tana', 2, true),
('Karim Haddadi', 'Karim Haddadi', 'ENS002', 'ENS002', '0331456723', '1985-03-20', 'Lot 303 Tana', 2, true),
('Salima Touhami', 'Salima Touhami', 'ENS003', 'ENS003', '0331456723', '1985-03-20', 'Lot 303 Tana', 2, true),
('Rachid El Mansouri', 'Rachid El Mansouri', 'PROF001', 'PROF001', '0331456723', '1985-03-20', 'Lot 303 Tana', 3, true),
('Amina Zerouali', 'Amina Zerouali', 'PROF002', 'PROF002', '0331456723', '1985-03-20', 'Lot 303 Tana', 3, true);

INSERT INTO Abonnement (id_adherent, date_debut, date_fin, actif)
VALUES
(1, '2025-02-01', '2025-07-24', true),
(2, '2025-02-01', '2025-07-01', false),
(3, '2025-04-01', '2025-12-01', true), 
(4, '2025-07-01', '2026-07-01', true), 
(5, '2025-08-01', '2026-05-01', false), 
(6, '2025-07-01', '2026-06-01', true), 
(7, '2025-06-01', '2025-12-01', true), 
(8, '2024-10-01', '2025-06-01', false);



