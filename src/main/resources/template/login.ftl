<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/html">
<head>
    <title>Welcome!</title>
    <meta charset="utf-8"/>
    <link rel='stylesheet' href='/lab07/static/css/lab07.css'>
</head>
<body>
<h1>Введите пароль</h1>

<form action="/lab07/usercheck" method="post">
    <div>
        <label>Имя пользователя</label>
        <input type="text" name="username">
    </div>
    <div>
        <label>Пароль</label>
        <input type="password" name="password">
    </div>
    <div>
        <input type="submit" value="OK">
    </div>
</form>

</body>
</html>