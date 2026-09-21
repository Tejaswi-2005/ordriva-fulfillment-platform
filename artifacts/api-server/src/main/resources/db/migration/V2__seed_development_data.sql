INSERT INTO users (id, name, email, password_hash, role)
VALUES (1, 'Tejaswi Kunche', 'tejaswi@northstar.co', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN')
ON CONFLICT (id) DO NOTHING;

INSERT INTO products (id, sku, name, description, price, active)
VALUES
    (1, 'APC-001', 'AeroPress Clear', 'Clear coffee brewer.', 45.00, TRUE),
    (2, 'MDL-204', 'Mori Desk Lamp', 'Adjustable warm-light desk lamp.', 142.00, TRUE),
    (3, 'FNT-330', 'Field Notes 3-pack', 'Three-pack of pocket notebooks.', 24.00, TRUE),
    (4, 'ARC-090', 'Arc Speaker', 'Compact wireless speaker.', 210.00, TRUE),
    (5, 'TRT-118', 'Transit Tote', 'Structured everyday carry tote.', 68.00, TRUE),
    (6, 'CBT-451', 'Cobalt Bottle', 'Insulated travel bottle.', 31.50, FALSE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO warehouses (id, name, code, city, state, country, active)
VALUES
    (1, 'Oakland', 'OAK-01', 'Oakland', 'CA', 'US', TRUE),
    (2, 'Reno', 'RNO-02', 'Reno', 'NV', 'US', TRUE),
    (3, 'Brooklyn', 'BK-03', 'Brooklyn', 'NY', 'US', TRUE)
ON CONFLICT (id) DO NOTHING;

INSERT INTO inventory (id, product_id, warehouse_id, available_quantity, reserved_quantity, reorder_level, version)
VALUES
    (1, 1, 1, 284, 31, 80, 0),
    (2, 2, 3, 12, 8, 24, 0),
    (3, 3, 2, 96, 12, 30, 0),
    (4, 4, 2, 0, 4, 18, 0),
    (5, 5, 1, 42, 15, 24, 0),
    (6, 6, 3, 18, 11, 20, 0)
ON CONFLICT (id) DO NOTHING;

INSERT INTO orders (id, order_number, user_id, status, total_amount, warehouse_id, created_at, updated_at)
VALUES
    (1, 'ORD-10482', 1, 'SHIPPED', 68.50, 1, NOW() - INTERVAL '2 hours', NOW() - INTERVAL '30 minutes'),
    (2, 'ORD-10481', 1, 'PROCESSING', 24.00, 2, NOW() - INTERVAL '3 hours', NOW() - INTERVAL '2 hours'),
    (3, 'ORD-10480', 1, 'PAYMENT_PENDING', 142.00, 3, NOW() - INTERVAL '4 hours', NOW() - INTERVAL '4 hours')
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_items (id, order_id, product_id, quantity, unit_price)
VALUES
    (1, 1, 1, 1, 45.00),
    (2, 1, 5, 1, 23.50),
    (3, 2, 3, 1, 24.00),
    (4, 3, 2, 1, 142.00)
ON CONFLICT (id) DO NOTHING;

INSERT INTO payments (id, transaction_id, order_id, amount, payment_method, status, idempotency_key)
VALUES
    (1, 'txn_8c1a94', 1, 68.50, 'CARD', 'PAID', 'seed-order-10482'),
    (2, 'txn_25bf10', 2, 24.00, 'APPLE_PAY', 'PAID', 'seed-order-10481'),
    (3, 'txn_99ea21', 3, 142.00, 'CARD', 'PENDING', 'seed-order-10480')
ON CONFLICT (id) DO NOTHING;

INSERT INTO fulfillments (id, order_id, status, tracking_number)
VALUES
    (1, 1, 'SHIPPED', 'TRK-OAK-10482'),
    (2, 2, 'PACKING', NULL)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_status_history (id, order_id, status, occurred_at, reason)
VALUES
    (1, 1, 'CREATED', NOW() - INTERVAL '2 hours', 'Seeded development order'),
    (2, 1, 'INVENTORY_RESERVING', NOW() - INTERVAL '115 minutes', 'Inventory reservation started'),
    (3, 1, 'INVENTORY_RESERVED', NOW() - INTERVAL '112 minutes', 'Inventory reserved'),
    (4, 1, 'PAYMENT_PENDING', NOW() - INTERVAL '110 minutes', 'Payment authorization started'),
    (5, 1, 'CONFIRMED', NOW() - INTERVAL '105 minutes', 'Payment authorized'),
    (6, 1, 'PROCESSING', NOW() - INTERVAL '90 minutes', 'Warehouse processing started'),
    (7, 1, 'SHIPPED', NOW() - INTERVAL '30 minutes', 'Shipment dispatched'),
    (8, 2, 'CREATED', NOW() - INTERVAL '3 hours', 'Seeded development order'),
    (9, 2, 'INVENTORY_RESERVED', NOW() - INTERVAL '175 minutes', 'Inventory reserved'),
    (10, 2, 'PAYMENT_PENDING', NOW() - INTERVAL '170 minutes', 'Payment authorization started'),
    (11, 2, 'CONFIRMED', NOW() - INTERVAL '165 minutes', 'Payment authorized'),
    (12, 2, 'PROCESSING', NOW() - INTERVAL '2 hours', 'Warehouse processing started'),
    (13, 3, 'CREATED', NOW() - INTERVAL '4 hours', 'Seeded development order'),
    (14, 3, 'INVENTORY_RESERVED', NOW() - INTERVAL '235 minutes', 'Inventory reserved'),
    (15, 3, 'PAYMENT_PENDING', NOW() - INTERVAL '4 hours', 'Payment authorization pending')
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('users', 'id'), COALESCE((SELECT MAX(id) FROM users), 1));
SELECT setval(pg_get_serial_sequence('products', 'id'), COALESCE((SELECT MAX(id) FROM products), 1));
SELECT setval(pg_get_serial_sequence('warehouses', 'id'), COALESCE((SELECT MAX(id) FROM warehouses), 1));
SELECT setval(pg_get_serial_sequence('inventory', 'id'), COALESCE((SELECT MAX(id) FROM inventory), 1));
SELECT setval(pg_get_serial_sequence('orders', 'id'), COALESCE((SELECT MAX(id) FROM orders), 1));
SELECT setval(pg_get_serial_sequence('order_items', 'id'), COALESCE((SELECT MAX(id) FROM order_items), 1));
SELECT setval(pg_get_serial_sequence('payments', 'id'), COALESCE((SELECT MAX(id) FROM payments), 1));
SELECT setval(pg_get_serial_sequence('fulfillments', 'id'), COALESCE((SELECT MAX(id) FROM fulfillments), 1));
SELECT setval(pg_get_serial_sequence('order_status_history', 'id'), COALESCE((SELECT MAX(id) FROM order_status_history), 1));