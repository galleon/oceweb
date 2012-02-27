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
    <div id="content">
    </div>
    <a href="#edit" id="#editLink" class="nyroModal"></a>
    <div id="edit" style="display: none"></div>


</div>

<script type="text/javascript">
    $(document).ready(function () {
        var shapeId = '${shapeId}';
        $(".showFace").click(function () {
            var url = $(this).attr('rel');
            $(".active-face").removeClass('active-face');
            $(this).addClass('active-face');
            return false;
        })
        if (shapeId) {
            showShape(shapeId);
        }
    })
</script>
</body>
</html>
