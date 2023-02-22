CREATE TABLE Address(
    addressID   INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    houseNumber TEXT,
    streetName  TEXT,
    placeName   TEXT,
    postCode    VARCHAR(8)

);

CREATE TABLE Person(
    title       VARCHAR(4),
    forename    VARCHAR(30),
    surname     VARCHAR(30),
    email       VARCHAR(255) PRIMARY KEY NOT NULL UNIQUE,
    password    VARCHAR(255),
    phoneNumber VARCHAR(15),
    addressID   INT,
    FOREIGN KEY (addressID) REFERENCES Address (addressID)

);

CREATE TABLE Host(
    email    VARCHAR(255) NOT NULL UNIQUE,
    hostID   INT          NOT NULL AUTO_INCREMENT,
    hostName VARCHAR(30),
    PRIMARY KEY (hostID, email),
    FOREIGN KEY (email) REFERENCES Person (email)

);


CREATE TABLE Guest(
    email     VARCHAR(255) NOT NULL UNIQUE,
    guestID   INT AUTO_INCREMENT,
    guestName VARCHAR(30),
    PRIMARY KEY (guestID, email),
    FOREIGN KEY (email) REFERENCES Person (email)

);

CREATE TABLE Property(
    propertyID      INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    hostID          INT,
    addressID       INT,
    shortName       VARCHAR(50),
    description     TEXT,
    generalLocation TEXT,
    hasBreakfast    BOOLEAN,
    FOREIGN KEY (hostID) REFERENCES Host (hostID),
    FOREIGN KEY (addressID) REFERENCES Address (addressID)

);

CREATE TABLE SleepingFacilities(
    propertyID  INT PRIMARY KEY NOT NULL UNIQUE,
    hasBedLinen BOOLEAN,
    hasTowels   BOOLEAN,
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)

);

CREATE TABLE Bedroom(
    bedRoomID  INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    propertyID INT,
    bed1Type   VARCHAR(10)     NOT NULL,
    bed2Type   VARCHAR(10),
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)

);


CREATE TABLE BathingFacilities(
    propertyID     INT PRIMARY KEY NOT NULL UNIQUE,
    hasHairDryer   BOOLEAN,
    hasShampoo     BOOLEAN,
    hasToiletPaper BOOLEAN,
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)

);

CREATE TABLE Bathroom(
    bathRoomID INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    propertyID INT,
    hasToilet  BOOLEAN,
    hasBath    BOOLEAN,
    hasShower  BOOLEAN,
    isShared   BOOLEAN,
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)

);

CREATE TABLE KitchenFacility(
    propertyID         INT PRIMARY KEY NOT NULL UNIQUE,
    hasRefrigerator    BOOLEAN,
    hasMicrowave       BOOLEAN,
    hasOven            BOOLEAN,
    hasStove           BOOLEAN,
    hasDishwasher      BOOLEAN,
    hasTableware       BOOLEAN,
    hasCookware        BOOLEAN,
    hasBasicProvisions BOOLEAN,
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)

);

CREATE TABLE LivingFacility(
    propertyID    INT PRIMARY KEY NOT NULL UNIQUE,
    hasWifi       BOOLEAN,
    hasTelevision BOOLEAN,
    hasSatellite  BOOLEAN,
    hasStreaming  BOOLEAN,
    hasDVDPlayer  BOOLEAN,
    hasBoardGames BOOLEAN,
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)

);

CREATE TABLE UtilityFacility(
    propertyID          INT PRIMARY KEY NOT NULL UNIQUE,
    hasCentralHeating   BOOLEAN,
    hasWashingMachine   BOOLEAN,
    hasDryingMachine    BOOLEAN,
    hasFireExtinguisher BOOLEAN,
    hasSmokeAlarm       BOOLEAN,
    hasFirstAidKit      BOOLEAN,
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)

);

CREATE TABLE OutdoorFacility(
    propertyID       INT PRIMARY KEY NOT NULL UNIQUE,
    hasOnSiteParking BOOLEAN,
    hasOnRoadParking BOOLEAN,
    hasPaidCarPark   BOOLEAN,
    hasPatio         BOOLEAN,
    hasBarbeque      BOOLEAN,
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)
);

CREATE TABLE Booking(
    bookingID  INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    guestID    INT,
    propertyID INT,
    startDate  DATE,
    endDate    DATE,
    pending    BOOLEAN,
    hostAgree  BOOLEAN,
    FOREIGN KEY (guestID) REFERENCES Guest (guestID),
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)
);

CREATE TABLE Review(
    bookingID          INT PRIMARY KEY NOT NULL UNIQUE,
    description        TEXT,
    cleanlinessScore   INT,
    communicationScore INT,
    checkInScore       INT,
    accuracyScore      INT,
    locationScore      INT,
    valueScore         INT,
    FOREIGN KEY (bookingID) REFERENCES Booking (bookingID)

);

CREATE TABLE ChargeBand(
    chargeBandID   INT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
    propertyID     INT,
    pricePerNight  FLOAT,
    cleaningCharge FLOAT,
    serviceCharge  FLOAT,
    startDate      DATE,
    endDate        DATE,
    FOREIGN KEY (propertyID) REFERENCES Property (propertyID)
);