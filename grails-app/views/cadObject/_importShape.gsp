<g:uploadForm action="createShapeFromFile" controller="CADObject" name="cubeForm">
    <fieldset class="form" style="width: 500px">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <input type="file" name="file">
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" action="createShapeFromFile" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:uploadForm>