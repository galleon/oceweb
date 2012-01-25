<!doctype html>
<html lang="en">
<head>
    <title>index</title>
    <meta name="layout" content="main"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">

</head>

<body>
<jq:resources/>
<script src="${resource(dir: 'js/three', file: 'Three.js')}"></script>
<script src="${resource(dir: 'js/three', file: 'ImprovedNoise.js')}"></script>
<script src="${resource(dir: 'js/three', file: 'RequestAnimationFrame.js')}"></script>
<script src="${resource(dir: 'js', file: '3dViewer.js')}"></script>
<script src="${resource(dir: 'js', file: 'cube.js')}"></script>
<script src="${resource(dir: 'js', file: 'cylinder.js')}"></script>
<script src="${resource(dir: 'js', file: 'sphere.js')}"></script>
<script src="${resource(dir: 'js', file: 'shape.js')}"></script>

<body>
<div>
    <g:form action="save" name="explodeForm">
        <fieldset class="form">
            <div id="data" style="width: 29%;float: left;">

            </div>

            <div id="content" style="width: 70%;float: right;">

            </div>

        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="Explode"/>
        </fieldset>
    </g:form>

</div>
<script type="text/javascript">
    $(document).ready(function () {
        var url = "${createLink(controller: 'util',action:'explode')}";
        $("#explodeForm").submit(function () {
            $.get(url, function (data) {
                $("#data").html(data)
            })
            return false;
        })
    });

    var boxUrl = "${createLink(controller: 'util',action:'fetchBox')}";
    var camera, scene, renderer, loader, mesh, containerWidth;

    showShape(boxUrl, 'content');
    animate();
</script>
</body>
</body>
</html>
