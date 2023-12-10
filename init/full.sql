-- Select master database
USE [master];
GO

-- Delete database if it exists
IF EXISTS (SELECT name
           FROM master.dbo.sysdatabases
           WHERE name = 'cozify')
    BEGIN
        ALTER DATABASE cozify SET OFFLINE WITH ROLLBACK IMMEDIATE;
        ALTER DATABASE cozify SET ONLINE;
        DROP DATABASE cozify;
    END;
GO

-- Create the new database
CREATE DATABASE cozify;
GO

USE cozify;
GO

CREATE TABLE role
(
    role_id TINYINT IDENTITY PRIMARY KEY NOT NULL,
    role    VARCHAR(50)                  NOT NULL, -- 'admin' or 'user' or 'staff'
)

-- Create the Users table
CREATE TABLE [user]
(
    [user_id]    INT IDENTITY PRIMARY KEY NOT NULL,
    username     NVARCHAR(50)             NOT NULL,
    [password]   CHAR(32)                 NOT NULL,
    email        NVARCHAR(255)            NOT NULL,
    role_id      TINYINT                  NOT NULL REFERENCES role (role_id),
    -- These attributes are not mandatory upon account creation
    first_name   NVARCHAR(50),
    last_name    NVARCHAR(50),
    phone_number VARCHAR(13), -- excluding country code, most countries' phone number length do not exceed 13
    address      NVARCHAR(255)
);

-- Create the Categories table
CREATE TABLE category
(
    category_id   INT IDENTITY PRIMARY KEY NOT NULL,
    category_name NVARCHAR(100)            NOT NULL,
    is_hidden     BIT                      NOT NULL
);

-- Create the Clothes table
CREATE TABLE clothes
(
    clothes_id     INT IDENTITY PRIMARY KEY NOT NULL,
    clothes_name   NVARCHAR(255)            NOT NULL,
    price          DECIMAL(10, 2)           NOT NULL,
    discount       TINYINT                  NOT NULL,
    rating         TINYINT                  NOT NULL,
    stock_quantity INT                      NOT NULL,
    size           VARCHAR(10), -- Hats and some accessories do not have sizes
    is_hidden      BIT                      NOT NULL,
    category_id    INT                      NOT NULL REFERENCES category (category_id)
);

CREATE TABLE payment_method
(
    payment_method_id TINYINT IDENTITY PRIMARY KEY NOT NULL,
    payment_method    NVARCHAR(50)                 NOT NULL -- cod
)

CREATE TABLE voucher
(
    voucher_id       INT IDENTITY PRIMARY KEY NOT NULL,
    voucher_name     NVARCHAR(200)            NOT NULL,
    voucher_code     CHAR(16)                 NOT NULL,
    voucher_percent  TINYINT                  NOT NULL,
    voucher_quantity INT                      NOT NULL,
    start_date       DATETIME DEFAULT GETDATE(),
    end_date         DATETIME DEFAULT DATEADD(day, 7, GETDATE()), -- by default, a voucher lasts for 7 days since its creation
    is_hidden        BIT                      NOT NULL
)

-- Create the Orders table
CREATE TABLE [order]
(
    order_id          INT IDENTITY PRIMARY KEY NOT NULL,
    user_id           INT                      NOT NULL REFERENCES [user] (user_id),
    order_time        DATETIME     DEFAULT GETDATE(),
    status            NVARCHAR(50) DEFAULT 'pending', -- pending, packaging, delivering, delivered, cancelled
    payment_method_id TINYINT                  NOT NULL REFERENCES payment_method (payment_method_id),
    first_name        NVARCHAR(50)             NOT NULL,
    last_name         NVARCHAR(50)             NOT NULL,
    [address]         NVARCHAR(255)            NOT NULL,
    phone_number      VARCHAR(13)              NOT NULL,
    email             NVARCHAR(255)            NOT NULL,
    total             DECIMAL(10, 2)           NOT NULL,
    note              NVARCHAR(1000)
);

-- Create the OrderItems table
CREATE TABLE order_item
(
    order_item_id INT IDENTITY PRIMARY KEY NOT NULL,
    order_id      INT                      NOT NULL REFERENCES [order] (order_id),
    clothes_id    INT                      NOT NULL REFERENCES clothes (clothes_id),
    quantity      INT,
    subtotal      DECIMAL(10, 2)
);
GO

