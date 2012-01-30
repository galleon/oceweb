<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle title="3D Viewer" default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
    <jq:resources/>
    <script src="${resource(dir: 'js/three', file: 'Three.js')}"></script>
    <script src="${resource(dir: 'js/three', file: 'ImprovedNoise.js')}"></script>
    <script src="${resource(dir: 'js/three', file: 'RequestAnimationFrame.js')}"></script>
    <script src="${resource(dir: 'js', file: '3dViewer.js')}"></script>
    <script src="${resource(dir: 'js', file: 'cube.js')}"></script>
    <script src="${resource(dir: 'js', file: 'cylinder.js')}"></script>
    <script src="${resource(dir: 'js', file: 'sphere.js')}"></script>
    <g:layoutHead/>
</head>

<body>
<div class="nav" role="navigation">
    <ul>
        <td:ifLoggedIn>
            <li style="float: right;"><g:link controller="home" action="logout">Logout</g:link></li>
            <li style="float: right;margin-top: 4px;" class="username">Welcome <td:loggedInUserName/></li>
        </td:ifLoggedIn>
        <td:ifNotLoggedIn>
            <li>3-D Viewer</li>
        </td:ifNotLoggedIn>

    </ul>
</div>
<g:layoutBody/>
<div class="footer" role="contentinfo"></div>

<div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
</body>
</html>