<g:uploadForm action="saveShapeFromFile" controller="CADObject" name="cubeForm">
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
            <input type="file" name="file">
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" action="saveShapeFromFile" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:uploadForm>