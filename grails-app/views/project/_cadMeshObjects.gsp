<g:set var="meshCountValue" value="${"0".toInteger()}"/>
<g:each in="${cadMeshObject}" var="cadObjectValue">
    <g:set var="meshCountValue" value="${meshCountValue.toInteger() + 1}"/>
    <g:if test="${meshCountValue == 1}">

   <li rel="group" id='${"phtml_" + (count)}'>
       <a href="javascript:void(0)">Group</a>
   <ul>
    </g:if>

    <li id="${"phtml_" + (count)}" rel="${cadObjectValue.type}">
        <a href="${createLink(controller: 'CADObject', action: 'show', id: cadObjectValue.id)}"
           class="${cadObjectValue.isMesh() && !cadObjectValue?.parent?.isMesh() ? '' : 'showObject'}
           ${cadObjectValue.parent ? 'parent_' + cadObjectValue.parent.type : ''}"
           id="${cadObjectValue.id}" title="Edit ${cadObjectValue.type.value}"
           rel='${createLink(controller: "CADObject", action: "create${cadObjectValue.type.value}", id: cadObjectValue.id)}'>${cadObjectValue.name}</a>
    </li>
</g:each>
<g:if test="${meshCountValue != 0}">
    </ul> </li>
</g:if>
