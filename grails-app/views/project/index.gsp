<!doctype html>
<html lang="en">
<head>
    <title>Project</title>
    <meta name="layout" content="3dViewer"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <script type="text/javascript">
        $(function () {
            $("#cubeForm").submit(function () {
                var x = $("#cubeForm #x").val()
                var y = $("#cubeForm #y").val()
                var z = $("#cubeForm #z").val()
                var x1 = $("#cubeForm #x1").val()
                var y1 = $("#cubeForm #y1").val()
                var z1 = $("#cubeForm #z1").val()
                if ((x == x1) || (y == y1) || (z == z1)) {
                    alert("This is not the correct specification for a cube !!! ")
                    return false;
                }
                else {
                    return true;
                }
            })
        })
    </script>
</head>

<body>
<div>
    <g:hasErrors bean="${co}">
        <div class="errors" role="alert" id="instanceErrors">
            <ul>
                <g:eachError bean="${co}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
        </div>
    </g:hasErrors>
    <g:if test="${flash.error}">
        <ul class="errors" role="alert" id="flashError"><li>${flash.error}</li></ul>
    </g:if>
   <div id="content">
    </div>

</div>
<a href="#explode" class="nyroModal" style="display: none" id="explodeLink">Explode</a>
<a href="#mesh" class="nyroModal" style="display: none" id="meshLink">Mesh</a>

<div id="editCube">Edit Cube Area</div>


<div id="explode" style="display: none">
    <g:render template="/cadObject/explode"/>
</div>

<div id="mesh" style="display: none">
    <g:render template="/cadObject/mesh"/>
</div>
<a href="#operation" class="nyroModal" style="display: none" id="operationLink">Operation</a>

<div id="operation" style="display: none">
    <g:render template="/cadObject/operation"/>
</div>


<script type="text/javascript">
    var shapeId = '${shapeId}';
    $(document).ready(function () {
        $(".showFace").click(function () {
            var url = $(this).attr('rel');
            $(".active-face").removeClass('active-face');
            $(this).addClass('active-face');
            return false;
        })
        $('.nyroModal').nyroModal({filter:{title:''}});
        $('.nyroModalTitle').html('');
        if (shapeId) {
            var url = createLink('CADObject', 'show');
            url = url + '/' + shapeId;
            showShape(url);
        }
    })
</script>
</body>
</html>
