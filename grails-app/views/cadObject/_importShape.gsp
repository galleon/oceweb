<g:uploadForm action="createShapeFromFile" controller="CADObject" name="cubeForm">
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <div class="fieldcontain required">
                <input type="file" name="file">
            </div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" action="createShapeFromFile" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:uploadForm>