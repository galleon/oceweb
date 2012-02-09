<div class="block small left">
    <div class="block_head">
        <div class="bheadl"></div>

        <div class="bheadr"></div>
        <g:form action="index" controller="project" name="changeProject">
            <g:select name="name" from="${projects}" optionKey="name" optionValue="name" style="width: 80%;"
                      noSelection="['': 'Choose Project']" value="${project?.name}"
                      id='selectProject'/>
        </g:form>
    </div>

    <div class="block_content">
        <g:if test="${project}">
            <div id="project">
                <ul>
                    <g:set var="count" value="1"/>
                    <li id="${"phtml_" + (count)}"><a href="#">${project.name}</a>
                        <ul>
                            <g:each in="${project.getParentCadObjects()}" var="cadObject">
                                <g:set var="count" value="${count.toInteger() + 1}"/>
                                <li id="${"phtml_" + (count)}">
                                    <a href="${createLink(controller: 'CADObject', action: 'show', id: cadObject.id)}"
                                       class="showObject" id="${cadObject.id}">${cadObject.name}</a>
                                    <ul>
                                        <g:each in="${cadObject.subCadObjects}" var="subCadObject">
                                            <g:set var="count" value="${count.toInteger() + 1}"/>
                                            <li id="${"phtml_" + (count)}">
                                                <a href="${createLink(controller: 'CADObject', action: 'show', id: subCadObject.id)}"
                                                   class="showObject" id="${subCadObject.id}">${subCadObject.name}</a>
                                            </li>
                                        </g:each>
                                    </ul>
                                </li>
                            </g:each>

                        </ul>
                    </li>
                </ul>
            </div>
            <g:if test="${project}">
                <g:hiddenField name="projectId" value="${project.id}"/>
                <ul style="margin-top: 20px">
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
                    <li style="float: left;" id="frameArea" ></li>
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
        </g:if>
        <g:else>
            Project Not Found
        </g:else>
    </div>


    <div class="bendl"></div>

    <div class="bendr"></div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        enableJsTree();
    })
</script>
