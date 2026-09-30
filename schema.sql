CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS function_halls (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    capacity INT NOT NULL,
    price_per_day DECIMAL(12,2) NOT NULL,
    description TEXT,
    image_url VARCHAR(500),
    amenities VARCHAR(1000),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    hall_id BIGINT NOT NULL,
    event_date DATE NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    guest_count INT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    customer_email VARCHAR(150) NOT NULL,
    notes VARCHAR(1000),
    total_amount DECIMAL(12,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING_PAYMENT',
    payment_status VARCHAR(30) NOT NULL DEFAULT 'UNPAID',
    payment_id VARCHAR(150),
    gateway_order_id VARCHAR(150),
    refund_id VARCHAR(150),
    refund_status VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_booking_hall FOREIGN KEY (hall_id) REFERENCES function_halls(id),
    INDEX idx_booking_hall_date (hall_id, event_date),
    INDEX idx_booking_user (user_id)
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    gateway_order_id VARCHAR(150),
    gateway_payment_id VARCHAR(150),
    amount DECIMAL(12,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    signature VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES bookings(id),
    INDEX idx_payment_order (gateway_order_id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    hall_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_hall_review (user_id, hall_id),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_review_hall FOREIGN KEY (hall_id) REFERENCES function_halls(id)
);

INSERT INTO function_halls
(name, city, address, capacity, price_per_day, description, image_url, amenities)
SELECT 'Sri Lakshmi Convention Hall', 'Tirupati', 'Tiruchanoor Road, Tirupati', 800, 65000,
'Spacious convention hall suitable for weddings, receptions and large family events.',
'https://images.unsplash.com/photo-1519167758481-83f550bb49b3?auto=format&fit=crop&w=1200&q=80',
'Parking, AC, Stage, Dining Hall, Generator, Bridal Room'
WHERE NOT EXISTS (SELECT 1 FROM function_halls WHERE name='Sri Lakshmi Convention Hall');

INSERT INTO function_halls
(name, city, address, capacity, price_per_day, description, image_url, amenities)
SELECT 'Royal Grand Function Hall', 'Hyderabad', 'Madhapur, Hyderabad', 500, 45000,
'Modern air-conditioned function space for weddings, birthdays and corporate events.',
'https://images.unsplash.com/photo-1507504031003-b417219a0fde?auto=format&fit=crop&w=1200&q=80',
'AC, Parking, Stage, Catering Area, Projector'
WHERE NOT EXISTS (SELECT 1 FROM function_halls WHERE name='Royal Grand Function Hall');

INSERT INTO function_halls
(name, city, address, capacity, price_per_day, description, image_url, amenities)
SELECT 'Green Garden Banquet', 'Vijayawada', 'Benz Circle, Vijayawada', 300, 30000,
'Garden-style venue with indoor and outdoor spaces for intimate celebrations.',
'https://images.unsplash.com/photo-1464366400600-7168b8af9bc3?auto=format&fit=crop&w=1200&q=80',
'Garden, Parking, Dining, Stage, Lighting'
WHERE NOT EXISTS (SELECT 1 FROM function_halls WHERE name='Green Garden Banquet');
