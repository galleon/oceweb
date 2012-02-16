<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="createCylinder" controller="CADObject" name="cylinderForm">
    <g:hiddenField name="type" value="${ShapeType.CYLINDER}"/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <td:textField label="Radius" name="radius" value="${cadObject?.radius}"/>
            <td:textField label="Height" name="height" value="${cadObject?.height}"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Edit" id="submit" action="editCylinder" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>