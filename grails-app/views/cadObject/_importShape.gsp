<g:uploadForm action="createCube" controller="CADObject" name="cubeForm" class="shapeForm">
    <g:render template="/cadObject/shapeInfo"/>
    <fieldset class="form">
        <div class="data-fields">
            <div class="fieldcontain required">
                <input type="file" name="file">
            </div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" action="createCube" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:uploadForm>