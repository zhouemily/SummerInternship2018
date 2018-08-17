## What this project does
This project exercises using JDBC to  
* Insert and select a record from MySQL database  
  
## Setup  
* Download and install [Java SE]([http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html))  
I used Java SE 8 for this project.  
* Download and install [MySQL]([https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/))  
I used MySQL v5.7 for this project.  
  
## Create database table  
After login to mysql shell using MySQL command-line tool  
```  
mysql>CREATE DATABASE test;  
mysql>USE test;  
mysql>CREATE TABLE grades (name VARCHAR(20), lettergrade VARCHAR(2));  
```  
## Compile and run  
You can use either javac or your own IDE. I used [Bluej]([https://www.bluej.org/](https://www.bluej.org/)) for this project.  
* Run MyJDBCInsertRunner to insert a student record. It will prompt you enter the student name and the letter grade of the student.  
* Run MyJDBCSelectRunner to select a student record. It will prompt you enter the student name.
