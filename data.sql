-- Insertion profils
INSERT INTO Profil (profil, quota, nbr_jour_pret_penalite, quota_reservation, quota_prolongement) VALUES
('Etudiant', 3, 10, 3, 3),
('Professionel', 4, 15, 4, 4),
('Professeur', 5, 20, 5, 5);

-- Insertion utilisateur (admin)
INSERT INTO Utilisateur (username, password) VALUES
('admin', 'admin');  -- À chiffrer dans ton backend

-- Insertion d'adhérents
INSERT INTO Adherent (nom, prenom, email, mdp, telephone, date_naissance, adresse, id_profil, actif)
VALUES
('Rabe', 'Jean', 'jean', 'ad1', '0321123456', '2010-05-15', 'Lot 101 Tana', 1, true),
('Raso', 'Marie', 'marie', 'ad2', '0321987654', '1990-12-01', 'Lot 202 Tana', 2, true),
('Rakoto', 'Paul', 'paul', 'ad3', '0331456723', '1985-03-20', 'Lot 303 Tana', 3, true);

-- Insertion de livres
INSERT INTO Livre (nom_livre, auteur, restreint) VALUES
('Introduction à Java', 'John Doe', false),
('Spring Boot Avancé', 'Jane Smith', false),
('Psychologie Interdite', 'Sigmund Freud', true);

-- Insertion d'exemplaires
INSERT INTO Exemplaire (id_livre, disponible) VALUES
(1, true), (1, true), -- 2 exemplaires du premier livre
(2, true),
(3, true);

-- Insertion d'abonnements (valide jusqu'à fin 2025)
INSERT INTO Abonnement (id_adherent, date_debut, date_fin)
VALUES
(1, '2025-01-01', '2025-12-31'),
(2, '2025-01-01', '2025-12-31'),
(3, '2025-01-01', '2025-03-31'); -- paul non abonne a partir de avril

-- Mise à jour du champ actif selon la date actuelle
UPDATE Abonnement SET actif = (date_fin >= CURRENT_DATE);

-- test pret a prolonger pour adapter la date de retour car ferie
INSERT INTO Pret (
    id_adherent,
    id_exemplaire,
    date_debut,
    date_retour_prevue,
    type_pret,
    statut_pret,
    etat_traitement
) VALUES (
    1,                -- Jean Rabe
    1,                -- Exemplaire de "Introduction à Java"
    '2025-07-26 10:00:00',
    '2025-08-05',     -- Date de retour initiale
    'EMPORTE',
    'EN_COURS',
    'VALIDE'
);

INSERT INTO Penalisation (id_adherent, date_debut, date_fin, etat)
VALUES (2, '2025-07-10', '2023-07-18', 'EN_COURS');