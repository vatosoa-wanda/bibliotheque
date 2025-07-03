-- Profils
INSERT INTO Profil (profil, quota, nbr_jour_pret_penalite) VALUES 
('Standard', 5, 14),
('Premium', 10, 21),
('Jeunesse', 3, 21);

-- Adhérents
INSERT INTO Adherent (nom, prenom, email, mdp, telephone, date_naissance, adresse, id_profil, actif) VALUES
('Dupont', 'Jean', 'jean', 'ad1', '0612345678', '1990-05-15', '12 Rue de Paris, 75001', 1, true),
('Martin', 'Sophie', 'sophie', 'ad2', '0698765432', '2005-11-22', '34 Avenue des Champs, 69002', 3, true),
('Durand', 'Pierre', 'pierre', 'ad3', '0634567890', '1985-03-10', '56 Boulevard Central, 13003', 2, true);


-- Abonnements
INSERT INTO Abonnement (id_adherent, date_debut, date_fin) VALUES
(1, '2023-01-01', '2024-12-31'),
(2, '2023-06-01', '2023-12-31'),
(3, '2023-03-15', '2024-03-14');

-- Livres
INSERT INTO Livre (nom_livre, auteur, restreint) VALUES
('Le Petit Prince', 'Antoine de Saint-Exupéry', false),
('1984', 'George Orwell', true),
('Harry Potter à l''école des sorciers', 'J.K. Rowling', false),
('Lolita', 'Vladimir Nabokov', true),
('Guide de voyage Japon', 'Lonely Planet', false);

-- Exemplaires
INSERT INTO Exemplaire (id_livre, disponible) VALUES
(1, true), (1, true), (1, false),
(2, true), (2, false),
(3, true), (3, true), (3, true),
(4, false),
(5, true), (5, true);


-- Adhérent valide, livre disponible, pas de sanction
INSERT INTO Pret (id_adherent, id_exemplaire, date_debut, date_retour_prevue, type_pret, statut_pret, etat_traitement)
VALUES (1, 1, '2023-10-01', '2023-10-15', 'emporte', 'en_cours', 'valide');

UPDATE Exemplaire SET disponible = false WHERE id_exemplaire = 1;


-- Adhérent mineur (Sophie Martin, née en 2005) essaie d'emprunter un livre restreint
-- Doit échouer lors de la tentative de prêt


-- Création de plusieurs prêts pour l'adhérent 1 (quota = 5)
INSERT INTO Pret (id_adherent, id_exemplaire, date_debut, date_retour_prevue, type_pret, statut_pret, etat_traitement) VALUES
(1, 4, '2023-10-01', '2023-10-15', 'emporte', 'en_cours', 'valide'),
(1, 6, '2023-10-02', '2023-10-16', 'emporte', 'en_cours', 'valide'),
(1, 7, '2023-10-03', '2023-10-17', 'sur_place', 'en_cours', 'valide'),
(1, 10, '2023-10-04', '2023-10-18', 'emporte', 'en_cours', 'valide');
-- Le 5ème prêt atteint le quota (ne peut pas en créer un 6ème)



-- Ajout d'une sanction pour l'adhérent 3
INSERT INTO Penalisation (id_adherent, date_debut, date_fin, etat)
VALUES (3, '2023-09-15', '2023-12-15', 'en_cours');

-- Tentative de prêt doit échouer


-- Modification de l'abonnement de l'adhérent 2 pour qu'il soit expiré
UPDATE Abonnement SET date_fin = '2023-09-30' WHERE id_adherent = 2;

-- Tentative de prêt doit échouer