#to access the h2 database you must add resources/application.properties and then add these lines in there:

###spring.datasource.url=jdbc:h2:mem:nameofyourdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
###spring.datasource.driverClassName=org.h2.Driver
###spring.datasource.username=yourusername
###spring.datasource.password=yourpassword
###spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
###spring.jpa.hibernate.ddl-auto=update
###spring.h2.console.enabled=true