-- Soft delete (hide) clothes instead of permanently remove it (hard delete)
CREATE TRIGGER tr_HideClothes
    ON clothes
    INSTEAD OF DELETE
    AS
BEGIN
    UPDATE clothes
    SET is_hidden = 1
    WHERE clothes_id IN (SELECT clothes_id FROM deleted);
END;
GO

-- Soft delete (hide) category instead of permanently remove it (hard delete)
-- Also hides all clothes of that category
CREATE TRIGGER tr_HideCategory
    ON category
    INSTEAD OF DELETE
    AS
BEGIN
    UPDATE category
    SET is_hidden = 1
    WHERE category_id IN (SELECT category_id FROM deleted);

    UPDATE clothes
    SET is_hidden = 1
    WHERE category_id IN (SELECT category_id FROM deleted)
END;
GO

-- Upon enabling category's state, also do the same to Clothes of the same category
CREATE TRIGGER tr_UpdateClothesIsHidden
    ON category
    AFTER UPDATE
    AS
BEGIN
    IF UPDATE(is_hidden)
        BEGIN
            UPDATE clothes
            SET is_hidden = 0
            FROM clothes
                     INNER JOIN inserted ON clothes.category_id = inserted.category_id
            WHERE inserted.is_hidden = 0
              AND clothes.is_hidden = 1;
        END;
END;
GO;

USE cozify;
GO

INSERT INTO role (role)
VALUES ('admin'),
       ('staff'),
       ('user');

-- Insert sample data into the Users table
INSERT INTO [user] (username, [password], email, role_id, first_name, last_name)
VALUES ('john', CONVERT(CHAR(32), HashBytes('MD5', 'password123'), 2), 'john@gmail.com', 3, 'John', 'Doe'),
       ('jane', CONVERT(CHAR(32), HashBytes('MD5', 'password456'), 2), 'jane@gmail.com', 1, 'Jane', 'Smith'),
       ('admin1', CONVERT(CHAR(32), HashBytes('MD5', 'admin123'), 2), 'admin1@gmail.com', 1, 'Admin', 'One'),
       ('staff1', CONVERT(CHAR(32), HashBytes('MD5', 'staff123'), 2), 'staff1@gmail.com', 2, 'Staff', 'One'),
       ('staff2', CONVERT(CHAR(32), HashBytes('MD5', 'staff123'), 2), 'staff2@gmail.com', 2, 'Staff', 'Two'),
       ('staff3', CONVERT(CHAR(32), HashBytes('MD5', 'staff123'), 2), 'staff3@gmail.com', 2, 'Staff', 'Three'),
       ('user1', CONVERT(CHAR(32), HashBytes('MD5', 'user123'), 2), 'user1@gmail.com', 3, 'User', 'One'),
       ('user2', CONVERT(CHAR(32), HashBytes('MD5', 'user123'), 2), 'user2@gmail.com', 3, 'User', 'Two'),
       ('user3', CONVERT(CHAR(32), HashBytes('MD5', 'user123'), 2), 'user3@gmail.com', 3, 'User', 'Three');

-- Insert sample data into the Categories table
INSERT INTO category (category_name, is_hidden)
VALUES ('T-shirts', 0),
       ('Jeans', 0),
       ('Shoes', 0),
       ('Dresses', 0),
       ('Hats', 0),
       ('Accessories', 0);

