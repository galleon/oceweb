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
    <script src="${resource(dir: 'js', file: 'Stats.js')}"></script>
    <script src="${resource(dir: 'js', file: 'application.js')}"></script>
    <script src="${resource(dir: 'js/three', file: 'Three.js')}"></script>
    <script src="${resource(dir: 'js/three', file: 'ImprovedNoise.js')}"></script>
    <script src="${resource(dir: 'js/three', file: 'RequestAnimationFrame.js')}"></script>
    <g:layoutHead/>
</head>

<body>
<div id="toolbox">
    <div id="logo" class="righttools clearfix">
        <g:form action="index" controller="project" name="changeProject">
            <ul class="light-list clearfix" style="width: 100%">
                <g:if test="${project}">
                    <li>
                        <g:select name="name" from="${projects}" optionKey="name" optionValue="name" noSelection="['': 'Choose Project']" value="${project?.name}"
                                  id='selectProject'/>
                    </li>
                    <g:render template="/project/confirmDelete" model="[project: project]"/>
                </g:if>
                <li class="last"><g:link uri="/">3-D Viewer</g:link><div id="spinner" style="display:none; text-align: right">Please wait while you content is loading</div></li>
            </ul>
        </g:form>
    </div>

    <div id="projectTree" title="Project">
        <g:render template="/project/cadObjects" model="[project: project]"/>
    </div>

    <div class="righttools clearfix">
        <g:render template="/project/toolbar"/>
    </div>
</div>
<g:layoutBody/>
</body>
</html>