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
* http://localhost:8080/registration: for registration
* http://localhost:8080/login : for log in
* http://localhost:8080/logout : for log out
* http://localhost:8080/user/update : for updating account info and delete account
* http://localhost:8080/admin/account-management : for promoting and revoking admin rights (needs to be admin)
* To become root admin: 
  1. Go to application.properties and set "app.init-db" to true, this is done to create root admin and create 1 "dummy" user account. 
  Note: please remember to set "app.init-db" to false when you want to run Spring Boot again, if not, there will be an error because the system tries to generate the same dummy accounts
  2. Run Spring Boot application
  3. Login account with "admin@gmail.com", password: admin
### Item Functions
* http://localhost:8080/item-form.html : for adding new product to system
* http://localhost:8080/item-list.html : for viewing all items in system, and selecting an item to edit or delete
### View products
* http://localhost:8080/shop : for viewing the product shop page
* http://localhost:8080/shoping-cart : for viewing the cart
* NOTE: to update and save product quantity in cart, please click the update button on the right of the "x" button.
### Checkout, admin confirm/cancel order
* NEEDS SOMETHING HERE

## Known issues and bugs
* Anchor tags bellow account name after logged in can not be open directly. However opening those tags on a new tab and pressing enter to access those links works.
* Currently, Item and Shop are 2 separate entities. Due to that the delete item function only removes such item from item table in the database, and not the shop table. Because of this, those deleted items will still show up in localhost:8080/shop. In the next sprint, we will merge these 2 entities which will coherently resolve this issue.
