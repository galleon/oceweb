<%@ page import="org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="mesh" controller="util" name="meshForm">
    <g:hiddenField name="parent.id" id="cadObjectId" value=""/>
    <fieldset class="form">
        <div class="data-fields">
            <g:render template="/cadObject/shapeInfo"/>
            <td:textField label="Element Size" name="size"/>
            <td:textField label=" Deflection" name="deflection"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="create" value="Create" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save closeModel"/>
    </fieldset>
</g:form>
