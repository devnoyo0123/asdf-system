INSERT INTO customer.customers (id, username, first_name, last_name)
VALUES ('fb16dd28-4205-4448-98a6-015ae19dcc40', 'user1', 'First', 'user');

INSERT INTO customer.customers (id, username, first_name, last_name)
VALUES ('85fe5fc6-fde9-42ed-8173-bd61bad329f6', 'user2', 'Second', 'user');

INSERT INTO restaurant.restaurants(id, name, active)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb45', 'restaurant_1', TRUE);
INSERT INTO restaurant.restaurants(id, name, active)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb46', 'restaurant_2', FALSE);

INSERT INTO restaurant.products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb47', 'product_1', 25.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb48', 'product_2', 50.00, TRUE);
INSERT INTO restaurant.products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb49', 'product_3', 20.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb50', 'product_4', 40.00, TRUE);

INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb51', 'd215b5f8-0249-4dc5-89a3-51fd148cfb45', 'd215b5f8-0249-4dc5-89a3-51fd148cfb47');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb52', 'd215b5f8-0249-4dc5-89a3-51fd148cfb45', 'd215b5f8-0249-4dc5-89a3-51fd148cfb48');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb53', 'd215b5f8-0249-4dc5-89a3-51fd148cfb46', 'd215b5f8-0249-4dc5-89a3-51fd148cfb49');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb54', 'd215b5f8-0249-4dc5-89a3-51fd148cfb46', 'd215b5f8-0249-4dc5-89a3-51fd148cfb50');
