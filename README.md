# COSC 2101
# Food Ordering System

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

## FUNCTIONS
### Account Functions
* localhost:8080/registration: for registration
* localhost:8080/login : for log in
* localhost:8080/logout : for log out
* localhost:8080/user/update : for updating account info and delete account
* localhost:8080/admin/account-management : for promoting and revoking admin rights (needs to be admin)
* To become root admin: 
  * 1. Go to application.properties and set "app.init-db" to true, this is done to create root admin and create 1 "dummy" user account. 
  * Note: set "app.init-db" to false when you want to run Spring Boot again
  * 2. Run Spring Boot application
  * 3. Login account with "admin@gmail.com", password: admin
### Item Functions
* localhost:8080/item-form.html : for adding new product to system
* localhost:8080/item-list.html : for viewing all items in system
### View products
* localhost:8080/shop : for viewing the product shop page
* localhost:8080/shoping-cart : for viewing the cart
* NOTE: to save product quantity in cart, please click the update button on the right of the "x" button.
### Checkout, admin confirm/cancel order
* NEEDS SOMETHING HERE

## Known issues and bugs
* Anchor tags bellow account name after logged in can not be open directly. However opening those tags on a new tab and pressing enter to access those links works.
* Currently, Item and Shopping are 2 separate entities. Due to that delete item function only removes such item from item table in the database and not the shop table. Because of this, those deleted 
