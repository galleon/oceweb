<div class="block small left">
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

            <a href="#explode" class="model" style="display: none" id="explodeLink">Explode</a>
            <a href="#mesh" class="model" style="display: none" id="meshLink">Mesh</a>

            <div id="explode" style="display: none" title="Explode">
                <g:render template="/cadObject/explode"/>
            </div>

            <div id="mesh" style="display: none" title="Create Mesh">
                <g:render template="/cadObject/mesh"/>
            </div>
            <a href="#operation" class="model" style="display: none" id="operationLink">Operation</a>

            <div id="operation" style="display: none" title="Boolean Operation">
                <g:render template="/cadObject/operation"/>
            </div>

        </g:if>
        <g:else>
            Project Not Found
        </g:else>
    </div>

</div>