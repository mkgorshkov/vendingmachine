# Vending Machine
Thank you for a wonderful challenge related to designing a vending
machine, please find attached the documentation related to design
decisions. More specific choices are commented throughout the application.

## Installation / Deployment
1. Download the .SQL file and create a new MySQL database 'vendingmachines', populating the schema and sample data from
{project_root}/src/main/java/dll/vending_machine_create_with_examples.sql
2. Spin up a vanilla Tomcat 8 instance
3. Deploy the pre-built war file from {project_root}/target/vendingmachine.war on the Tomcat 8 instance, or, generate a new build through a maven clean install
4. The page will be reachable on localhost:8080

## Front End
One of the most influential libraries for front-end javaScript frameworks
is react. Since it is advertised as a framework for UIs, this was a logical
choice for the vending machine interface. The plugins used in this implementation include axios for making API calls.

## Back End
We want a decoupled system which can sit in front of many different types of 
vending machines. To accommodate this separation, I've exposed REST-API endpoints
which allow the user, through the interface to interact with the underlying data. There were
a few ways to implement this challenge - have the state, items, etc live on the vending
machine itself (at the REST-API) level or have the data stored in the database and be
accessible through the API.

For the sake of scalability, I've taken all of the 
data out into the database and left only the required operational logic in the API. This
allows us to add n-vending machines with n-items. This also allows the system to gracefully
recover from a restart/power off situation.

## Improvements
- Want to look at a way for the technician to be able to rename the products, change the prices from the interface
- We want to potentially incorporate the position of each of the items when looking at the vending machine since we will need to have a
concept of position for turning the actual rings of the machine
- Obviously, in a real implementation, we would have some authentication that would be involved in each of the calls, especially for those
which require a technician (ex. Oauth, JWT Tokens)
- In a real life implementation, we would have the service functions behind some sort of password or verified area

### Database Structure
A vending machine has a unique ID which is used to identify how much it has in the register (how many coins of each type)
and what the inventory of the machine looks like. Since the same product ex. bag of chips can be the 
same across different machines, products and inventory are separate tables. Furthermore, the price 
is determined on a machine-by-machine basis since pricing may vary on location for the same product.

### Initial Values
The initial values of coins are loosely based on https://www.pagetable.com/?p=353

### Rest API Structure and Calls
#### Currency
POST: localhost:8080/vendingmachine/currency/{machine id}/insertChange/{coin value in cents}
- Validation: Ensure that machine id is recognized
- Validation: Ensure that the coin is one we handle, ex. no pennies
- Validation: Ensure that machine's cash register will not overflow ie. currentNumCoins <= maxNumCoins
GET: localhost:8080/vendingmachine/currency/{machine id}/currentlyInserted
- Validation: Ensure that machine id is recognized
GET: localhost:8080/vendingmachine/currency/{machine id}/returnChange
- Validation: Ensure that machine id is recognized
#### Items
POST: localhost:8080/vendingmachine/items/{machine id}/buy/{productid}
- Validation: Ensure that machine id is recognized
- Validation: Ensure that the product id is recognized
- Validation: Ensure that the user is able to pay for the item based on the change inserted
- Validation: Ensure that the user is able to buy the item based on whether it's in stock
#### Service
GET: localhost:8080/vendingmachine/service/{machine id}/availableChange
- Validation: Ensure that machine id is recognized
GET: localhost:8080/vendingmachine/service/{machine id}/availableStock
- Validation: Ensure that machine id is recognized
POST: localhost:8080/vendingmachine/service/{machine id}/restockProducts
- Validation: Ensure that machine id is recognized
- Only restock the products where the number of products is less than the maximum it can hold
POST: localhost:8080/vendingmachine/service/{machine id}/restockChange
- Validation: Ensure that machine id is recognized
- Only restock the change where the number of coins for that amount is less than the maximum it can hold

### Notes
- Because of hibernate caching, it is discouraged to do any external modifications to the database which are not done through the API or the webapp. If
you would like to do so please restart tomcat.