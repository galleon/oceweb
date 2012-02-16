<g:form action="createCube" controller="CADObject" name="cubeForm">
    <g:hiddenField name="type" value="CUBE"/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <td:textField label="X1" name="x1"/>
            <td:textField label="Y1" name="y1"/>
            <td:textField label="Z1" name="z1"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" id="submit" action="createCube" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>