-- Insert sample data into the Clothes table
INSERT INTO clothes (clothes_name, price, discount, rating, stock_quantity, size, is_hidden, category_id)
VALUES
    -- T-shirts
    ('Basic White Tee', 19.99, 10, 5, 8, 'XS', 0, 1),
    ('Basic White Tee', 19.99, 10, 5, 6, 'S', 0, 1),
    ('Basic White Tee', 19.99, 10, 5, 10, 'M', 0, 1),
    ('Basic White Tee', 19.99, 10, 5, 8, 'L', 0, 1),
    ('Basic White Tee', 19.99, 10, 5, 6, 'XL', 0, 1),
    ('Basic White Tee', 19.99, 10, 5, 10, 'XXL', 0, 1),

    ('Graphic Print T-shirt', 24.99, 0, 4, 5, 'XS', 0, 1),
    ('Graphic Print T-shirt', 24.99, 0, 4, 7, 'S', 0, 1),
    ('Graphic Print T-shirt', 24.99, 0, 4, 9, 'M', 0, 1),
    ('Graphic Print T-shirt', 24.99, 0, 4, 5, 'L', 0, 1),
    ('Graphic Print T-shirt', 24.99, 0, 4, 7, 'XL', 0, 1),
    ('Graphic Print T-shirt', 24.99, 0, 4, 9, 'XXL', 0, 1),

    ('Striped Tee', 14.99, 0, 4, 10, 'XS', 0, 1),
    ('Striped Tee', 14.99, 0, 4, 8, 'S', 0, 1),
    ('Striped Tee', 14.99, 0, 4, 12, 'M', 0, 1),
    ('Striped Tee', 14.99, 0, 4, 10, 'L', 0, 1),
    ('Striped Tee', 14.99, 0, 4, 8, 'XL', 0, 1),
    ('Striped Tee', 14.99, 0, 4, 12, 'XXL', 0, 1),

    ('V-neck Tee', 29.99, 5, 5, 7, 'XS', 0, 1),
    ('V-neck Tee', 29.99, 5, 5, 9, 'S', 0, 1),
    ('V-neck Tee', 29.99, 5, 5, 11, 'M', 0, 1),
    ('V-neck Tee', 29.99, 5, 5, 7, 'L', 0, 1),
    ('V-neck Tee', 29.99, 5, 5, 9, 'XL', 0, 1),
    ('V-neck Tee', 29.99, 5, 5, 11, 'XXL', 0, 1),

-- Jeans
    ('Straight Fit Jeans', 59.99, 0, 4, 6, 'XS', 0, 2),
    ('Straight Fit Jeans', 59.99, 0, 4, 8, 'S', 0, 2),
    ('Straight Fit Jeans', 59.99, 0, 4, 10, 'M', 0, 2),
    ('Straight Fit Jeans', 59.99, 0, 4, 6, 'L', 0, 2),
    ('Straight Fit Jeans', 59.99, 0, 4, 8, 'XL', 0, 2),
    ('Straight Fit Jeans', 59.99, 0, 4, 10, 'XXL', 0, 2),

    ('Skinny Jeans', 69.99, 15, 5, 5, 'XS', 0, 2),
    ('Skinny Jeans', 69.99, 15, 5, 7, 'S', 0, 2),
    ('Skinny Jeans', 69.99, 15, 5, 9, 'M', 0, 2),
    ('Skinny Jeans', 69.99, 15, 5, 5, 'L', 0, 2),
    ('Skinny Jeans', 69.99, 15, 5, 7, 'XL', 0, 2),
    ('Skinny Jeans', 69.99, 15, 5, 9, 'XXL', 0, 2),

    ('Ripped Jeans', 79.99, 0, 4, 7, 'XS', 0, 2),
    ('Ripped Jeans', 79.99, 0, 4, 9, 'S', 0, 2),
    ('Ripped Jeans', 79.99, 0, 4, 11, 'M', 0, 2),
    ('Ripped Jeans', 79.99, 0, 4, 7, 'L', 0, 2),
    ('Ripped Jeans', 79.99, 0, 4, 9, 'XL', 0, 2),
    ('Ripped Jeans', 79.99, 0, 4, 11, 'XXL', 0, 2),

    ('Slim Fit Jeans', 49.99, 0, 4, 8, 'XS', 0, 2),
    ('Slim Fit Jeans', 49.99, 0, 4, 10, 'S', 0, 2),
    ('Slim Fit Jeans', 49.99, 0, 4, 12, 'M', 0, 2),
    ('Slim Fit Jeans', 49.99, 0, 4, 8, 'L', 0, 2),
    ('Slim Fit Jeans', 49.99, 0, 4, 10, 'XL', 0, 2),
    ('Slim Fit Jeans', 49.99, 0, 4, 12, 'XXL', 0, 2),

