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
    <fieldset class="form">
        <div class="block small left">
            <div class="block_head">
                <div class="bheadl"></div>

                <div class="bheadr"></div>

                <h2>Projects</h2>
            </div>

            <div class="block_content" id="projects">
                <ul>
                    <g:each in="${projects}" var="project" status="index">
                        <li id="${"phtml_" + (index + 1)}"><a href="#">${project.name}</a></li>
                    </g:each>
                </ul>
            </div>

            <div class="bendl"></div>

            <div class="bendr"></div>
        </div>

        <div class="block small right">
            <div class="block_head">
                <div class="bheadl"></div>

                <div class="bheadr"></div>

                <ul>
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

<div id="cubeInfo" style="display: none">
    <g:render template="/cadObject/cubeInfo"/>
</div>

<div id="sphereInfo" style="display: none">
    <g:render template="/cadObject/sphereInfo"/>
</div>

<div id="cylinderInfo" style="display: none">
    <g:render template="/cadObject/cylinderInfo"/>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $(".showFace").click(function () {
            var url = $(this).attr('rel');
            $(".active-face").removeClass('active-face');
            $(this).addClass('active-face');
            return false;
        })
        $('.nyroModal').nyroModal({filter:{title:''}});
        $('.nyroModalTitle').html('');
        $("#projects").jstree({"plugins":["themes", "html_data", "ui", "crrm", "hotkeys"], "themes":{"theme":"apple",
            "icons":true,
            "url":"${resource(dir:'css', file: 'style.css')}"
        }})
    })
</script>
</body>
</html>
