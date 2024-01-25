
# Ömer Pala interview project
This project is implemented for Hostfully interview. Below endpoints implemented.
* Get all bookings based on property
* List all bookings
* get all bookings 
* Create a new booking
* update a booking
* cancel a booking
* rebook a booking
* delete booking

* Blocking calls are simply the same apis with booking. To make a timeframe blocking API caller needs to mark booking is a blocking booking.
* all API's are usable for blocking bookings.

Only property initial data is added to h2db with 1-5 id. You can check them from resources/data.sql

### Running in local machine
1. Run `mvn install`
2. Run `mvn spring-boot:run`
3. H2 db will be initialized and h2 console is available at [h2 console](http://localhost:8080/h2-console). You can check records in the database


## Testing
Unit tests written for service implemented. You can check coverage by running `mvn test` and check `/target/site/index.html`
