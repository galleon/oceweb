<g:uploadForm action="saveShapeFromFile" controller="CADObject" class="shapeForm" name="fileForm">
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
            <input type="file" name="file">
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:if test="${cadObject?.id}">
            <g:submitButton name="submit" value="Update" class="save"/>
        </g:if>
        <g:else>
            <g:submitButton name="Submit" value="Save"  class="save"/>
        </g:else>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:uploadForm>