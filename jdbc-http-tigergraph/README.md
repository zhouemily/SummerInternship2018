## What this project does  
This project uses Java to  
* Read records from relational database, like MySQL, using JDBC  
* Write records to TigerGraph database as vertices or edges using TigerGraph REST API over HTTP POST method.  
  
## Setup  
* Download and install [Java SE]([http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html))  
I used Java SE 8 for this project.  
* Download and install [MySQL]([https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/))  
I used MySQL v5.7 for this project.  
* Download and install [TigerGraph]([https://www.tigergraph.com/download/](https://www.tigergraph.com/download/))  
I used TigerGraph v2.1 for this project.  
  
## Create database table and populate data to it  
For example, using MySQL, after login to mysql shell using MySQL command-line tool  
```  
mysql>CREATE DATABASE tg_src_db;  
mysql>USE tg_src_db;  
mysql>SOURCE mysql_social.txt  
```  
The mysql_social.txt file is in this repository serves an example which contains SQL statements to create the SocialUser and SocialConn tables that are referenced in the config.json file in this project. The mysql_social.txt file also contains SQL INSERT statements to populate some data to the SocialUser table. You can run the following SQL statement to confirm the expected number of records has been inserted into the SocialUser table.  
```  
mysql>SELECT COUNT(*) FROM SocialUser;  
```  
## Define graph schema and loading job in TigerGraph database  
TigerGraph [GSQL 101]([https://doc.tigergraph.com/2.1/GSQL-101.html](https://doc.tigergraph.com/2.1/GSQL-101.html)) is a good start point to learn TigerGraph GSQL. The following shows how to create the graph socialroom that used in config.json.  
After login to gsql shell using TigerGraph GSQL command-line tool  
```  
gsql>CREATE VERTEX socialuser(PRIMARY_ID uid INT, name STRING, isActive BOOl, registrationTimestamp String)  
gsql>CREATE UNDIRECTED EDGE socialconn (FROM socialuser, TO socialuser)  
gsql>CREATE GRAPH socialroom (socialuser, socialconn)  
```  
then create the loading job, first create 2 files in user tignergraph home directory  
socialuser.csv which contains 1 line - the header  
```  
uid,name,isActive,registrationTimestamp  
```  
socialconn.csv which contains 1 line - the header  
```  
socialuser1, socialuser2  
```  
then in gsql shell  
```  
gsql>USE GRAPH socialroom  
gsql>BEGIN  
gsql>CREATE LOADING JOB load_socialroom FROM GRAPH socialroom {  
gsql> DEFINE FILENAME file1="/home/tigergraph/socialuser.csv;  
gsql> DEFINE FILENAME file2="/home/tigergraph/socialconn.csv;  
gsql>  
gsql> LOAD file1 TO VERTEX socialuser VALUES ($"uid", $"name", $"isActive", $"registrationTime") USING header="true", separator=",";  
gsql> LOAD file2 TO EDGE socialconn VALUES ($0, $1) USING header="true", separator=",";  
gsql>}  
gsql>END  
```  
and use following GSQL statements to examine the graph and loading job defined correctly and the number of vertices in the socialuser vertex is 0  
```  
gsql>ls  
```  
then run the loading job  
```  
gsql>RUN LOADING JOB load_socialroom  
```  
then use the following GSQL statement to see the number of vertices in the socialuser vertex is 0  
```  
gsql>SELECT count() FROM socialuser  
```  
then make the loading job online so that it accepts HTTP POST  
```  
gsql>offline2online load_socialroom  
gsql>ls  
```  
## Compile and run  
You can use either javac or your own IDE. I used [Bluej]([https://www.bluej.org/](https://www.bluej.org/)) for this project.  
The main TGJDBCRunner class expects 1 argument that is the name of the configuration file which is in json format. Please see the config.json file as example.
