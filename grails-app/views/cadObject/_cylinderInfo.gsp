<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="createCylinder" controller="CADObject" name="cylinderForm">
    <g:hiddenField name="type" value="${ShapeType.CYLINDER}"/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <td:textField label="Radius" name="radius"/>
            <td:textField label="Height" name="height"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" id="submit" action="createCylinder" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>