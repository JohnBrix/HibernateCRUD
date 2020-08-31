# HibernateCRUD
Hibernate ORM as backend my front end is Java FX.

<h1>How to install: </h1>

<h3>First download the project, open or import in your Intellij IDEA.</h3>



<h3>Second install the following libraries: </h3>

<h4> • <dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>5.4.18.Final</version>
</dependency> </h4>

<h4> • <dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<version>5.1.23</version>
</dependency> </h4>

<h3>Third: open your XAMPP or any Mysql command:</h3>

<h4> Create a Database name 'hibernatecrud' </h4>

<h4>Then paste this in your SQL command: </h4>


<h4>CREATE TABLE `hibernatecrud`.`person` 
( `id` INT(11) NOT NULL AUTO_INCREMENT , `first_name` VARCHAR(50) NOT NULL , `last_name` INT(50) NOT NULL , PRIMARY KEY (`id`))
ENGINE = InnoDB; </h4>

<h2> Actual image: </h2>

![Java FX UI](img/ui.png)

<h2>How to delete if you dont see: </h2>

<h4> Select the following data that you want to delete, then Right Click mouse now you can delete.</h4>
