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
<script src="${resource(dir: 'data/three.js/build', file: 'Three.js')}"></script>
<script src="${resource(dir: 'data/three.js/examples/js', file: 'ImprovedNoise.js')}"></script>
<script src="${resource(dir: 'data/three.js/examples/js', file: 'RequestAnimationFrame.js')}"></script>
<script src="${resource(dir: 'data/three.js/examples/js', file: 'Stats.js')}"></script>
<script src="${resource(dir: 'js', file: 'cube.js')}"></script>
<script src="${resource(dir: 'js', file: 'cylinder.js')}"></script>

<body>
<div id="create" class="content scaffold-create" role="main">
    <h1>Cube</h1>

    <fieldset class="form">
        <div class="data-fields">
            <div class="fieldcontain required">
                <label for="width">
                    Width
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="width" value=""/>
            </div>
        </div>

        <div class="canvas">
            <div id="cubeResult" style="height: 200px"></div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <input type="button" class="save" value="Create Cube" id="createCube">
    </fieldset>

    <h1>Cylinder</h1>

    <fieldset class="form">
        <div class="data-fields">

            <div class="fieldcontain required">
                <label for="radius">
                    Radius
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="radius" value=""/>
            </div>

            <div class="fieldcontain required">
                <label for="height">
                    Height
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="height" value=""/>
            </div>
        </div>

        <div class="canvas">
            <div id="cylinderResult" style="height: 200px"></div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <input type="button" class="save" value="Create Cylinder" id="createCylinder">
    </fieldset>

    %{-- <h1>Sphere</h1>

    <fieldset class="form">
        <div class="fieldcontain required">
            <label for="x">
                x
                <span class="required-indicator">*</span>
            </label>
            <g:textField name="x" value=""/>
        </div>

        <div class="canvas">
            <canvas id="sphere"></canvas>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <input type="button" class="save" value="Create Sphere">
    </fieldset>--}%
</div>
<script type="text/javascript">

    $(document).ready(function () {
        $("#createCube").click(function () {
            var width = parseInt($("#width").val())
            if (isNaN(width)) {
                alert("Please enter valid number")
            } else {
                createCube(width);
                animateCube();
            }

        })
        $("#createCylinder").click(function () {
            var radius = parseInt($("#radius").val())
            var height = parseInt($("#height").val())
            if (isNaN(radius) || isNaN(height)) {
                alert("Please enter valid number")
            } else {
                createCylinder(radius, height);
                animateCylinder();
            }

        })
    })

</script>
</body>
</body>
</html>