-- Shoes
    ('Running Sneakers', 79.99, 0, 5, 10, '6', 0, 3),
    ('Running Sneakers', 79.99, 0, 5, 12, '7', 0, 3),
    ('Running Sneakers', 79.99, 0, 5, 14, '8', 0, 3),
    ('Running Sneakers', 79.99, 0, 5, 10, '9', 0, 3),
    ('Running Sneakers', 79.99, 0, 5, 12, '10', 0, 3),
    ('Running Sneakers', 79.99, 0, 5, 14, '11', 0, 3),

    ('Leather Boots', 99.99, 25, 5, 8, '6', 0, 3),
    ('Leather Boots', 99.99, 25, 5, 10, '7', 0, 3),
    ('Leather Boots', 99.99, 25, 5, 12, '8', 0, 3),
    ('Leather Boots', 99.99, 25, 5, 8, '9', 0, 3),
    ('Leather Boots', 99.99, 25, 5, 10, '10', 0, 3),
    ('Leather Boots', 99.99, 25, 5, 12, '11', 0, 3),

    ('Canvas Slip-Ons', 49.99, 0, 4, 14, '6', 0, 3),
    ('Canvas Slip-Ons', 49.99, 0, 4, 16, '7', 0, 3),
    ('Canvas Slip-Ons', 49.99, 0, 4, 18, '8', 0, 3),
    ('Canvas Slip-Ons', 49.99, 0, 4, 14, '9', 0, 3),
    ('Canvas Slip-Ons', 49.99, 0, 4, 16, '10', 0, 3),
    ('Canvas Slip-Ons', 49.99, 0, 4, 18, '11', 0, 3),

    ('High Heels', 89.99, 0, 5, 9, '6', 0, 3),
    ('High Heels', 89.99, 0, 5, 11, '7', 0, 3),
    ('High Heels', 89.99, 0, 5, 13, '8', 0, 3),
    ('High Heels', 89.99, 0, 5, 9, '9', 0, 3),
    ('High Heels', 89.99, 0, 5, 11, '10', 0, 3),
    ('High Heels', 89.99, 0, 5, 13, '11', 0, 3),

-- Dresses
    ('Floral Print Dress', 89.99, 0, 4, 5, 'XS', 0, 4),
    ('Floral Print Dress', 89.99, 0, 4, 7, 'S', 0, 4),
    ('Floral Print Dress', 89.99, 0, 4, 9, 'M', 0, 4),
    ('Floral Print Dress', 89.99, 0, 4, 5, 'L', 0, 4),
    ('Floral Print Dress', 89.99, 0, 4, 7, 'XL', 0, 4),
    ('Floral Print Dress', 89.99, 0, 4, 9, 'XXL', 0, 4),

    ('Little Black Dress', 99.99, 0, 5, 6, 'XS', 0, 4),
    ('Little Black Dress', 99.99, 0, 5, 8, 'S', 0, 4),
    ('Little Black Dress', 99.99, 0, 5, 10, 'M', 0, 4),
    ('Little Black Dress', 99.99, 0, 5, 6, 'L', 0, 4),
    ('Little Black Dress', 99.99, 0, 5, 8, 'XL', 0, 4),
    ('Little Black Dress', 99.99, 0, 5, 10, 'XXL', 0, 4),

    ('Maxi Dress', 79.99, 0, 4, 7, 'XS', 0, 4),
    ('Maxi Dress', 79.99, 0, 4, 9, 'S', 0, 4),
    ('Maxi Dress', 79.99, 0, 4, 11, 'M', 0, 4),
    ('Maxi Dress', 79.99, 0, 4, 7, 'L', 0, 4),
    ('Maxi Dress', 79.99, 0, 4, 9, 'XL', 0, 4),
    ('Maxi Dress', 79.99, 0, 4, 11, 'XXL', 0, 4),

    ('Wrap Dress', 69.99, 0, 5, 8, 'XS', 0, 4),
    ('Wrap Dress', 69.99, 0, 5, 10, 'S', 0, 4),
    ('Wrap Dress', 69.99, 0, 5, 12, 'M', 0, 4),
    ('Wrap Dress', 69.99, 0, 5, 8, 'L', 0, 4),
    ('Wrap Dress', 69.99, 0, 5, 10, 'XL', 0, 4),
    ('Wrap Dress', 69.99, 0, 5, 12, 'XXL', 0, 4),

-- Hats
    ('Baseball Cap', 19.99, 0, 5, 20, NULL, 0, 5),
    ('Beanie', 14.99, 0, 5, 25, NULL, 0, 5),
    ('Bucket Hat', 24.99, 0, 5, 15, NULL, 0, 5),
    ('Panama Hat', 34.99, 0, 5, 10, NULL, 0, 5),
    ('Fedora', 29.99, 0, 5, 12, NULL, 0, 5),

