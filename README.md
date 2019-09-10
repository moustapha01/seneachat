1. After cloning the repo, cd to the project root folder and run mvn -U clean install to install all mvn dependencies

2. The database is hosted in MS SQL Server. Create a new user senachat, password welcome1 under security in SQL Server. Under user properties and server roles
select db creator and public options.

3. Create database senachat. Navigate to senachat -> Security -> Users. Right click under Users and add new user with the following properties
General tab
User name: senachat
login: senachat
Default schema: dbo

Owned Schemas
Select the options db_datareader, db_datawriter, db_ddladmin, db_owner

Memberships
Select the options db_datareader, db_datawriter, db_ddladmin, db_owner. Then click OK.

4. Run the application by selecting the class com.signaretech.seneachat.SenachatApplication under senachat-server. Right click and run as Java application.
Then open a web browser and navigate to localhost:8080 to access the main page.

