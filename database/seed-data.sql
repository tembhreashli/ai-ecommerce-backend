-- AI E-Commerce Sample Data
-- This script populates the database with sample data for testing

-- Insert sample categories
INSERT INTO categories (name, description, image_url, is_active) VALUES
('Electronics', 'Electronic devices and accessories', 'https://images.unsplash.com/photo-1498049794561-7780e7231661', true),
('Clothing', 'Fashion and apparel', 'https://images.unsplash.com/photo-1489987707025-afc232f7ea0f', true),
('Books', 'Books and educational materials', 'https://images.unsplash.com/photo-1495446815901-a7297e633e8d', true),
('Home & Garden', 'Home improvement and gardening supplies', 'https://images.unsplash.com/photo-1513694203232-719a280e022f', true),
('Sports', 'Sports equipment and fitness gear', 'https://images.unsplash.com/photo-1461896836934-ffe607ba8211', true)
ON CONFLICT (name) DO NOTHING;

-- Insert subcategories
INSERT INTO categories (name, description, parent_id, is_active) VALUES
('Smartphones', 'Mobile phones and accessories', (SELECT id FROM categories WHERE name = 'Electronics' LIMIT 1), true),
('Laptops', 'Portable computers', (SELECT id FROM categories WHERE name = 'Electronics' LIMIT 1), true),
('Men''s Clothing', 'Clothing for men', (SELECT id FROM categories WHERE name = 'Clothing' LIMIT 1), true),
('Women''s Clothing', 'Clothing for women', (SELECT id FROM categories WHERE name = 'Clothing' LIMIT 1), true)
ON CONFLICT (name) DO NOTHING;

