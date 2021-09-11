# COSC 2101
# Food Ordering System
[![Build Status](https://app.travis-ci.com/s3639869/SEPT-Group1.svg?branch=master)](https://app.travis-ci.com/s3639869/SEPT-Group1)
[![Heroku](https://pyheroku-badge.herokuapp.com/?app=sept-group1)](https://sept-group1.herokuapp.com/)

### Group 1
> * Vo An Huy
> * Doan Nguyen My Hanh
> * Doyun Kim
> * Vo Khang Di

### Project Architecture
## BACKEND
### Backend technologies:
* PostgreSQL
* Spring MVC
* Hibernate
* JPARepository
* Spring Security

##  FRONTEND
### Frontend technologies:
* Thymeleaf
* HTML/CSS/JS
* Bootstrap
* A template by Colorlib

### Before running:
- Go to file `.../config/AppConfig.java` and make changes based on your database.    
```
  dataSource.setDriverClassName("org.postgresql.Driver");
  dataSource.setUrl("jdbc:postgresql://localhost:[your port]/[your schema]");
  dataSource.setUsername([your username]);
  dataSource.setPassword([your password]);
```
### Running:
- Open terminal
```
  mvn clean install
  mvn spring-boot:run
```
## FUNCTIONALITIES
### Account functions
![login](https://user-images.githubusercontent.com/54358309/132903507-5102f8c0-a730-4cd0-960f-49abc8343100.jpg)

![drop-down](https://user-images.githubusercontent.com/54358309/132903521-1cb7487a-c59b-4373-a88d-5b526f1e59ae.jpg)

Account information and functionalities can be accessed through the drop-down section at the top-left of the website, or directly from the URLs below.
* http://localhost:8080/registration : For registering a new account - the phone number needs to be a Vietnamese phone number format.
* http://localhost:8080/login : For logging in with an existing account.
* http://localhost:8080/logout : For loggin out of the current session.
* http://localhost:8080/user/update : For updating your account information, and deleting the account.
* http://localhost:8080/order-history : For viewing your own order history, and selecting to view a specific order's details.

**To populate the database and website with 3 dummy accounts (2 Admins, 1 user) and 4 pages of shop products:**
1. Go to the application.properties file, and set "app.init-db" to true, then run the application.

*NOTE: please remember to set "app.init-db" to false when you want to run the application again, if not, there will be an error because the system tries to insert the same data again into the database*

2. Login with:

`Default ADMIN account:`
> * Email: admin@gmail.com
> * Password: admin

`Default USER account:`
> * Email: user@gmail.com
> * Password: password

`Default ADMIN account 2:`
> * Email: cakeorder.user@gmail.com
> * Password: 123

*NOTE: The second ADMIN account is the only account with a real, working email. If you want to test the email notifications (when placing an order, or whenever the order status is changed), please use that account.
If you do not want to use our email, you can register a new account with your own email (a temporary mail, GMail, or any email you have access to), and the notifications for the orders will be sent to your own email.*

`Google credentials for our email - you can login to GMail and check the mailbox to see if emails are sent:`
> * Email: cakeorder.user@gmail.com
> * Password: G1@cOsc21O1&

### Admin-only functions (only accessible when logged in with an Admin account)

![admin-panel](https://user-images.githubusercontent.com/54358309/132901151-a8302078-6725-415c-9430-fced69814ce0.jpg)

All Admin functionalities can be accessed via the Admin Panel in the navbar, or directly from the URLs below.
* http://localhost:8080/item-form : For adding a new item into the system.
* http://localhost:8080/item-list : For viewing all items in system, and selecting an item to edit or delete.
* http://localhost:8080/account-management : For viewing a list of all accounts, and selecting a specific account to view details, promote or revoke their Admin rights.
* http://localhost:8080/orderlist : For viewing a list of all orders, viewing the details of a specific order and changing the order status.
### Shop, cart and checkout functions

![shop-page](https://user-images.githubusercontent.com/54358309/132901782-506c5fe3-4780-4201-aa12-e19b2280aa20.jpg)
![cart-top](https://user-images.githubusercontent.com/54358309/132904978-cdb93186-9ed6-4617-8ab7-46b9860c3e4e.jpg)

The shop and cart can be visited by clicking "Shop" or "Cart" in the navbar, the cart icon at the top-right corner, or directly from the URLs below.
* http://localhost:8080/shop : For viewing the shop page of all products. It is possible to search the shop by keyword, sort by ID, name, price ascending or descending, and filter by category. Each product's details page can be visited by clicking on the product link. You can add a product to your cart from the details page.
* http://localhost:8080/shopping-cart : For viewing the current cart, updating quantity of items or deleting items from the cart.
*NOTE: to update and save the product quantity in cart, please click the black update button on the right of that item in the cart.*

* To proceed to the checkout, you will need to have at least 1 item in your cart. You can press "Proceed to Checkout" or access the URL http://localhost:8080/checkout to get to the checkout page and see the summary of your order, when you click "Place Order", the order will be saved into the system and an email will be sent to the email you used to register the account.

## Travis-CI Build
* Our current build is passing.

![travis0](https://user-images.githubusercontent.com/54358309/132946099-5a935544-36e9-453e-a0d3-de778fafdcd2.jpg)
![travis](https://user-images.githubusercontent.com/54358309/132946102-2cb6fdfe-3718-400d-887f-1df995d29b5e.jpg)
