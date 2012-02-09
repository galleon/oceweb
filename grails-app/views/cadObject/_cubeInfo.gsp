<g:form action="createCube" controller="CADObject" name="cubeForm" class="shapeForm">
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <div class="fieldcontain required">
                <label for="x1">
                    X1
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="x1" value="" required="true"/>
            </div>

            <div class="fieldcontain required">
                <label for="y1">
                    Y1
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="y1" value="" required="true"/>
            </div>

            <div class="fieldcontain required">
                <label for="z1">
                    Z1
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="z1" value="" required="true"/>
            </div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" action="createCube" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>