<%@ page import="org.jcae.mesh.cad.CADShapeEnum" %>
<g:form action="mesh" controller="util" name="meshForm">
    <g:hiddenField name="id" id="cadObjectId" value=""/>
    <fieldset class="form" >
        <h3>Create a Mesh</h3>   <br/>
        <div class="data-fields">
            <div class="fieldcontain required">
                <label for="CADName">
                    CAD Name
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="CADName" required="false"/>
            </div>

            <div class="fieldcontain required">
                <label for="elementSize">
                    Element Size
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="elementSize" required="false"/>
            </div>

            <div class="fieldcontain required">
                <label for="deflection">
                    Deflection
                    <span class="required-indicator">*</span>
                </label>
                <g:textField name="deflection" required="false"/>
            </div>
        </div>
    </fieldset>
    <fieldset class="buttons">
        <g:submitButton name="create" value="Create" class="save"/>
        <input type="button" value="Cancel" id="cancel" class="save nyroModalClose"/>
    </fieldset>
</g:form>
