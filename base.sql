create database biblio;
\c biblio;


CREATE TABLE Utilisateur (
    id_U SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Table Livre
CREATE TABLE Livre (
    id_livre SERIAL PRIMARY KEY,
    nom_livre VARCHAR(100) NOT NULL,
    auteur VARCHAR(100) NOT NULL,
    restreint BOOLEAN DEFAULT FALSE
);

-- Table Exemplaire
CREATE TABLE Exemplaire (
    id_exemplaire SERIAL PRIMARY KEY,
    id_livre INTEGER NOT NULL REFERENCES Livre(id_livre) ON DELETE CASCADE,
    disponible BOOLEAN DEFAULT TRUE
);

-- Table Profil
CREATE TABLE Profil (
    id_profil SERIAL PRIMARY KEY,
    profil VARCHAR(50) NOT NULL,
    quota INTEGER NOT NULL,
    nbr_jour_pret_penalite INTEGER NOT NULL
);


CREATE TABLE Adherent (
    id_adherent SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mdp VARCHAR(255) NOT NULL,  -- Champ mot de passe ajouté
    telephone VARCHAR(20),
    date_naissance DATE NOT NULL,
    adresse TEXT,
    id_profil INTEGER NOT NULL REFERENCES Profil(id_profil),
    actif BOOLEAN DEFAULT FALSE,
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Table Penalisation
CREATE TABLE Penalisation (
    id_penalisation SERIAL PRIMARY KEY,
    id_adherent INTEGER NOT NULL REFERENCES Adherent(id_adherent) ON DELETE CASCADE,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    etat VARCHAR(20) CHECK (etat IN ('en_cours', 'termine')) DEFAULT 'en_cours'
);

-- Table Abonnement
CREATE TABLE Abonnement (
    id_abonnement SERIAL PRIMARY KEY,
    id_adherent INTEGER NOT NULL REFERENCES Adherent(id_adherent) ON DELETE CASCADE,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL
);
ALTER TABLE abonnement ADD COLUMN actif BOOLEAN;

-- Table Reservation
CREATE TABLE Reservation (
    id_reservation SERIAL PRIMARY KEY,
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_adherent INTEGER NOT NULL REFERENCES Adherent(id_adherent) ON DELETE CASCADE,
    id_exemplaire INTEGER NOT NULL REFERENCES Exemplaire(id_exemplaire) ON DELETE CASCADE,
    etat VARCHAR(20) CHECK (etat IN ('en_cours', 'valide', 'annule')) DEFAULT 'en_cours'
);

-- Table Pret
CREATE TABLE Pret (
    id_pret SERIAL PRIMARY KEY,
    id_adherent INTEGER NOT NULL REFERENCES Adherent(id_adherent) ON DELETE CASCADE,
    id_exemplaire INTEGER NOT NULL REFERENCES Exemplaire(id_exemplaire) ON DELETE CASCADE,
    date_debut TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,
    type_pret VARCHAR(20) CHECK (type_pret IN ('sur_place', 'emporte')),
    statut_pret VARCHAR(20) CHECK (statut_pret IN ('en_demande', 'en_cours', 'retourne', 'retard')) DEFAULT 'en_demande',
    etat_traitement VARCHAR(20) CHECK (etat_traitement IN ('en_attente', 'valide', 'rejete', 'annule')) DEFAULT 'en_attente'
);

-- Table Prolongement
CREATE TABLE Prolongement (
    id_prolong SERIAL PRIMARY KEY,
    id_pret INTEGER NOT NULL REFERENCES Pret(id_pret) ON DELETE CASCADE,
    date_debut DATE NOT NULL,
    date_retour_prevue DATE NOT NULL,
    date_retour_effective DATE,
    etat_traitement VARCHAR(20) CHECK (etat_traitement IN ('en_attente', 'valide', 'rejete', 'annule')) DEFAULT 'en_attente'
);

-- Table JourFerie
CREATE TABLE JourFerie (
    id_ferie SERIAL PRIMARY KEY,
    date DATE NOT NULL UNIQUE,
    description VARCHAR(100) NOT NULL
);

-- Index pour améliorer les performances
CREATE INDEX idx_exemplaire_livre ON Exemplaire(id_livre);
CREATE INDEX idx_adherent_profil ON Adherent(id_profil);
CREATE INDEX idx_pret_adherent ON Pret(id_adherent);
CREATE INDEX idx_pret_exemplaire ON Pret(id_exemplaire);
CREATE INDEX idx_reservation_adherent ON Reservation(id_adherent);
CREATE INDEX idx_reservation_exemplaire ON Reservation(id_exemplaire);


ALTER TABLE abonnement DROP COLUMN IF EXISTS actif;

ALTER TABLE abonnement ADD COLUMN actif BOOLEAN NOT NULL DEFAULT true;

UPDATE abonnement SET actif = (date_fin >= CURRENT_DATE);