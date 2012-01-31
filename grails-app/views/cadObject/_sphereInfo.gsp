<g:form action="createSphere" controller="CADObject" name="sphereForm" class="shapeForm">

    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <div class="fieldcontain required">
                <label for="radius">
                    Radius
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="radius" value=""/>
            </div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" action="createSphere" class="save"/>
    </fieldset>
</g:form>