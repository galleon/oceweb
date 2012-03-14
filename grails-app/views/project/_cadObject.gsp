<g:set var="count" value="${count.toInteger() + 1}"/>
<li id="${"phtml_" + (count)}" rel="${cadObject.type}">
    <a href="${createLink(controller: 'CADObject', action: 'show', id: cadObject.id)}" class="showObject" id="${cadObject.id}" title="Edit ${cadObject.type.value}"
       rel='${createLink(controller: "CADObject", action: "create${cadObject.type.value}", id: cadObject.id)}'>${cadObject.name}</a>
    <g:if test="${cadObject?.subCadObjects}">
        <ul>
            <g:each in="${cadObject?.subCadObjects}" var="subCadObject">
                <g:render template="cadObject" model="[cadObject:subCadObject]"/>
            </g:each>
        </ul>
    </g:if>
</li>
