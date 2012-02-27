<div class="fieldcontain required">
    <label for="${name}">
        ${label}
        <g:if test="${required}">
            <span class="required-indicator">*</span>
        </g:if>
    </label>
    <input type="text" name="${name}" id="${id}" value="${value}" "${required}"/>
</div>