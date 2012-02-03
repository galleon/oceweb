<g:form action="createSphere" controller="CADObject" name="sphereForm" class="shapeForm">

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
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" action="createSphere" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>