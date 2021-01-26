-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------
-- 

DROP TABLE Reservation;
DROP TABLE BikeModel;
-- --------------------------------- BikeModel ------------------------------------
CREATE TABLE BikeModel ( bikeModelId BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) COLLATE latin1_bin NOT NULL,
    description VARCHAR(255) COLLATE latin1_bin NOT NULL,
    rentableFrom DATETIME NOT NULL,
    totalUnits INTEGER NOT NULL,
    pricePerDay REAL NOT NULL,
    creationDate DATETIME NOT NULL,
    timesRated INTEGER NOT NULL,
    avgScore REAL NOT NULL,
    CONSTRAINT BikeModelPK PRIMARY KEY(bikeModelId),
    CONSTRAINT validTotalUnits CHECK ( totalUnits >= 0 ),
    CONSTRAINT validPricePerDay CHECK ( pricePerDay >= 0 )   
    ) ENGINE = InnoDB;


-- --------------------------------- Reservation ------------------------------------    

CREATE TABLE Reservation ( rentalId BIGINT NOT NULL AUTO_INCREMENT,
    userEmail VARCHAR(255) COLLATE latin1_bin NOT NULL,
    creditCard VARCHAR(255) COLLATE latin1_bin NOT NULL,
    startRental DATETIME NOT NULL,
    endRental DATETIME NOT NULL,
    bikesToRent INTEGER NOT NULL,
    bikeModelId BIGINT NOT NULL,
    creationDate DATETIME NOT NULL,
    score REAL NOT NULL,
    CONSTRAINT ReservationPK PRIMARY KEY(rentalId),
    CONSTRAINT ReservationBikeModelFK FOREIGN KEY(bikeModelId)
        REFERENCES BikeModel(bikeModelId) ON DELETE CASCADE) ENGINE = InnoDB;
