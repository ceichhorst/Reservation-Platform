-- Clean tables
DELETE FROM reservation_action;
DELETE FROM reservation;
DELETE FROM admin_restaurant;
DELETE FROM service_instance;
DELETE FROM administrator;
DELETE FROM restaurant;

-- Seed restaurant
INSERT INTO restaurant (
    id,
    name,
    require_allergen_info,
    scheduling_type
)
VALUES (
    1,
    'Test Restaurant',
    false,
    'DATE_ONLY'
);

-- Seed administrator
INSERT INTO administrator (
    id,
    username,
    role,
    email
)
VALUES (
    1,
    'seedadmin',
    'SUPER_ADMIN',
    'seed@admin.com'
);

-- Link admin to restaurant
INSERT INTO admin_restaurant (
    admin_id,
    restaurant_id
)
VALUES (
    1,
    1
);

-- Seed service instance
-- IMPORTANT:
-- Use high IDs so Hibernate-generated IDs do not collide
INSERT INTO service_instance (
    id,
    restaurant_id,
    service_date,
    service_time,
    capacity,
    version,
    visible
)
VALUES (
    1000,
    1,
    CURRENT_DATE,
    CURRENT_TIME,
    10,
    0,
    true
);

-- Seed reservation
INSERT INTO reservation (
    id,
    service_instance_id,
    customer_name,
    email,
    party_size,
    status,
    version
)
VALUES (
    2000,
    1000,
    'Seed User',
    'seed@email.com',
    2,
    'CONFIRMED',
    0
);