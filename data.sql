INSERT INTO Utilisateur (username, password) 
VALUES ('adherent', '1');

-- Insertion de l'utilisateur 'bibliothecaire' avec mot de passe 'bib456' hashé
INSERT INTO Utilisateur (username, password) 
VALUES ('bibliothecaire', '2');


INSERT INTO Profil (profil, quota, nbr_jour_pret_penalite) VALUES
('étudiant', 3, 5),
('professionnel', 5, 10),
('professeur', 7, 15);


INSERT INTO Adherent (
    nom,
    prenom,
    email,
    mdp,
    telephone,
    date_naissance,
    adresse,
    id_profil,
    actif
) VALUES (
    'Randria',
    'Jean',
    'jean.randria@example.com',
    'ad1',
    '+261341234567',
    '1990-05-15',
    'Lot 123 ABC, Antananarivo',
    1,        -- Assure-toi que le profil avec id_profil = 1 existe
    TRUE
);

INSERT INTO Adherent (
    nom,
    prenom,
    email,
    mdp,
    telephone,
    date_naissance,
    adresse,
    id_profil,
    actif
) VALUES (
    'Rakoto',
    'Marie',
    'marie.rakoto@example.com',
    'ad2',
    '+261339876543',
    '1985-11-20',
    '123 Rue des Fleurs, Antsirabe',
    2,        -- Assure-toi que le profil avec id_profil = 2 existe
    TRUE
);

