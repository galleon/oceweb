<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>3-D Viewer | <g:layoutTitle default="3-D Viewer"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${resource(dir: 'images', file: 'apple-touch-icon-retina.png')}">
    <link rel="stylesheet" href="${resource(dir: 'css', file: '3dViewer.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'style.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'mobile.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'colorpicker.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.ui.all.css')}" type="text/css">
    <jq:resources/>
    <script src="${resource(dir: 'js', file: 'jquery.ui.core.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.ui.widget.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.ui.mouse.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.ui.button.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.ui.draggable.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.ui.position.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.ui.resizable.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.ui.dialog.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.mousewheel.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.cookie.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.hotkeys.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.jstree.js')}"></script>
    <script src="${resource(dir: 'js', file: 'jquery.form.js')}"></script>
    <script src="${resource(dir: 'js', file: 'colorpicker.js')}"></script>
    <script src="${resource(dir: 'js', file: 'eye.js')}"></script>
    <script src="${resource(dir: 'js', file: 'Stats.js')}"></script>
    <script src="${resource(dir: 'js', file: 'application.js')}"></script>
    <script src="${resource(dir: 'js/three', file: 'Three.js')}"></script>
    <script src="${resource(dir: 'js/three', file: 'ImprovedNoise.js')}"></script>
    <script src="${resource(dir: 'js/three', file: 'RequestAnimationFrame.js')}"></script>
    <g:layoutHead/>
</head>

<body>
<g:if test="${project}">
    <div id="projectTree" title="Project">
        <g:render template="/project/cadObjects" model="[project: project,projects:projects]"/>
    </div>

</g:if>
<div id="dialog-confirm" title="Delete selected items?" style="display: none;">
    <p></p>
</div>

<div id="toolbox">
    <div class="righttools clearfix">
        <div id="frameArea"><g:link uri="/">3-D Viewer</g:link></div>
        <g:if test="${project}">
            <g:render template="/project/toolbar"/>
        </g:if>
    </div>
</div>
<g:layoutBody/>
<div id="spinner" class="${project ? 'info' : 'errormsg'} message" style="display: ${project ? 'none' : 'block'};">
    <p>
        ${project ? 'Please wait while you content is loading' : 'Project not found'}
    </p>
    <a href="#" title="close" id="closeFlash"><g:img dir="images" file="close.png" alt="close" class="close-img"/></a>
</div>
</body>
</html>