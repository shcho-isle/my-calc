Calculator
=============================

To run the application with the default settings, no additional settings are required. Just build it with maven and deploy to tomcat.

---

DB config: `src/main/resources/db/hsqldb.properties`. Modify it to switch between "file" and "memory" DB type.

---

To prevent automatic DB initialization, comment out these lines in `src/main/resources/spring/spring-db.xml`:
```xml
    <jdbc:initialize-database data-source="dataSource" enabled="${database.init}">
        <jdbc:script location="classpath:db/initDB_hsql.sql"/>
    </jdbc:initialize-database>
```