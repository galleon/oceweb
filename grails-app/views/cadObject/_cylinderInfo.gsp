<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="saveCylinder" controller="CADObject" class="shapeForm" name="cylinderForm">
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
            <g:render template="/cadObject/axisInfo" model="[x:cadObject?.x,y:cadObject?.y,z:cadObject?.z]"/>
            <td:textField label="Radius" name="radius" value="${cadObject?.radius}"/>
            <td:textField label="Height" name="height" value="${cadObject?.height}"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="submit" value="${cadObject?.id?'Update':'Save'}" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>