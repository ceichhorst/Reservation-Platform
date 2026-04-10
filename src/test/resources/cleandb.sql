-- Clean tables (order matters because of FK constraints)
DELETE FROM reservation;
DELETE FROM service_instance;
DELETE FROM service_template;
DELETE FROM restaurant;

-- Insert test restaurant
INSERT INTO restaurant (id, name, requireAllergenInfo, schedulingType)
VALUES (1, 'Test Restaurant', 0, 'DATE_TIME');

-- Insert test service instance
INSERT INTO service_instance (id, restaurant_id, service_date, service_time, capacity, version)
VALUES (1, 1, CURRENT_DATE, CURRENT_TIME, 10, 0);

-- Insert test reservation
INSERT INTO reservation (
    id,
    service_instance_id,
    customer_name,
    email,
    party_size,
    status
)
VALUES (
    1001,
    1,
    'Seed User',
    'seed@email.com',
    2,
    'CONFIRMED'
);