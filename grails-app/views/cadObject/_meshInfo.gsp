<%@ page import="com.eads.threedviewer.enums.ShapeType; org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="saveMesh" class="shapeForm" controller="CADObject" name="meshForm">
    <fieldset class="form">
        <div class="data-fields">
            <g:hiddenField name="id" value="${cadObject?.id}"/>
            <td:textField label="Name" name="name" value="${cadObject?.name}"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="submit" value="Save" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
