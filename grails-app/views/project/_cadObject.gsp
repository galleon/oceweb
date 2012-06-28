<%@ page import="com.eads.threedviewer.CADObject" %>
<li id="${"phtml_" + (count)}" rel="${cadObject.type}">
    <a href="${createLink(controller: 'CADObject', action: 'show', id: cadObject.id)}"
       class="${cadObject.isMesh() && !cadObject?.parent?.isMesh() ? '' : 'showObject'}
       ${cadObject.parent ? 'parent_' + cadObject.parent.type : ''}"
       id="${cadObject.id}" title="Edit ${cadObject.type.value}"
       rel='${createLink(controller: "CADObject", action: "create${cadObject.type.value}", id: cadObject.id)}'>${cadObject.name}</a>
    <g:if test="${cadObject?.subCadObjects}">
        <ul>
            <g:if test="${cadObject?.subCadObjects && subObjectType?.trim()?.equalsIgnoreCase("true")}">

                <g:set var="simulationObject" value="${cadObject?.subCadObjects.findAll {CADObject subObjectData ->
                    subObjectData.type?.toString()?.trim()?.equalsIgnoreCase('SIMULATED')
                }}"/>
                <g:set var="meshObject" value="${cadObject?.subCadObjects.findAll {CADObject subObjectData ->
                    subObjectData.isMesh()
                }}"/>
                <g:render template="cadMeshObjects" model="[cadMeshObject: meshObject,cadMainObject:cadObject]"/>
                <g:render template="cadComputeObject" model="[cadSimulationObjects: simulationObject,cadMainObject:cadObject]"/>
                <g:set var="subMeshSimuType" value="true"/>
            </g:if>
            <g:else>
                <g:set var="subMeshSimuType" value="false"/>
            </g:else>
            <g:each in="${cadObject?.subCadObjects}" var="subCadObject">
                <g:set var="count" value="${count.toInteger() + 1}" scope="page"/>
                <g:if test="${subCadObject.isMesh()}">
                    <g:set var="subObjectType" value="true"/>
                </g:if>
                <g:if test="${!subMeshSimuType?.trim()?.equalsIgnoreCase("true")}">
                    <g:if test="${subCadObject?.subCadObjects}">
                        <g:render template="cadObject" model="[cadObject: subCadObject]"/>
                    </g:if>
                    <g:else>
                        <g:set var="simulationObject" value="${cadObject?.subCadObjects.findAll {CADObject subObjectData ->
                            subObjectData.type?.toString()?.trim()?.equalsIgnoreCase('SIMULATED')
                        }}"/>
                        <g:set var="meshObject" value="${cadObject?.subCadObjects.findAll {CADObject subObjectData ->
                            subObjectData.isMesh()
                        }}"/>
                        <g:render template="cadMeshObjects" model="[cadMeshObject: meshObject,cadMainObject:cadObject]"/>
                        <g:render template="cadComputeObject" model="[cadSimulationObjects: simulationObject,cadMainObject:cadObject]"/>
                        <g:set var="subMeshSimuType" value="true"/>
                    </g:else>
                </g:if>
            </g:each>
        </ul>
    </g:if>
</li>
