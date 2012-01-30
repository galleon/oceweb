<!doctype html>
<html lang="en">
<head>
    <title>index</title>
    <meta name="layout" content="main"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">

</head>

<body>
<div>
    <g:hasErrors bean="${user}">
        <ul class="errors" role="alert">
            <g:renderErrors bean="${user}"/>
        </ul>
    </g:hasErrors>
    <g:form action="login" controller="home">
        <fieldset class="form">
            <div class="fieldcontain ${hasErrors(bean: user, field: 'username', 'error')} required">
                <label for="username">
                    Username
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="username" value="${user?.username}"/>
            </div>

            <div class="fieldcontain ${hasErrors(bean: user, field: 'password', 'error')} required">
                <label for="password">
                    Password
                    <span class="required-indicator">*</span>
                </label>
                <g:passwordField name="password" value="${user?.password}"/>
            </div>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="login" class="save" value="Login"/>
        </fieldset>
    </g:form>

</div>
</body>
</html>
