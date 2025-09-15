# Inventory management backend
### link to frontend https://github.com/andronisdre/inventory-management-frontend

## The backend for my inventory management application
I made this project as an assignment to show VGR on the recruitment day.

The project is made to handle the inventory of articles/products in a healthcare center. The project is made in intelliJ with spring boot

## Functionality
I made CRUD functions for the article model (create, read, update, delete)
Except for the basic crud functions you can also simply patch the amount of an article by either subtracting ord adding a number to the current amount.
The get all function has pagination, search, sorting and filtering logic so that you can get only the articles you want. 
this would be useful when there are hundreds of articles

## Installation
first, on the code page of the repository, click the green button "code", then click the copy url to clipboard button.
After that, open a command prompt and use cd.. cd folderName to move to the location you want to install the project in.
Then when in the correct folder location, type "git clone" and paste the url from your clipboard that you copied earlier. 
After that, open the project in your IDE that can handle java, for example intelliJ. 

To run the application, go to the file InventoryManagementBackendApplication in src/main/java/se.vgregion.inventory_management_backend/services and click the run button

## Database
to access the h2 in-memory database, go to "src/main" and add the folder and file "resources/application.properties" and then add these lines in there:
```
spring.datasource.url=jdbc:h2:mem:nameofyourdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
```
i didnt add application.properties directly with git to show that i understand gitignore practices. normally you would ignore database information since it would contain confidential information.

if you want to retain the database when restarting the application you have to cahnge from memory to file-based.
