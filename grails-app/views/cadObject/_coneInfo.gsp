<g:form action="createCone" controller="CADObject" name="coneForm" >
    <g:hiddenField name="type" value="CONE"/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <div class="fieldcontain required">
                <label for="baseRadius">
                    Base Radius
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="baseRadius" value="" required="true"/>
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
        <g:actionSubmit value="Create" id="submit" action="createCone" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>