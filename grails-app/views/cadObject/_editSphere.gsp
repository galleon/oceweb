<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="createSphere" controller="CADObject" name="sphereForm">
    <g:hiddenField name="type" value="${ShapeType.SPHERE}"/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <td:textField label="Radius" name="radius" value="${cadObject?.radius}"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Edit" id="submit" action="editSphere" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>