-- Accessories
    ('Leather Belt', 29.99, 0, 5, 15, 'S', 0, 6),
    ('Leather Belt', 29.99, 0, 5, 20, 'M', 0, 6),
    ('Leather Belt', 29.99, 0, 5, 25, 'L', 0, 6),
    ('Leather Belt', 29.99, 0, 5, 30, 'XL', 0, 6),

    ('Silk Scarf', 39.99, 0, 4, 15, NULL, 0, 6),

    ('Silver Necklace', 49.99, 0, 5, 20, NULL, 0, 6),

    ('Gold Earrings', 59.99, 0, 2, 25, NULL, 0, 6),

    ('Leather Wallet', 34.99, 0, 5, 20, NULL, 0, 6);

INSERT INTO voucher (voucher_name, voucher_code, voucher_percent, voucher_quantity, is_hidden)
VALUES ('10% Off', 'ABCD1234EFGH5678', 10, 100, 0),
       ('20% Off', 'IJKL9012MNOP3456', 20, 50, 0),
       ('30% Off', 'QRST6789UVWX1234', 30, 30, 1),
       ('50% Off', 'YZAB4567CDEF8901', 50, 10, 0);

INSERT INTO payment_method (payment_method)
VALUES ('cod'),
       ('debit'),
       ('credit');


-- Inserting sample data into the updated Order table
INSERT INTO [order] (user_id, order_time, status, payment_method_id, first_name, last_name, [address], phone_number,
                     email,
                     total,
                     note)
VALUES
    -- Pending
    (1, '2022-01-05 12:00:00', 'pending', 1, 'John', 'Doe', '1234 Street, City, State', '1234567890',
     'johndoe@gmail.com', 99.99, 'Please deliver before 6 PM'),
    (2, '2022-01-07 13:00:00', 'pending', 1, 'Jane', 'Smith', '1234 Lane, City, State', '2345678901',
     'janesmith@gmail.com', 89.99, 'Leave at the front door'),

    -- Packaging
    (1, '2022-01-10 14:00:00', 'packaging', 1, 'John', 'Doe', '1234 Road, City, State', '3456789012',
     'johndoe@gmail.com', 79.99, null),
    (2, '2022-01-12 15:00:00', 'packaging', 1, 'Jane', 'Smith', '1234 Blvd, City, State', '4567890123',
     'janesmith@gmail.com', 69.99, null),

    -- Delivering
    (1, '2022-01-15 16:00:00', 'delivering', 1, 'John', 'Doe', '1234 Ave, City, State', '5678901234',
     'johndoe@gmail.com', 59.99, 'Call upon arrival'),
    (2, '2022-01-17 17:00:00', 'delivering', 1, 'Jane', 'Smith', '1234 St, City, State', '6789012345',
     'janesmith@gmail.com', 49.99, 'No need to ring the bell'),

    -- Delivered
    (1, '2022-01-20 18:00:00', 'delivered', 1, 'John', 'Doe', '1234 Ln, City, State', '7890123456',
     'johndoe@gmail.com', 39.99, NULL),
    (2, '2022-01-22 19:00:00', 'delivered', 1, 'Jane', 'Smith', '1234 Rd, City, State', '8901234567',
     'janesmith@gmail.com', 29.99, NULL),

    -- Cancelled
    (1, '2022-01-25 20:00:00', 'cancelled', 1, 'John', 'Doe', '1234 Blvd, City, State', '9012345678',
     'johndoe@gmail.com', 19.99, NULL),
    (2, '2022-01-27 21:00:00', 'cancelled', 1, 'Jane', 'Smith', '1234 Ave, City, State', '0123456789',
     'janesmith@gmail.com', 9.99, NULL);

-- Inserting sample data into the OrderItem table
INSERT INTO order_item (order_id, clothes_id, quantity, subtotal)
VALUES
    -- Order 1
    (1, 1, 2, 19.98), -- 2 items of clothes 1 for order 1
    (1, 2, 1, 9.99),  -- 1 item of clothes 2 for order 1

    -- Order 2
    (2, 1, 1, 9.99),  -- 1 item of clothes 1 for order 2
    (2, 3, 3, 29.97), -- 3 items of clothes 3 for order 2

    -- Order 3
    (3, 2, 2, 19.98), -- 2 items of clothes 2 for order 3
    (3, 3, 1, 9.99); -- 1 item of clothes 3 for order 3
GO