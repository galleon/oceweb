<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="saveExplode" class="shapeForm" controller="CADObject" name="explodeForm" >
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="submit" value="${cadObject?.id?'Update':'Save'}" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>