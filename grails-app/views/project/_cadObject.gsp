<li id="${"phtml_" + (count)}" rel="${cadObject.type}">
    <a href="${createLink(controller: 'CADObject', action: 'show', id: cadObject.id)}"
       class="${cadObject.isMesh() && !cadObject?.parent?.isMesh() ? '' : 'showObject'}
       ${cadObject.parent ? 'parent_' + cadObject.parent.type : ''}"
       id="${cadObject.id}" title="Edit ${cadObject.type.value}"
       rel='${createLink(controller: "CADObject", action: "create${cadObject.type.value}", id: cadObject.id)}'>${cadObject.name}</a>
    <g:if test="${cadObject?.subCadObjects}">
        <g:if test="${cadObject.isMesh()}">
            <ul><li  rel="group"><a href="${createLink(controller: 'CADObject', action: 'show', id: cadObject.id)}"
            href="javascript:void(0)" id='${"phtml_" + (cadObject.id)}' class="${cadObject.isMesh() && !cadObject?.parent?.isMesh() ? '' : 'showObject'}
            ${cadObject.parent ? 'parent_' + cadObject.parent.type : ''}"
            rel='${createLink(controller: "CADObject", action: "create${cadObject.type.value}", id: cadObject.id)}'>Group</a>
            </g:if>
        <ul>
            <g:each in="${cadObject?.subCadObjects}" var="subCadObject">
                <g:set var="count" value="${count.toInteger() + 1}" scope="page"/>
                <g:render template="cadObject" model="[cadObject:subCadObject]"/>
            </g:each>
        </ul>
        <g:if test="${cadObject.isMesh()}">
            </li></ul>
            </g:if>
    </g:if>
</li>
