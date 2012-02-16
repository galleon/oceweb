<script type="text/javascript">
    $(document).ready(function () {
        enableJsTree();
    })
</script>
<g:if test="${project}">
    <g:hiddenField name="projectId" value="${project.id}"/>
    <ul class="toolbox-list clearfix">
        <li><a title="Create sphere" class="sphere nyroModal" href="#sphereInfo"></a></li>
        <li><a title="Create Cube" class="cube nyroModal" href="#cubeInfo"></a></li>
        <li><a title="Create Cylinder" class="cylinder nyroModal" href="#cylinderInfo"></a></li>
        <li><a title="Create Cone" class="cone nyroModal" href="#coneInfo"></a></li>
        <li><a title="Import File" class="plane nyroModal" href="#importShapeInfo"></a></li>
    </ul>
    <ul class="export-list">
        <li id="frameArea"></li>
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
</g:if>