<g:uploadForm action="saveShapeFromFile" controller="CADObject" class="shapeForm" name="fileForm">
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
            <input type="file" name="file">
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="submit" value="${cadObject?.id?'Update':'Save'}" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:uploadForm>