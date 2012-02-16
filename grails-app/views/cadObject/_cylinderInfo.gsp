<g:form action="createCylinder" controller="CADObject" name="cylinderForm">
    <g:hiddenField name="type" value="CYLINDER"/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <div class="fieldcontain required">
                <label for="radius">
                    Radius
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="radius" value="" required="true"/>
            </div>

            <div class="fieldcontain required">
                <label for="height">
                    Height
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="height" value="" required="true"/>
            </div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" id="submit" action="createCylinder" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>