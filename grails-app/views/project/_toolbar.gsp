<g:if test="${project}">
    <g:hiddenField name="projectId" value="${project.id}"/>
    <ul class="toolbox-list clearfix">
        <li><a title="Create sphere" class="sphere model" href="#sphereInfo"></a></li>
        <li><a title="Create Cube" class="cube model" href="#cubeInfo"></a></li>
        <li><a title="Create Cylinder" class="cylinder model" href="#cylinderInfo"></a></li>
        <li><a title="Create Cone" class="cone model" href="#coneInfo"></a></li>
        <li><a title="Import File" class="plane model" href="#importShapeInfo"></a></li>
    </ul>

    <div id="cubeInfo" style="display: none" title="Create Cube">
        <g:render template="/cadObject/cubeInfo"/>
    </div>

    <div id="sphereInfo" style="display: none" title="Create Sphere">
        <g:render template="/cadObject/sphereInfo"/>
    </div>

    <div id="cylinderInfo" style="display: none" title="Create Cylinder">
        <g:render template="/cadObject/cylinderInfo"/>
    </div>

    <div id="coneInfo" style="display: none" title="Create Cone">
        <g:render template="/cadObject/coneInfo"/>
    </div>

    <div id="importShapeInfo" style="display: none" title="Import Shape">
        <g:render template="/cadObject/importShape"/>
    </div>
</g:if>
<ul class="export-list">
    <li id="frameArea"></li>
</ul>

