<g:set var="computeCountValue" value="${"0".toInteger()}"/>
<g:each in="${cadSimulationObjects}" var="cadSimulationObject">
    <g:set var="computeCountValue" value="${computeCountValue.toInteger() + 1}"/>
    <g:if test="${computeCountValue == 1}">
        <ul>
   <li rel="result">
       <a   href="javascript:void(0)">Result</a>
   <ul>
    </g:if>

    <li id="${"phtml_" + (count)}" rel="${cadSimulationObject.type}">
        <a href="${createLink(controller: 'CADObject', action: 'show', id: cadSimulationObject.id)}"
           class="${cadSimulationObject.isMesh() && !cadSimulationObject?.parent?.isMesh() ? '' : 'showObject'}
           ${cadSimulationObject.parent ? 'parent_' + cadSimulationObject.parent.type : ''}"
           id="${cadSimulationObject.id}" title="Edit ${cadSimulationObject.type.value}"
           rel='${createLink(controller: "CADObject", action: "create${cadSimulationObject.type.value}", id: cadSimulationObject.id)}'>${cadSimulationObject.name}</a>
    </li>
</g:each>
<g:if test="${computeCountValue != 0}">
    </ul> </li></ul>
</g:if>
