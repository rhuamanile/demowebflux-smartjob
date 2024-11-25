    CREATE TABLE users (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255),
                           email VARCHAR(255) UNIQUE NOT NULL,
                           password VARCHAR(255) NOT NULL,
                           created_date TIMESTAMP,
                           modified_date TIMESTAMP,
                           last_login TIMESTAMP,
                           token VARCHAR(255),
                           inactive BOOLEAN
    );

    CREATE TABLE phones (
                            id UUID PRIMARY KEY,
                            user_id UUID,
                            number VARCHAR(20),
                            city_code VARCHAR(10),
                            country_code VARCHAR(10),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );