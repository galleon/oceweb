<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="createCube" controller="CADObject" name="cubeForm">
    <g:hiddenField name="type" value="${ShapeType.CUBE}"/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <td:textField label="X1" name="x1" value="${cadObject?.x1}"/>
            <td:textField label="Y1" name="y1" value="${cadObject?.y1}"/>
            <td:textField label="Z1" name="z1" value="${cadObject?.z1}"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Edit" id="submit" action="editCube" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>