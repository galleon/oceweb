<%@ page import="com.eads.threedviewer.enums.ShapeType" %>
<g:form action="createCone" controller="CADObject" name="coneForm" >
    <g:hiddenField name="type" value="${ShapeType.CONE}"/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <g:render template="/cadObject/axisInfo"/>
            <td:textField label="Base Radius" name="baseRadius"/>
            <td:textField label="Height" name="height"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:actionSubmit value="Create" id="submit" action="createCone" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>