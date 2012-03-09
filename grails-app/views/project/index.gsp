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
    <div id="content">
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $(".showFace").click(function () {
            var url = $(this).attr('rel');
            $(".active-face").removeClass('active-face');
            $(this).addClass('active-face');
            return false;
        })
    })
</script>
</body>
</html>
