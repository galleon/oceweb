<g:form action="createCube" controller="CADObject" name="cubeForm" class="shapeForm">
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <div class="fieldcontain required">
                <label for="length">
                    Length
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="length" value=""/>
            </div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" action="createCube" class="save"/>
    </fieldset>
</g:form>