-- Insert sample users (password is 'password123' hashed with BCrypt)
INSERT INTO users (username, email, password, first_name, last_name, phone, user_role, is_active, is_verified) VALUES
('admin', 'admin@aiecommerce.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'User', '+1234567890', 'ADMIN', true, true),
('john_doe', 'john@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John', 'Doe', '+1234567891', 'CUSTOMER', true, true),
('jane_smith', 'jane@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane', 'Smith', '+1234567892', 'CUSTOMER', true, true),
('vendor1', 'vendor@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vendor', 'One', '+1234567893', 'VENDOR', true, true)
ON CONFLICT (username) DO NOTHING;

-- Insert sample products
INSERT INTO products (name, description, sku, price, cost_price, quantity, category_id, image_url, status, rating, review_count, is_active) VALUES
-- Electronics
('iPhone 15 Pro', 'Latest Apple iPhone with advanced features', 'IPHONE-15-PRO-001', 999.99, 750.00, 50, (SELECT id FROM categories WHERE name = 'Smartphones' LIMIT 1), 'https://images.unsplash.com/photo-1632661674596-df8be070a5c5', 'ACTIVE', 4.8, 128, true),
('Samsung Galaxy S24', 'Premium Android smartphone with excellent camera', 'SAMSUNG-S24-001', 899.99, 680.00, 45, (SELECT id FROM categories WHERE name = 'Smartphones' LIMIT 1), 'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c', 'ACTIVE', 4.7, 95, true),
('MacBook Pro 16"', 'Professional laptop for creative work', 'MACBOOK-PRO-16-001', 2499.99, 1950.00, 25, (SELECT id FROM categories WHERE name = 'Laptops' LIMIT 1), 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8', 'ACTIVE', 4.9, 210, true),
('Dell XPS 15', 'High-performance Windows laptop', 'DELL-XPS-15-001', 1799.99, 1350.00, 30, (SELECT id FROM categories WHERE name = 'Laptops' LIMIT 1), 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45', 'ACTIVE', 4.6, 167, true),
('Wireless Earbuds Pro', 'Premium noise-cancelling earbuds', 'EARBUDS-PRO-001', 149.99, 89.99, 100, (SELECT id FROM categories WHERE name = 'Electronics' LIMIT 1), 'https://images.unsplash.com/photo-1590658268037-6bf12165a8df', 'ACTIVE', 4.5, 342, true),

-- Clothing
('Men''s Classic T-Shirt', 'Comfortable cotton t-shirt', 'MENS-TSHIRT-001', 29.99, 12.00, 200, (SELECT id FROM categories WHERE name = 'Men''s Clothing' LIMIT 1), 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab', 'ACTIVE', 4.3, 89, true),
('Men''s Denim Jeans', 'Classic fit denim jeans', 'MENS-JEANS-001', 59.99, 28.00, 150, (SELECT id FROM categories WHERE name = 'Men''s Clothing' LIMIT 1), 'https://images.unsplash.com/photo-1542272604-787c3835535d', 'ACTIVE', 4.4, 124, true),
('Women''s Summer Dress', 'Light and comfortable summer dress', 'WOMENS-DRESS-001', 79.99, 35.00, 120, (SELECT id FROM categories WHERE name = 'Women''s Clothing' LIMIT 1), 'https://images.unsplash.com/photo-1515372039744-b8f02a3ae446', 'ACTIVE', 4.6, 198, true),
('Women''s Yoga Pants', 'Stretchy and comfortable yoga pants', 'WOMENS-YOGA-001', 49.99, 22.00, 180, (SELECT id FROM categories WHERE name = 'Women''s Clothing' LIMIT 1), 'https://images.unsplash.com/photo-1506629082955-511b1aa562c8', 'ACTIVE', 4.7, 267, true),

-- Books
('The Art of Programming', 'Comprehensive guide to software development', 'BOOK-PROG-001', 49.99, 25.00, 75, (SELECT id FROM categories WHERE name = 'Books' LIMIT 1), 'https://images.unsplash.com/photo-1532012197267-da84d127e765', 'ACTIVE', 4.8, 89, true),
('AI and Machine Learning', 'Introduction to AI and ML concepts', 'BOOK-AI-001', 59.99, 30.00, 60, (SELECT id FROM categories WHERE name = 'Books' LIMIT 1), 'https://images.unsplash.com/photo-1485988412941-77a35537dae4', 'ACTIVE', 4.9, 134, true),

-- Home & Garden
('Smart LED Bulbs (4-Pack)', 'WiFi-enabled color-changing bulbs', 'LED-BULBS-001', 39.99, 18.00, 150, (SELECT id FROM categories WHERE name = 'Home & Garden' LIMIT 1), 'https://images.unsplash.com/photo-1550985543-49bee3167284', 'ACTIVE', 4.4, 201, true),
('Garden Tool Set', 'Complete set of gardening tools', 'GARDEN-TOOLS-001', 89.99, 42.00, 85, (SELECT id FROM categories WHERE name = 'Home & Garden' LIMIT 1), 'https://images.unsplash.com/photo-1416879595882-3373a0480b5b', 'ACTIVE', 4.5, 112, true),

-- Sports
('Yoga Mat Premium', 'Non-slip yoga mat with carrying strap', 'YOGA-MAT-001', 34.99, 15.00, 200, (SELECT id FROM categories WHERE name = 'Sports' LIMIT 1), 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f', 'ACTIVE', 4.6, 178, true),
('Dumbbell Set 20kg', 'Adjustable dumbbell set', 'DUMBBELL-20KG-001', 129.99, 68.00, 50, (SELECT id FROM categories WHERE name = 'Sports' LIMIT 1), 'https://images.unsplash.com/photo-1583454110551-21f2fa2afe61', 'ACTIVE', 4.7, 93, true)
ON CONFLICT (sku) DO NOTHING;

-- Insert sample carts for users
INSERT INTO carts (user_id) VALUES
((SELECT user_id FROM users WHERE username = 'john_doe' LIMIT 1)),
((SELECT user_id FROM users WHERE username = 'jane_smith' LIMIT 1))
ON CONFLICT (user_id) DO NOTHING;

-- Insert sample cart items
INSERT INTO cart_items (cart_id, product_id, quantity, price) VALUES
((SELECT id FROM carts WHERE user_id = (SELECT user_id FROM users WHERE username = 'john_doe' LIMIT 1) LIMIT 1),
 (SELECT id FROM products WHERE sku = 'IPHONE-15-PRO-001' LIMIT 1), 1, 999.99),
((SELECT id FROM carts WHERE user_id = (SELECT user_id FROM users WHERE username = 'john_doe' LIMIT 1) LIMIT 1),
 (SELECT id FROM products WHERE sku = 'EARBUDS-PRO-001' LIMIT 1), 2, 149.99),
((SELECT id FROM carts WHERE user_id = (SELECT user_id FROM users WHERE username = 'jane_smith' LIMIT 1) LIMIT 1),
 (SELECT id FROM products WHERE sku = 'MACBOOK-PRO-16-001' LIMIT 1), 1, 2499.99)
ON CONFLICT (cart_id, product_id) DO NOTHING;

-- Insert sample orders
INSERT INTO orders (user_id, order_number, status, total_amount, shipping_address, billing_address, payment_method, payment_status) VALUES
((SELECT user_id FROM users WHERE username = 'john_doe' LIMIT 1), 
 'ORD-2024-00001', 'DELIVERED', 1299.97,
 '123 Main St, New York, NY 10001, USA',
 '123 Main St, New York, NY 10001, USA',
 'CREDIT_CARD', 'COMPLETED'),
((SELECT user_id FROM users WHERE username = 'jane_smith' LIMIT 1),
 'ORD-2024-00002', 'SHIPPED', 2579.98,
 '456 Oak Ave, Los Angeles, CA 90001, USA',
 '456 Oak Ave, Los Angeles, CA 90001, USA',
 'PAYPAL', 'COMPLETED')
ON CONFLICT (order_number) DO NOTHING;

-- Insert sample order items
INSERT INTO order_items (order_id, product_id, quantity, price, discount) VALUES
((SELECT id FROM orders WHERE order_number = 'ORD-2024-00001' LIMIT 1),
 (SELECT id FROM products WHERE sku = 'IPHONE-15-PRO-001' LIMIT 1), 1, 999.99, 0),
((SELECT id FROM orders WHERE order_number = 'ORD-2024-00001' LIMIT 1),
 (SELECT id FROM products WHERE sku = 'EARBUDS-PRO-001' LIMIT 1), 2, 149.99, 0),
((SELECT id FROM orders WHERE order_number = 'ORD-2024-00002' LIMIT 1),
 (SELECT id FROM products WHERE sku = 'MACBOOK-PRO-16-001' LIMIT 1), 1, 2499.99, 0),
((SELECT id FROM orders WHERE order_number = 'ORD-2024-00002' LIMIT 1),
 (SELECT id FROM products WHERE sku = 'MENS-JEANS-001' LIMIT 1), 1, 59.99, 10.00);

-- Display summary
DO $$
DECLARE
    user_count INT;
    category_count INT;
    product_count INT;
    order_count INT;
BEGIN
    SELECT COUNT(*) INTO user_count FROM users;
    SELECT COUNT(*) INTO category_count FROM categories;
    SELECT COUNT(*) INTO product_count FROM products;
    SELECT COUNT(*) INTO order_count FROM orders;
    
    RAISE NOTICE 'Database seeded successfully!';
    RAISE NOTICE 'Users: %', user_count;
    RAISE NOTICE 'Categories: %', category_count;
    RAISE NOTICE 'Products: %', product_count;
    RAISE NOTICE 'Orders: %', order_count;
END $$;
