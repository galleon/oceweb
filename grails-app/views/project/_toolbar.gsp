<div class="block small left">

    <div class="block_content">

        <g:hiddenField name="projectId" value="${project.id}"/>
        <ul>
            <li style="float: right;padding:5px ">
                <a href="#importShapeInfo" class="nyroModal" id="importShape" title="Import Shape">
                    <img src="${resource(dir: 'images', file: 'folder-big.png')}">
                </a>
            </li>
            <li style="float: right;padding:5px">
                <a href="#sphereInfo" class="nyroModal" id="createSphere" title="Create sphere">
                    <img src="${resource(dir: 'images', file: 'sphere.png')}">
                </a>
            </li>
            <li style="float: right;padding:5px">
                <a href="#cubeInfo" class="nyroModal" id="createCube" title="Create cube">
                    <img src="${resource(dir: 'images', file: 'cube.png')}">
                </a>
            </li>
            <li style="float: right;padding:5px">
                <a href="#cylinderInfo" class="nyroModal" id="createCylinder" title="Create cylinder">
                    <img src="${resource(dir: 'images', file: 'cylinder.png')}">
                </a>
            </li>
            <li style="float: right;padding:5px">
                <a href="#coneInfo" class="nyroModal" id="createCone" title="Create Cone">
                    <img src="${resource(dir: 'images', file: 'cone.jpg')}">
                </a>
            </li>
            <li style="float: left;" id="frameArea"></li>
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

        <div id="coneInfo" style="display: none">
            <g:render template="/cadObject/coneInfo"/>
        </div>

        <div id="importShapeInfo" style="display: none">
            <g:render template="/cadObject/importShape"/>
        </div>

    </div>


    <div class="bendl"></div>

    <div class="bendr"></div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        enableJsTree();
    })
</script>
