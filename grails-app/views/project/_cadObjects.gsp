<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:render template="/project/confirmDelete" model="[project: project]"/>

<div class="block small left">
    <div class="block_head">
            <g:form action="index" controller="project" name="changeProject" method="get">
                <g:select name="name" from="${projects}" optionKey="name" optionValue="name" noSelection="['': 'Choose Project']" value="${project?.name}" id='selectProject'/>
            </g:form>
    </div>

    <div class="block_content">
        <div id="project">
            <ul>
                <g:set var="count" value="1"/>
                <li id="${"phtml_" + (count)}" rel="root"><a href="#">${project.name}</a>
                    <ul>
                        <g:each in="${project.getParentCadObjects()}" var="cadObject">
                            <g:set var="count" value="${count.toInteger() + 1}"/>
                            <li id="${"phtml_" + (count)}" rel="${cadObject.type}">
                                <a href="${createLink(controller: 'CADObject', action: 'show', id: cadObject.id)}"
                                   class="showObject" id="${cadObject.id}" title="Edit ${cadObject.type.value}"
                                   rel='${createLink(controller: "CADObject", action: "create${cadObject.type.value}", id: cadObject.id)}' name = "${createLink(controller:
                                        "CADObject", action: "createMesh", params: ['project.id':cadObject.project.id, 'parent.id':cadObject.id, 'type':ShapeType.MESH])}">${cadObject.name}</a>
                                <ul>
                                    <g:each in="${cadObject.subCadObjects}" var="subCadObject">
                                        <g:set var="count" value="${count.toInteger() + 1}"/>
                                        <li id="${"phtml_" + (count)}" rel="${subCadObject.type}">
                                            <a lang="edit" href="${createLink(controller: 'CADObject', action: 'show', id: subCadObject.id)}"
                                               class="showObject" id="${subCadObject.id}" title="Edit ${subCadObject.type.value}"
                                               rel='${createLink(controller: "CADObject", action: "create${subCadObject.type.value}", id: subCadObject.id)}' name = "${createLink(controller:
                                                    "CADObject", action: "createMesh", params: ['id':subCadObject?.id, 'type':ShapeType.MESH])}">${subCadObject.name}</a>
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
            <g:render template="/cadObject/meshInfo" model="[project: project]"/>
        </div>
        <a href="#operation" class="model" style="display: none" id="operationLink">Operation</a>

        <div id="operation" style="display: none" title="Boolean Operation">
            <g:render template="/cadObject/operation"/>
        </div>

    </div>

</div>