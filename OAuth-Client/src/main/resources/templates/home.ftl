<!DOCTYPE html>
<html  xmlns:th="http://www.thymeleaf.org">
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello World !</title>
</head>
<body>
    <h2>Hello ${name!"World"} !</h2>
 
	<div class="container unauthenticated">
  	<div>
    	With GitHub: <a href="/oauth2/authorization/github">click here</a>
  	</div>
  	<div>
    	With Google: <a href="/oauth2/authorization/google">click here</a>
  	</div>
	</div>
    
    <#if name??>
	<div class="container authenticated">
  	Logged in as: <span id="user"></span>
  	<div>
	<form action="/logout" method="post">
	<input type="submit"/>
	</form>
  	</div>
	</div>
    </#if>

</body>
</html>