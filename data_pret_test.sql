-- Profils
-- INSERT INTO Profil (profil, quota, nbr_jour_pret_penalite) VALUES 
-- ('Standard', 5, 14),
-- ('Premium', 4, 21),
-- ('Jeunesse', 3, 21);
INSERT INTO Profil (profil, quota, nbr_jour_pret_penalite, quota_reservation, quota_prolongement) VALUES 
('Standard', 5, 14, 3, 2),
('Premium', 4, 21, 5, 3),
('Jeunesse', 3, 21, 2, 1);
 


-- Adhérents
INSERT INTO Adherent (nom, prenom, email, mdp, telephone, date_naissance, adresse, id_profil, actif) VALUES
('Dupont', 'Jean', 'jean', 'ad1', '0612345678', '1990-05-15', '12 Rue de Paris, 75001', 1, true),
('Martin', 'Sophie', 'sophie', 'ad2', '0698765432', '2005-11-22', '34 Avenue des Champs, 69002', 3, true),
('Durand', 'Pierre', 'pierre', 'ad3', '0634567890', '1985-03-10', '56 Boulevard Central, 13003', 2, true);

INSERT INTO Adherent (nom, prenom, email, mdp, telephone, date_naissance, adresse, id_profil, actif) VALUES
('Dupont', 'Marie', 'marie', 'ad4', '0412345678', '2015-05-15', '12 Rue de Paris, 75001', 1, true);
-- Abonnements
INSERT INTO Abonnement (id_adherent, date_debut, date_fin) VALUES
(1, '2023-01-01', '2024-12-31'),
(2, '2023-06-01', '2023-12-31'),
(3, '2023-03-15', '2024-03-14');

-- Livres
INSERT INTO Livre (nom_livre, auteur, restreint) VALUES
('Le Petit Prince', 'Antoine de Saint-Exupery', false),
('1984', 'George Orwell', true),
('Harry Potter à l''école des sorciers', 'J.K. Rowling', false),
('Lolita', 'Vladimir Nabokov', true),
('Guide de voyage Japon', 'Lonely Planet', false);

UPDATE Livre SET restreint = true WHERE id_livre = 5;

-- Exemplaires
INSERT INTO Exemplaire (id_livre, disponible) VALUES (1, false);
INSERT INTO Exemplaire (id_livre, disponible) VALUES (4, true);
INSERT INTO Exemplaire (id_livre, disponible) VALUES (5, true);
INSERT INTO Exemplaire (id_livre, disponible) VALUES (1, false);
INSERT INTO Exemplaire (id_livre, disponible) VALUES (4, true);
INSERT INTO Exemplaire (id_livre, disponible) VALUES (5, true);

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



-- Ajout d'une sanction pour l'adhérent 3
INSERT INTO Penalisation (id_adherent, date_debut, date_fin, etat)
VALUES (3, '2023-09-15', '2023-12-15', 'EN_COURS');


UPDATE Penalisation SET etat = 'TERMINE' WHERE id_adherent = 3;
-- Tentative de prêt doit échouer


-- Modification de l'abonnement de l'adhérent 2 pour qu'il soit expiré
UPDATE Abonnement SET date_fin = '2023-09-30' WHERE id_adherent = 2;

-- Tentative de prêt doit échouer

INSERT INTO Pret (id_adherent, id_exemplaire, date_retour_prevue, type_pret, statut_pret, etat_traitement)
VALUES 
(2, 2, '2025-07-20', 'EMPORTE', 'EN_COURS', 'VALIDE'), 
(2, 5, '2025-07-22', 'SUR_PLACE', 'EN_COURS', 'VALIDE'),
(2, 6, '2025-07-25', 'EMPORTE', 'EN_COURS', 'VALIDE'),
(2, 4, '2025-07-28', 'EMPORTE', 'EN_COURS', 'VALIDE');