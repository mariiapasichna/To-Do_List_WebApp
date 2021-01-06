## To-Do List WebApp

Simple Web Application for working.

You can use the 'data.sql' file to deploy the database

You can run it from SQL Shell (psql) using the command:

> \i <path_to_file>/data.sql

The database stores three users with ADMIN and USER roles.


| Username      | Password | Role  |
| ------------- |:--------:|:-----:|
| mike@mail.com | Qwerty11 | ADMIN |
| nick@mail.com | Qwerty22 | USER  |
| nora@mail.com | Qwerty33 | USER  |


Security Level includes user Authentication and Authorization.

User with the ADMIN role has full access to all resources.

1. If user hasn't yet logged in, after visiting any page he is redirected to the login page, which is available at URL "/form-login". The login form include fields for entering login and password, and the button “LogIn”. As the “username” for login, uses the user’s email.

<p align="center">
  <img src="https://i.ibb.co/0CDQwhP/image1.png">
</p>

2. If user entered the invalid credentials, he is redirecting back to the login page with error information.

<p align="center">
  <img src="https://i.ibb.co/4trfWzH/image2.png">
</p>

3. If user's login and password was entered correctly, allow him access to web application according to his role:

   3.1. If user has role ADMIN, allow him full access to all resources of the web application.
    
<p align="center">
  <img src="https://i.ibb.co/ZMc5bxj/image3.png">
</p>

   3.2. If user has role USER:

After going to “All To-Dos of …” page, display for user the list of To-Dos where he is owner or collaborator. If user isn't owner To-Do, he isn't allowed to delete or edit this To-Do.

<p align="center">
  <img src="https://i.ibb.co/tK2SZFT/image4.png">
</p>

User can view any To-Dos where he is owner or collaborator.

<p align="center">
  <img src="https://i.ibb.co/9y0R5Mq/image5.png">
</p>

User isn't allowed to add collaborators to To-Do if he isn't the owner of this To-Do.

<p align="center">
  <img src="https://i.ibb.co/q5QnQ9C/image6.png">
</p>

In case of an attempt unauthorized access to forbidden URL,  user redirected to the “Access Denied” page and set the status code 403.

<p align="center">
  <img src="https://i.ibb.co/Z60m21q/image7.png">
</p>

4. If user clicks the "LogOut" button, user's session finished and redirect him to the login page.

For store user's credentials used “users” and “roles” tables from database.

User's password encoded using the BCrypt encryption algorith.
