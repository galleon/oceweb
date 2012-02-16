<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="createSphere" controller="CADObject" name="sphereForm">
    <g:hiddenField name="type" value="${ShapeType.SPHERE}"/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <td:textField label="Radius" name="radius"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" id="submit" action="createSphere" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>