<%@ page import="com.eads.threedviewer.enums.ShapeType; org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="saveMesh" controller="CADObject" name="meshForm">
    <g:hiddenField name="project.id" value="${project?.id}"/>
    <g:hiddenField name="parent.id" id="cadObjectId" value=""/>
    <fieldset class="form">
        <div class="data-fields">
            <g:hiddenField name="id" value="${cadObject?.id}"/>
            <g:hiddenField name="type" value="${ShapeType.MESH}"/>
            <td:textField label="Name" name="name" value="${cadObject?.name}"/>
            <td:textField label="Element Size" name="size" value="${cadObject?.size}"/>
            <td:textField label=" Deflection" name="deflection" value="${cadObject?.deflection}"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="submit" value="${cadObject?.id?'Update':'Save'}" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
