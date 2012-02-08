<!doctype html>
<html lang="en">
<head>
    <title>index</title>
    <meta name="layout" content="main"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <script src="${resource(dir: 'js', file: 'cube.js')}"></script>
    <script src="${resource(dir: 'js', file: 'cylinder.js')}"></script>
    <script src="${resource(dir: 'js', file: 'sphere.js')}"></script>
</head>

<body>
<div id="create" class="content scaffold-create" role="main">
    <h1>Position</h1>
    <fieldset class="form">
        <div class="data-fields">
            <div class="fieldcontain required">
                <label for="x">
                    X
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="x" value="100"/>
            </div>

            <div class="fieldcontain required">
                <label for="y">
                    Y
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="y" value="100"/>
            </div>

            <div class="fieldcontain required">
                <label for="z">
                    Z
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="z" value="0"/>
            </div>
        </div>

    </fieldset>

    <h1>Cube (Drag to spin)</h1>

    <fieldset class="form">
        <div class="data-fields">
            <div class="fieldcontain required">
                <label for="width">
                    Width
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="width" value="300"/>
            </div>
        </div>

        <div class="canvas">
            <div id="cubeResult" style="height: 200px"></div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <input type="button" class="save" value="Create Cube" id="createCube">
    </fieldset>

    <h1>Cylinder (Drag to spin)</h1>

    <fieldset class="form">
        <div class="data-fields">

            <div class="fieldcontain required">
                <label for="topRadius">
                    Radius
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="topRadius" value="300"/>
            </div>

            <div class="fieldcontain required">
                <label for="height">
                    Height
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="height" value="800"/>
            </div>
        </div>

        <div class="canvas">
            <div id="cylinderResult" style="height: 200px"></div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <input type="button" class="save" value="Create Cylinder" id="createCylinder">
    </fieldset>

    <h1>Sphere</h1>

    <fieldset class="form">
        <div class="data-fields">
            <div class="fieldcontain required">
                <label for="radius">
                    Radius
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="radius" value="500"/>
            </div>
        </div>

        <div class="canvas">
            <div id="sphereResult" style="height: 200px"></div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <input type="button" class="save" value="Create Sphere" id="createSphere">
    </fieldset>
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
            var radius = parseInt($("#topRadius").val())
            var height = parseInt($("#height").val())
            if (isNaN(radius) || isNaN(height)) {
                alert("Please enter valid number")
            } else {
                createCylinder(radius, height);
                animateCylinder();
            }

        })
        $("#createSphere").click(function () {
            var radius = parseInt($("#radius").val())
            if (isNaN(radius)) {
                alert("Please enter valid number")
            } else {
                createSphere(radius);
                animateSphere();
            }

        })
    })
</script>
</body>
</html>