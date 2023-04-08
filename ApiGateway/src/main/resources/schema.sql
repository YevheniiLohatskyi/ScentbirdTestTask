CREATE TABLE countries (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    code VARCHAR(2) NOT NULL
);

CREATE TABLE covid_new_cases (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    country_id INTEGER NOT NULL,
    date DATE NOT NULL,
    new_cases INTEGER NOT NULL,
    FOREIGN KEY (country_id) REFERENCES countries(id)
);