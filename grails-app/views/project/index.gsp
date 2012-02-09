<!doctype html>
<html lang="en">
<head>
    <title>Project</title>
    <meta name="layout" content="main"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">

</head>

<body>
<div>
    <g:hasErrors bean="${co}">
        <g:renderErrors bean="${co}"/>
    </g:hasErrors>
    <fieldset class="form">
        <div id="projectTree">
            <g:render template="/project/cadObjects" model="[projects: projects, project: project]"/>
        </div>

        <div class="block small right">
            <div class="block_head">
                <div class="bheadl"></div>

                <div class="bheadr"></div>
                <g:if test="${project}">
                    <g:hiddenField name="projectId" value="${project.id}"/>
                    <ul>
                        <li style="float: right;">
                            <a href="#importShapeInfo" class="nyroModal" id="importShape" title="Import Shape">
                                <img src="${resource(dir: 'images', file: 'folder-big.png')}">
                            </a>
                        </li>
                        <li style="float: right;">
                            <a href="#sphereInfo" class="nyroModal" id="createSphere" title="Create sphere">
                                <img src="${resource(dir: 'images', file: 'sphere.png')}">
                            </a>
                        </li>
                        <li style="float: right;">
                            <a href="#cubeInfo" class="nyroModal" id="createCube" title="Create cube">
                                <img src="${resource(dir: 'images', file: 'cube.png')}">
                            </a>
                        </li>
                        <li style="float: right;">
                            <a href="#cylinderInfo" class="nyroModal" id="createCylinder" title="Create cylinder">
                                <img src="${resource(dir: 'images', file: 'cylinder.png')}">
                            </a>
                        </li>
                    </ul>

                    <div id="cubeInfo" style="display: none">
                        <g:render template="/cadObject/cubeInfo"/>
                    </div>

                    <div id="sphereInfo" style="display: none">
                        <g:render template="/cadObject/sphereInfo"/>
                    </div>

                    <div id="cylinderInfo" style="display: none">
                        <g:render template="/cadObject/cylinderInfo"/>
                    </div>

                    <div id="importShapeInfo" style="display: none">
                        <g:render template="/cadObject/importShape"/>
                    </div>
                </g:if>

            </div>

            <div class="block_content">
                <div id="content" style="height: 400px;">

                </div>
            </div>

            <div class="bendl"></div>

            <div class="bendr"></div>
        </div>

    </fieldset>
    <fieldset class="buttons">
        <a href="#" id="info" style="float: right;">
            <img src="${resource(dir: 'images', file: 'info.png')}" alt="info">
        </a>
        <a href="#" style="float: right;">
            <img src="${resource(dir: 'images', file: 'folder-big.png')}" alt="projects">
        </a>
    </fieldset>
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
            showShape(url, 'content', {}, {});
        }
    })
</script>
</body>
</html>
