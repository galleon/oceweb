<g:form action="createCylinder" controller="CADObject" name="cylinderForm" class="shapeForm">
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

            <div class="fieldcontain required">
                <label for="height">
                    Height
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="height" value=""/>
            </div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" action="createCylinder" class="save"/>
    </fieldset>
</g:form>