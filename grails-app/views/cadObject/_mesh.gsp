<%@ page import="org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="mesh" controller="util" name="meshForm">
    <g:hiddenField name="id" id="cadObjectId" value=""/>
    <fieldset class="form" >
        <div class="data-fields">
            <td:textField label="CAD Name" name="CADName"/>
            <td:textField label="Element Size" name="elementSize"/>
            <td:textField label=" Deflection" name="deflection"/>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="create" value="Create" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>
