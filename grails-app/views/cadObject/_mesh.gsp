<%@ page import="com.eads.threedviewer.enums.ShapeType; org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="saveMesh" class="shapeForm" controller="CADObject" name="meshForm">
    <g:hiddenField name="project.id" value="${project?.id}"/>
    <g:hiddenField name="parent.id" id="cadObjectId" value=""/>
    <g:hiddenField name="type" value="${ShapeType.MESH}"/>
    <fieldset class="form">
        <div class="data-fields">
            <td:textField label="Name" name="name" value=""/>
            <td:textField label="Element Size" name="size" value=""/>
            <td:textField label=" Deflection" name="deflection" value=""/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="submit" value="Save" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
