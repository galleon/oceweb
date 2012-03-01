<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="saveCube" controller="CADObject" name="cubeForm" class="shapeForm">
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo" model="[cadObject:cadObject]"/>
            <g:render template="/cadObject/axisInfo" model="[x:cadObject?.x,y:cadObject?.y,z:cadObject?.z]"/>
            <td:textField label="X1" name="x1" value="${cadObject?.x1}"/>
            <td:textField label="Y1" name="y1" value="${cadObject?.y1}"/>
            <td:textField label="Z1" name="z1" value="${cadObject?.z1}"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="submit" value="${cadObject?.id?'Update':'Save'}" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>