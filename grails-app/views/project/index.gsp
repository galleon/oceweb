<!doctype html>
<html lang="en">
<head>
    <title>Project</title>
    <meta name="layout" content="main"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <script type="text/javascript">
        $(function () {
            stats = new Stats();
            $("#frameArea").append(stats.domElement);
        })
    </script>
</head>

<body>
<div>
    <g:hasErrors bean="${co}">
        <g:renderErrors bean="${co}"/>
    </g:hasErrors>
        <div id="projectTree">
            <g:render template="/project/cadObjects" model="[projects: projects, project: project]"/>
        </div>
        <g:if test="${project}">
            <div id="toolbar">
                <g:render template="/project/toolbar" model="[project: project]"/>
            </div>

        </g:if>

        <div id="content">
        </div>


</div>
<a href="#explode" class="nyroModal" style="display: none" id="explodeLink">Explode</a>

<div id="explode" style="display: none">
    <g:render template="/cadObject/explode"/>
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
